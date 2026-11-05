package com.redystum.andala.ui.screens.routeResultScreen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DirectionsBoat
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsSubway
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.redystum.andala.data.Pousada
import com.redystum.andala.data.Pousadas
import com.redystum.andala.data.remote.RouteOption
import com.redystum.andala.data.remote.TransitDetailsDto
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
    val routesByLeg = routeOptions.groupBy { it.legIndex }.toSortedMap()
    val selectedOptions = routesByLeg.mapNotNull { (legIndex, options) ->
        options.getOrNull(selectedRouteByLeg[legIndex] ?: 0)
    }
    val totalMinutes = selectedOptions.sumOf { it.durationMinutes ?: 0 }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContainerColor = MaterialTheme.colorScheme.surface,
        sheetContentColor = MaterialTheme.colorScheme.onSurface,
        sheetShadowElevation = 8.dp,
        sheetTonalElevation = 0.dp,
        sheetShape = MaterialTheme.shapes.large,
        sheetPeekHeight = 500.dp,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (routeOptions.isEmpty()) {
                            "A procurar rotas..."
                        } else {
                            "Escolha as suas rotas"
                        },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    if (selectedOptions.isNotEmpty()) {
                        Text(
                            text = formatDuration(totalMinutes),
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Text(
                    text = if (selectedOptions.isEmpty()) {
                        "Vamos encontrar as melhores ligações para a sua viagem"
                    } else {
                        "Duração total estimada · ${selectedOptions.size} " +
                            if (selectedOptions.size == 1) "viagem" else "viagens"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp, bottom = 10.dp)
                )

                if (routesByLeg.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(32.dp),
                                strokeWidth = 3.dp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "A calcular as melhores opções...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 320.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(
                            items = routesByLeg.entries.toList(),
                            key = { it.key }
                        ) { (legIndex, options) ->
                            val (originName, destinationName) = routeEndpointNames(
                                legIndex = legIndex,
                                pousadas = pousadas,
                                startLocation = viewModel.selectedLocation
                            )

                            TripSection(
                                legIndex = legIndex,
                                totalLegs = routesByLeg.size,
                                originName = originName,
                                destinationName = destinationName,
                                options = options,
                                selectedOptionIndex = selectedRouteByLeg[legIndex] ?: 0,
                                onOptionSelected = { optionIndex ->
                                    onRouteSelected(legIndex, optionIndex)
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onConfirmRoute,
                    enabled = selectedOptions.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = "Confirmar Rota",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
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
                // One polyline per trip, so every selected leg is visible at once.
                routePaths = routesByLeg.map { (legIndex, options) ->
                    val selectedIndex = selectedRouteByLeg[legIndex] ?: 0
                    options.getOrNull(selectedIndex)?.path.orEmpty()
                }.filter { it.size > 1 }
            )
        }
    }
}

@Composable
private fun TripSection(
    legIndex: Int,
    totalLegs: Int,
    originName: String,
    destinationName: String,
    options: List<RouteOption>,
    selectedOptionIndex: Int,
    onOptionSelected: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${legIndex + 1}",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp)
            ) {
                Text(
                    text = "Viagem ${legIndex + 1} de $totalLegs",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$originName → $destinationName",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (options.size > 1) {
                Text(
                    text = "${options.size} opções",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(
                items = options,
                key = { _, option ->
                    "${option.legIndex}-${option.departureTime}-${option.route.hashCode()}"
                }
            ) { optionIndex, option ->
                RouteOptionCard(
                    option = option,
                    isSelected = selectedOptionIndex == optionIndex,
                    originName = originName,
                    destinationName = destinationName,
                    onClick = { onOptionSelected(optionIndex) },
                    modifier = Modifier.fillMaxWidth(0.88f)
                )
            }
        }
    }
}

@Composable
private fun RouteOptionCard(
    option: RouteOption,
    isSelected: Boolean,
    originName: String,
    destinationName: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var detailsExpanded by remember(option) { mutableStateOf(false) }

    val transitDetails = option.route.legs
        .flatMap { it.steps }
        .mapNotNull { it.transitDetails }
    val lineName = transitDetails
        .firstNotNullOfOrNull { it.transitLine?.name }
        ?: option.transitLines.firstOrNull()
        ?: "Ligação"
    val vehicleIcon = vehicleIcon(transitDetails)
    val stopNames = transitDetails
        .flatMap { details ->
            listOfNotNull(
                details.stopDetails?.departureStop?.name,
                details.stopDetails?.arrivalStop?.name
            )
        }
        .distinct()

    val timelineStops = if (stopNames.size >= 2) {
        stopNames.mapIndexed { index, name ->
            TimelineStopData(
                name = name,
                label = when (index) {
                    0 -> "Partida"
                    stopNames.lastIndex -> "Chegada"
                    else -> "Transbordo"
                }
            )
        }
    } else {
        listOf(
            TimelineStopData(originName, "Partida"),
            TimelineStopData(destinationName, "Chegada")
        )
    }

    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
            }
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 0.dp
        )
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = vehicleIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp)
                ) {
                    Text(
                        text = lineName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Duração: ${formatDuration(option.durationMinutes ?: 0)}  ·  " +
                            "Partida ${formatTime(option.departureTime)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    modifier = Modifier.size(24.dp)
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
            )

            TripTimeline(
                stops = timelineStops,
                detailsExpanded = detailsExpanded,
                onToggleDetails = { detailsExpanded = !detailsExpanded },
                detailsText = departureDetails(option, transitDetails)
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Tempo de viagem:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${formatDuration(option.durationMinutes ?: 0)} (total)",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun TripTimeline(
    stops: List<TimelineStopData>,
    detailsExpanded: Boolean,
    onToggleDetails: () -> Unit,
    detailsText: String
) {
    stops.forEachIndexed { index, stop ->
        TimelineStopRow(
            stop = stop,
            isLast = index == stops.lastIndex
        )

        if (index == 0) {
            TimelineDetailsRow(
                expanded = detailsExpanded,
                onToggle = onToggleDetails,
                detailsText = detailsText
            )
        }
    }
}

@Composable
private fun TimelineStopRow(
    stop: TimelineStopData,
    isLast: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        TimelineRail(
            isLast = isLast,
            filled = isLast,
            modifier = Modifier.fillMaxHeight()
        )
        Column(
            modifier = Modifier
                .padding(start = 12.dp)
                .padding(bottom = 8.dp)
        ) {
            Text(
                text = stop.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = stop.label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun TimelineDetailsRow(
    expanded: Boolean,
    onToggle: () -> Unit,
    detailsText: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        TimelineRail(
            isLast = false,
            filled = false,
            showDot = false,
            modifier = Modifier.fillMaxHeight()
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
                .padding(bottom = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .clickable(onClick = onToggle)
                    .padding(vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Detalhes da viagem",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(
                    imageVector = if (expanded) {
                        Icons.Default.KeyboardArrowUp
                    } else {
                        Icons.Default.KeyboardArrowDown
                    },
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
            }
            if (expanded) {
                Text(
                    text = detailsText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun TimelineRail(
    isLast: Boolean,
    filled: Boolean,
    showDot: Boolean = true,
    modifier: Modifier = Modifier
) {
    val primary = MaterialTheme.colorScheme.primary
    Column(
        modifier = modifier.width(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showDot) {
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .size(14.dp)
                    .then(
                        if (filled) {
                            Modifier.background(primary, CircleShape)
                        } else {
                            Modifier.border(2.dp, primary, CircleShape)
                        }
                    )
            )
        }
        if (!isLast) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .width(2.dp)
                    .background(primary.copy(alpha = 0.35f))
            )
        }
    }
}

private data class TimelineStopData(
    val name: String,
    val label: String
)

private fun routeEndpointNames(
    legIndex: Int,
    pousadas: List<Pousada>,
    startLocation: String?
): Pair<String, String> {
    val origin = when {
        legIndex == 0 -> startLocation ?: "Origem"
        legIndex - 1 in pousadas.indices -> pousadas[legIndex - 1].name
        else -> "Origem"
    }
    val destination = when {
        legIndex in pousadas.indices -> pousadas[legIndex].name
        else -> startLocation ?: "Destino"
    }
    return origin to destination
}

private fun vehicleIcon(transitDetails: List<TransitDetailsDto>): ImageVector {
    val vehicleTypes = transitDetails.mapNotNull { it.transitLine?.vehicle?.type?.uppercase() }
    return when {
        vehicleTypes.any {
            it.contains("RAIL") || it.contains("TRAIN")
        } -> Icons.Default.Train
        vehicleTypes.any { it.contains("SUBWAY") || it.contains("METRO") } ->
            Icons.Default.DirectionsSubway
        vehicleTypes.any { it.contains("FERRY") || it.contains("BOAT") } ->
            Icons.Default.DirectionsBoat
        vehicleTypes.any {
            it.contains("BUS") || it.contains("TROLLEY") || it.contains("COACH")
        } -> Icons.Default.DirectionsBus
        else -> Icons.Default.DirectionsWalk
    }
}

private fun departureDetails(
    option: RouteOption,
    transitDetails: List<TransitDetailsDto>
): String {
    val transfers = (transitDetails.size - 1).coerceAtLeast(0)
    val transferText = when (transfers) {
        0 -> "sem transbordos"
        1 -> "1 transbordo"
        else -> "$transfers transbordos"
    }
    return "Partida às ${formatTime(option.departureTime)} · $transferText"
}

private fun formatTime(dateTime: String): String =
    dateTime.substringAfter('T').take(5).ifBlank { "—" }

private fun formatDuration(minutes: Int): String {
    if (minutes <= 0) return "—"
    val hours = minutes / 60
    val remainingMinutes = minutes % 60
    return when {
        hours == 0 -> "${remainingMinutes}min"
        remainingMinutes == 0 -> "${hours}h"
        else -> "${hours}h${remainingMinutes.toString().padStart(2, '0')}"
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
