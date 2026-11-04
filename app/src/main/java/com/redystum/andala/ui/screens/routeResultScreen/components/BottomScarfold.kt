package com.redystum.andala.ui.screens.routeResultScreen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.redystum.andala.data.remote.RouteOption
import com.redystum.andala.data.Pousada
import com.redystum.andala.data.Pousadas
import com.redystum.andala.ui.screens.exploreScreen.components.MapComponent
import com.redystum.andala.ui.theme.AndaLaTheme
import com.redystum.andala.ui.viewmodel.TripViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultMapScreen(
    pousadas: List<Pousada>,
    highlightPousadas: MutableList<Pousada>,
    viewModel: TripViewModel,
    scaffoldState: BottomSheetScaffoldState,
    onConfirmRoute: () -> Unit,
    routeOptions: List<RouteOption> = emptyList(),
    selectedRouteByLeg: Map<Int, Int> = emptyMap(),
    onRouteSelected: (legIndex: Int, optionIndex: Int) -> Unit = { _, _ -> }
) {


    val selectedPousadas = viewModel.selectedPousadas

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContainerColor = MaterialTheme.colorScheme.surface,
        sheetContentColor = MaterialTheme.colorScheme.onSurface,
        sheetShadowElevation = 8.dp,
        sheetTonalElevation = 0.dp,
        sheetShape = MaterialTheme.shapes.large,
        sheetPeekHeight = 360.dp,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = if (routeOptions.isEmpty()) {
                        "A procurar rotas..."
                    } else {
                        "Rotas disponíveis"
                    },
                    style = MaterialTheme.typography.titleMedium
                )
                LazyColumn {
                    items(
                        items = routeOptions.groupBy { it.legIndex }.toList(),
                        key = { (legIndex, _) -> legIndex }
                    ) { (legIndex, options) ->
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Viagem ${legIndex + 1}",
                                style = MaterialTheme.typography.labelLarge,
                                modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
                            )
                            LazyRow(
                                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(
                                    8.dp
                                )
                            ) {
                                items(
                                    items = options,
                                    key = { option ->
                                        "${option.legIndex}-${option.departureTime}-${option.route.hashCode()}"
                                    }
                                ) { option ->
                                    val optionIndex = options.indexOf(option)
                                    Card(
                                        onClick = {
                                            onRouteSelected(legIndex, optionIndex)
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth(0.82f)
                                            .padding(vertical = 4.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (
                                                selectedRouteByLeg[legIndex] == optionIndex
                                            ) {
                                                MaterialTheme.colorScheme.primaryContainer
                                            } else {
                                                MaterialTheme.colorScheme.surfaceVariant
                                            }
                                        )
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(
                                                text = option.departureTime
                                                    .substringAfter('T')
                                                    .take(5)
                                            )
                                            Text(
                                                text = "${option.durationMinutes ?: "—"} min" +
                                                    if (option.transitLines.isEmpty()) {
                                                        ""
                                                    } else {
                                                        " • ${option.transitLines.joinToString()}"
                                                    },
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            MapComponent(
                screen = 1,
                pousadas = pousadas,
                highlightPousada = highlightPousadas,
                selectedPousadas = selectedPousadas,
                onPousadaSelected = { pousada -> viewModel.toggleSelection(pousada) },
                routePath = routeOptions
                    .groupBy { it.legIndex }
                    .toSortedMap()
                    .values
                    .mapNotNull { options ->
                        val selectedIndex = selectedRouteByLeg[options.first().legIndex]
                        options.getOrNull(selectedIndex ?: 0)?.path
                    }
                    .flatMapIndexed { index, path ->
                        if (index == 0) path else path.drop(1)
                    }
            )
        }
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun ScaffoldPreview(
    viewModel: TripViewModel = TripViewModel()
) {
    val pousadas = Pousadas.listaPousadas
    val a = mutableListOf<Pousada>()
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded
        )
    )

    AndaLaTheme {
        ResultMapScreen(
            pousadas,
            a,
            viewModel,
            scaffoldState,
            {}
        )
    }
}
