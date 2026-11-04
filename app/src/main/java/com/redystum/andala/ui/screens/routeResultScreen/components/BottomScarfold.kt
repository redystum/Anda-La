package com.redystum.andala.ui.screens.routeResultScreen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
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
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Text(
                    text = if (routeOptions.isEmpty()) {
                        "A procurar rotas..."
                    } else {
                        "Escolha as suas rotas"
                    },
                    style = MaterialTheme.typography.titleLarge
                )
                val selectedOptions = routeOptions
                    .groupBy { it.legIndex }
                    .mapNotNull { (legIndex, options) ->
                        options.getOrNull(selectedRouteByLeg[legIndex] ?: 0)
                    }
                val totalMinutes = selectedOptions.sumOf { it.durationMinutes ?: 0 }
                Text(
                    text = "Duração total: ${formatDuration(totalMinutes)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
                )
                LazyColumn {
                    items(
                        items = routeOptions.groupBy { it.legIndex }.toList(),
                        key = { (legIndex, _) -> legIndex }
                    ) { (legIndex, options) ->
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Trecho ${legIndex + 1}",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                                            .fillMaxWidth(0.88f)
                                            .padding(vertical = 4.dp),
                                        shape = RoundedCornerShape(16.dp),
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
                                        RouteOptionCard(option)
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = onConfirmRoute,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(26.dp)
                ) {
                    Text("Confirmar rota")
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(20.dp)
                    )
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

@Composable
private fun RouteOptionCard(option: RouteOption) {
        val transitDetails = option.route.legs
            .flatMap { it.steps }
            .mapNotNull { it.transitDetails }
        val stopNames = transitDetails.flatMap { details ->
            listOfNotNull(
                details.stopDetails?.departureStop?.name,
                details.stopDetails?.arrivalStop?.name
            )
        }.distinct()
        val lineName = transitDetails
            .firstNotNullOfOrNull { it.transitLine?.name }
            ?: "Ligação"
        val isTrain = transitDetails.any {
            it.transitLine?.vehicle?.type == "HEAVY_RAIL"
        }

        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Icon(
                    imageVector = if (isTrain) Icons.Default.Train else Icons.Default.DirectionsBus,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp)
                ) {
                    Text(text = lineName, style = MaterialTheme.typography.titleSmall)
                    Text(
                        text = "Duração: ${formatDuration(option.durationMinutes ?: 0)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Divider(modifier = Modifier.padding(vertical = 10.dp))
            if (stopNames.isEmpty()) {
                TimelineStop("Ponto de partida", "Início")
                TimelineStop("Destino", "Chegada", isLast = true)
            } else {
                TimelineStop(stopNames.first(), "Partida")
                stopNames.drop(1).forEachIndexed { index, stop ->
                    TimelineStop(
                        name = stop,
                        label = if (index == stopNames.lastIndex) "Chegada" else "Transbordo",
                        isLast = index == stopNames.lastIndex
                    )
                }
            }
            Divider(modifier = Modifier.padding(vertical = 10.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Tempo de viagem:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${formatDuration(option.durationMinutes ?: 0)} (total)",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }

    @Composable
    private fun TimelineStop(
        name: String,
        label: String,
        isLast: Boolean = false
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                Text(
                    text = if (isLast) "●" else "○",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium
                )
                if (!isLast) {
                    Divider(
                        modifier = Modifier
                            .height(20.dp)
                            .padding(vertical = 2.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
                    )
                }
            }
            Column(modifier = Modifier.padding(start = 10.dp, bottom = 4.dp)) {
                Text(text = name, style = MaterialTheme.typography.labelLarge)
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

    private fun formatDuration(minutes: Int): String {
        if (minutes <= 0) return "—"
        val hours = minutes / 60
        val remainingMinutes = minutes % 60
        return when {
            hours == 0 -> "${remainingMinutes}min"
            remainingMinutes == 0 -> "${hours}h"
            else -> "${hours}h ${remainingMinutes}min"
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
