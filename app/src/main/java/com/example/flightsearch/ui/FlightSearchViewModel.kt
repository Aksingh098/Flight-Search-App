package com.example.flightsearch.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flightsearch.FSearchApplication
import com.example.flightsearch.data.repository.FSearchRepository
import com.example.flightsearch.data.toDomain
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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class FlightSearchViewModel(
    private val repository: FSearchRepository
) : ViewModel(){

    private val _searchQuery = MutableStateFlow("")
    private val _searchExpanded = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private val _suggestionFlow = _searchQuery
        .debounce(300L)
        .distinctUntilChanged()
        .flatMapLatest { query->
            repository.autoSuggestion(query)

        }



    val uiState: StateFlow<FlightSearchUiState> = combine(
        _searchQuery,
        _searchExpanded,
        _suggestionFlow
    ){ query, expanded, suggestions ->
        FlightSearchUiState(
            searchedQuery = query,
            searchExpanded = expanded,
            airportSuggestion = suggestions.toDomain()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = FlightSearchUiState()
    )

    fun expandChange( expand: Boolean){
        _searchExpanded.value = expand

    }

    fun onSearchQueryChange(query: String){
        _searchQuery.value = query



    }

    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as FSearchApplication)
                FlightSearchViewModel(application.container.fSearchRepository)

            }
        }
    }

}

