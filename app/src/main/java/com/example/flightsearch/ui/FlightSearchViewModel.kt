package com.example.flightsearch.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flightsearch.FSearchApplication
import com.example.flightsearch.data.local.FavoriteEntity
import com.example.flightsearch.data.repository.FSearchRepository
import com.example.flightsearch.data.toAirportDomain
import com.example.flightsearch.data.toDomain
import com.example.flightsearch.data.toFavoriteDomain
import com.example.flightsearch.domain.FlightRoute
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FlightSearchViewModel(
    private val repository: FSearchRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _searchExpanded = MutableStateFlow(false)



    private val _selectedAirportCode = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private val _suggestionFlow = _searchQuery
        .debounce(300L)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            repository.autoSuggestion(query)

        }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private val _flightResultsFlow = _selectedAirportCode
        .flatMapLatest { code ->
            if (code.isNullOrBlank()) {
                flowOf(emptyList())
            } else {
                repository.getSelectedAirport(code)
                    .combine(repository.getAllDestination(code)) { departure, destinations ->
                        destinations.map { dest ->
                            FlightRoute(
                                departureCode = departure.iataCode,
                                departureName = departure.name,
                                destinationCode = dest.iataCode,
                                destinationName = dest.name
                            )

                        }


                    }
            }

        }

    private val _favoriteFlow = repository.getFavorites()


    val uiState: StateFlow<FlightSearchUiState> = combine(
        _searchQuery,
        _searchExpanded,
        _suggestionFlow,
        _flightResultsFlow,
        _favoriteFlow
    ) { query, expanded, suggestions, results,favorites ->
        val updatedResults = results.map{route ->
            val isFavorite = favorites.any{favorite ->
                favorite.departureCode == route.departureCode && favorite.destinationCode == route.destinationCode
            }
            route.copy(isFavorite =isFavorite)

        }
        FlightSearchUiState(
            searchedQuery = query,
            searchExpanded = expanded,
            airportSuggestion = suggestions.toAirportDomain(),
            flightResults = updatedResults,
            hasExecutedSearch = _selectedAirportCode.value != null,
            favorites = favorites.toFavoriteDomain()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = FlightSearchUiState()
    )

    fun expandChange(expand: Boolean) {
        _searchExpanded.value = expand

    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query


    }


    fun onFavoriteClick(departureCode: String, destinationCode: String){
        viewModelScope.launch {
            val isCurrentFavorite = uiState.value.favorites.any{favorite ->
                favorite.departureCode == departureCode && favorite.destinationCode == destinationCode

            }

            if(isCurrentFavorite){
                repository.deleteFavorite(departureCode,destinationCode)
            }else{
                repository.addFavorite(
                    favorite = FavoriteEntity(
                        id = 0,
                        departureCode = departureCode,
                        destinationCode = destinationCode

                    )
                )

            }
        }


    }

    fun onAirportSelected(iataCode: String) {
        _searchQuery.value = iataCode
        _selectedAirportCode.value = iataCode
        _searchExpanded.value = false
    }




    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as FSearchApplication)
                FlightSearchViewModel(application.container.fSearchRepository)

            }
        }
    }

}

