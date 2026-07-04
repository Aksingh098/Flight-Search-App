package com.example.flightsearch.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearch.R
import com.example.flightsearch.ui.theme.FlightSearchTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightSearchScreen(
    modifier: Modifier = Modifier,
    viewModel: FlightSearchViewModel = viewModel(factory = FlightSearchViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Column(
        modifier = modifier
    ) {
        Box(modifier = Modifier.fillMaxWidth()){
            SearchBar(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 8.dp),
                expanded = uiState.searchExpanded,
                onExpandedChange = {viewModel.expandChange(it)},
                inputField = {
                    SearchBarDefaults.InputField(
                        query = uiState.searchedQuery,
                        onQueryChange = {newQuery ->
                            viewModel.onSearchQueryChange(newQuery)

                        },
                        onSearch = {
                            viewModel.expandChange(false)
                        },
                        expanded = uiState.searchExpanded,
                        onExpandedChange = {viewModel.expandChange(it)},
                        placeholder = { Text(stringResource(R.string.searchbar_placeholder)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        },
                        trailingIcon = {
                            if(uiState.searchExpanded && uiState.searchedQuery.isNotEmpty()){
                                IconButton(
                                    onClick = { viewModel.onSearchQueryChange("") }
                                ) {
                                    Icon(
                                        Icons.Default.Clear,
                                        contentDescription = stringResource(R.string.searchbar_clear)
                                    )
                                }
                            }
                        }
                    )
                }

            ){
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(uiState.airportSuggestion.take(7  )) { airport ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable{viewModel.onAirportSelected(airport.iataCode)}
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${airport.iataCode}:",
                                fontWeight = FontWeight.Bold,
                            )
                            Spacer(modifier = Modifier.width(5.dp))

                            Text(
                                text = airport.name
                            )
                        }


                    }
                }

            }
        }
        if(!uiState.searchExpanded && uiState.hasExecutedSearch){
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(uiState.flightResults){route ->
                    AirportCard(
                        destinationName = route.destinationName,
                        departureName = route.departureName,
                        destinationCode = route.destinationCode,
                        departureCode = route.departureCode
                    )

                }
            }


            }
        }




}

@Composable
fun AirportCard(
    departureCode: String,
    departureName: String,
    destinationCode: String,
    destinationName: String,
    modifier: Modifier = Modifier
){
    ElevatedCard(
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 6.dp
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.SpaceAround

        ) {
            Text(
                stringResource(R.string.depart),
                style = MaterialTheme.typography.labelLarge
            )

            AirportName(departureCode,departureName)

            Text(
                stringResource(R.string.arrival),
                style = MaterialTheme.typography.labelLarge
            )
            AirportName(destinationCode,destinationName)
        }
    }

}

@Composable
fun AirportName(
    code: String,
    name: String,
    modifier: Modifier = Modifier
){
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            code,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = modifier.width(6.dp))
        Text(
            name,
            style = MaterialTheme.typography.bodyMedium
        )

    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AirportCardPreview() {
    FlightSearchTheme {
        AirportCard(
            departureCode = "OSL",
            departureName = "Oslo Airport",
            destinationCode = "LIS",
            destinationName = "Humberto Diegado Airport"
        )
    }
}