package com.redystum.andala.ui.screens.routeResultScreen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DirectionsBoat
import androidx.compose.material.icons.filled.DirectionsSubway
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
import com.redystum.andala.data.remote.StepDto
import com.redystum.andala.data.remote.TransitDetailsDto
import com.redystum.andala.data.remote.TransitLineDto
import com.redystum.andala.ui.screens.exploreScreen.components.MapComponent
import com.redystum.andala.ui.theme.AndaLaTheme
import com.redystum.andala.ui.theme.tripRouteColor
import com.redystum.andala.ui.viewmodel.TripViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultMapScreen(
    pousadas: List<Pousada>,
    highlightPousadas: MutableList<Pousada>,
    viewModel: TripViewModel,
    scaffoldState: BottomSheetScaffoldState,
    onConfirmRoute: () -> Unit,
    isConfirmingRoute: Boolean = false,
    routeOptions: List<RouteOption> = emptyList(),
    selectedRouteByLeg: Map<Int, Int> = emptyMap(),
    onRouteSelected: (legIndex: Int, optionIndex: Int) -> Unit = { _, _ -> },
    isLoadingRoutes: Boolean = false
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
        sheetPeekHeight = 600.dp,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = when {
                            routeOptions.isNotEmpty() -> "Escolha as suas rotas"
                            isLoadingRoutes -> "A procurar rotas..."
                            else -> "Sem rotas disponíveis"
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
                    text = when {
                        selectedOptions.isNotEmpty() ->
                            "Duração total estimada · ${selectedOptions.size} " +
                                if (selectedOptions.size == 1) "viagem" else "viagens"
                        isLoadingRoutes ->
                            "Vamos encontrar as melhores ligações para a sua viagem"
                        else ->
                            "Não encontrámos ligações para esta viagem"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp, bottom = 10.dp)
                )

                if (routesByLeg.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isLoadingRoutes) {
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
                        } else {
                            Text(
                                text = "Não foi possível encontrar rotas para esta viagem.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth(),
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
                                tripColor = tripRouteColor(legIndex),
                                onOptionSelected = { optionIndex ->
                                    onRouteSelected(legIndex, optionIndex)
                                }
                            )
                        }

                        item(
                            key = "confirm_route"
                        ){
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = onConfirmRoute,
                                enabled = selectedOptions.isNotEmpty() && !isConfirmingRoute,
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
                                    text = if (isConfirmingRoute) {
                                        "A guardar..."
                                    } else {
                                        "Confirmar Rota"
                                    },
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                if (isConfirmingRoute) {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .padding(start = 8.dp)
                                            .size(20.dp),
                                        strokeWidth = 2.dp,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .padding(start = 8.dp)
                                            .size(20.dp)
                                    )
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
                // One polyline per trip, so every selected leg is visible at once.
                routePaths = routesByLeg.map { (legIndex, options) ->
                    val selectedIndex = selectedRouteByLeg[legIndex] ?: 0
                    options.getOrNull(selectedIndex)?.path.orEmpty()
                },
                routeColors = routesByLeg.keys.map { legIndex ->
                    tripRouteColor(legIndex)
                }
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
    tripColor: Color,
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
                        color = tripColor.copy(alpha = 0.15f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${legIndex + 1}",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = tripColor
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
                    color = tripColor
                )
            }
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
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
                    tripColor = tripColor,
                    originName = originName,
                    destinationName = destinationName,
                    onClick = { onOptionSelected(optionIndex) },
                    modifier = Modifier.fillParentMaxWidth(0.88f)
                )
            }
        }
    }
}

@Composable
internal fun RouteOptionCard(
    option: RouteOption,
    isSelected: Boolean,
    tripColor: Color,
    originName: String,
    destinationName: String,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val steps = option.route.legs.flatMap { it.steps }
    val transitDetails = steps.mapNotNull { it.transitDetails }
    val vehicleIcon = vehicleIcon(transitDetails)
    val transportLabel = transportTypeLabel(transitDetails)
    val headerSummary = transitSummary(transitDetails)
    val journeyItems = buildJourneyItems(steps, originName, destinationName)
    val legTitle = "$originName → $destinationName"

    Card(
        onClick = { onClick?.invoke() },
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) {
                tripColor
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
                            color = tripColor,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = vehicleIcon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp)
                ) {
                    Text(
                        text = legTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (headerSummary.isNotBlank()) {
                        Text(
                            text = headerSummary,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f, fill = false)
                                .background(
                                    color = tripColor.copy(alpha = 0.14f),
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = transportLabel,
                                style = MaterialTheme.typography.labelSmall,
                                color = tripColor,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Duração: ${formatDuration(option.durationMinutes ?: 0)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }
                }
                if (onClick != null) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = if (isSelected) {
                            tripColor
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
            )

            JourneyTimeline(
                items = journeyItems,
                tripColor = tripColor
            )
        }
    }
}

private sealed interface JourneyItem {
    data class Stop(
        val time: String?,
        val name: String,
        val isStart: Boolean = false,
        val isEnd: Boolean = false
    ) : JourneyItem

    data class Walk(
        val durationSeconds: Int?,
        val distanceMeters: Int?
    ) : JourneyItem

    data class Transit(
        val details: TransitDetailsDto,
        val durationMinutes: Int?
    ) : JourneyItem
}

@Composable
private fun JourneyTimeline(
    items: List<JourneyItem>,
    tripColor: Color
) {
    items.forEachIndexed { index, item ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            TimelineRail(
                isLast = index == items.lastIndex,
                filled = item is JourneyItem.Stop && (item.isStart || item.isEnd),
                showDot = item is JourneyItem.Stop,
                color = tripColor,
                modifier = Modifier.fillMaxHeight()
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
                    .padding(bottom = if (index == items.lastIndex) 0.dp else 10.dp)
            ) {
                when (item) {
                    is JourneyItem.Stop -> JourneyStopRow(item)
                    is JourneyItem.Walk -> JourneyWalkRow(item)
                    is JourneyItem.Transit -> JourneyTransitRow(item, tripColor)
                }
            }
        }
    }
}

@Composable
private fun JourneyStopRow(stop: JourneyItem.Stop) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.width(50.dp)) {
            if (stop.time != null) {
                Text(
                    text = stop.time,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Text(
            text = stop.name,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = if (stop.isStart || stop.isEnd) {
                FontWeight.Bold
            } else {
                FontWeight.Medium
            },
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun JourneyWalkRow(item: JourneyItem.Walk) {
    val details = buildList {
        add("A pé")
        item.durationSeconds
            ?.let(::secondsToMinutes)
            ?.takeIf { it > 0 }
            ?.let { add(formatDuration(it)) }
        item.distanceMeters
            ?.takeIf { it > 0 }
            ?.let { add(formatDistance(it)) }
    }.joinToString(" · ")

    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.DirectionsWalk,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = details,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun JourneyTransitRow(
    item: JourneyItem.Transit,
    tripColor: Color
) {
    val details = item.details
    val line = details.transitLine
    val badgeText = line?.nameShort?.takeIf { it.isNotBlank() }
        ?: line?.name?.takeIf { it.isNotBlank() }
        ?: "Transporte"
    val badgeColor = parseTransitColor(line?.color) ?: tripColor
    val badgeTextColor = parseTransitColor(line?.textColor) ?: Color.White
    val operator = line?.agencies
        ?.mapNotNull { it.name?.takeIf { name -> name.isNotBlank() } }
        ?.distinct()
        ?.joinToString(" / ")
    val service = line
        ?.takeIf(::isRailLine)
        ?.let(::railServiceLabel)
    val summary = buildList {
        if (!operator.isNullOrBlank()) add("Empresa: $operator")
        if (!service.isNullOrBlank()) add("Serviço: $service")
    }.joinToString(" · ")
    val duration = buildList {
        item.durationMinutes
            ?.takeIf { it > 0 }
            ?.let { add(formatDuration(it)) }
        stopsLabel(details.stopCount)?.let { add(it) }
    }.joinToString(" ")

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .background(
                        color = badgeColor,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = badgeText,
                    style = MaterialTheme.typography.labelSmall,
                    color = badgeTextColor,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (!details.headsign.isNullOrBlank()) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = details.headsign,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        if (summary.isNotBlank()) {
            Text(
                text = summary,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
        if (duration.isNotBlank()) {
            Text(
                text = duration,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun buildJourneyItems(
    steps: List<StepDto>,
    originName: String,
    destinationName: String
): List<JourneyItem> {
    val items = mutableListOf<JourneyItem>()

    val firstTransitIndex = steps.indexOfFirst { it.transitDetails != null }
    val lastTransitIndex = steps.indexOfLast { it.transitDetails != null }

    val firstDeparture = steps.getOrNull(firstTransitIndex)
        ?.transitDetails
        ?.stopDetails
        ?.departureTime
        ?.let(::formatIsoTime)
    val leadingWalkMinutes = if (firstTransitIndex >= 0) {
        secondsToMinutes(
            steps.take(firstTransitIndex).sumOf { stepDurationSeconds(it) ?: 0 }
        )
    } else {
        0
    }
    val startTime = shiftTime(firstDeparture, -leadingWalkMinutes)

    val lastArrival = steps.getOrNull(lastTransitIndex)
        ?.transitDetails
        ?.stopDetails
        ?.arrivalTime
        ?.let(::formatIsoTime)
    val trailingWalkMinutes = if (lastTransitIndex >= 0) {
        secondsToMinutes(
            steps.drop(lastTransitIndex + 1).sumOf { stepDurationSeconds(it) ?: 0 }
        )
    } else {
        0
    }
    val endTime = shiftTime(lastArrival, trailingWalkMinutes)

    items.add(JourneyItem.Stop(startTime, originName, isStart = true))

    steps.forEach { step ->
        val transit = step.transitDetails
        if (transit == null) {
            items.add(
                JourneyItem.Walk(
                    durationSeconds = stepDurationSeconds(step),
                    distanceMeters = step.distanceMeters
                )
            )
        } else {
            val stopDetails = transit.stopDetails
            val departureStop = stopDetails?.departureStop?.name
            val departureTime = formatIsoTime(stopDetails?.departureTime)
                ?: transit.localizedValues?.departureTime?.time?.text
            if (!departureStop.isNullOrBlank()) {
                items.addStop(JourneyItem.Stop(departureTime, departureStop))
            }

            items.add(
                JourneyItem.Transit(
                    details = transit,
                    durationMinutes = stepDurationMinutes(step)
                )
            )

            val arrivalStop = stopDetails?.arrivalStop?.name
            val arrivalTime = formatIsoTime(stopDetails?.arrivalTime)
                ?: transit.localizedValues?.arrivalTime?.time?.text
            if (!arrivalStop.isNullOrBlank()) {
                items.addStop(JourneyItem.Stop(arrivalTime, arrivalStop))
            }
        }
    }

    val lastStopIndex = items.indexOfLast { it is JourneyItem.Stop }
    val lastStop = items.getOrNull(lastStopIndex) as? JourneyItem.Stop
    if (lastStop == null || lastStop.name != destinationName) {
        items.add(
            JourneyItem.Stop(
                time = endTime,
                name = destinationName,
                isEnd = true
            )
        )
    } else {
        items[lastStopIndex] = lastStop.copy(
            isEnd = true,
            time = if (trailingWalkMinutes > 0) {
                endTime ?: lastStop.time
            } else {
                lastStop.time ?: endTime
            }
        )
    }

    return items.mergeWalkSteps()
}

/**
 * Google returns one step per walking direction, which would render as a long
 * series of "A pé" rows. Collapse consecutive walking steps into a single row
 * with the combined duration and distance.
 */
private fun List<JourneyItem>.mergeWalkSteps(): List<JourneyItem> {
    val merged = mutableListOf<JourneyItem>()
    forEach { item ->
        val previous = merged.lastOrNull()
        if (item is JourneyItem.Walk && previous is JourneyItem.Walk) {
            merged[merged.lastIndex] = JourneyItem.Walk(
                durationSeconds = sumOrNull(previous.durationSeconds, item.durationSeconds),
                distanceMeters = sumOrNull(previous.distanceMeters, item.distanceMeters)
            )
        } else {
            merged.add(item)
        }
    }
    return merged
}

private fun sumOrNull(first: Int?, second: Int?): Int? =
    if (first == null && second == null) null else (first ?: 0) + (second ?: 0)

private fun MutableList<JourneyItem>.addStop(stop: JourneyItem.Stop) {
    val last = lastOrNull()
    if (last is JourneyItem.Stop && last.name == stop.name) {
        if (last.time == null && stop.time != null) {
            this[lastIndex] = last.copy(time = stop.time)
        }
        return
    }
    add(stop)
}

private fun stepDurationSeconds(step: StepDto): Int? =
    step.staticDuration
        ?.removeSuffix("s")
        ?.toIntOrNull()
        ?.takeIf { it > 0 }

private fun secondsToMinutes(seconds: Int): Int = (seconds + 30) / 60

private fun stepDurationMinutes(step: StepDto): Int? =
    stepDurationSeconds(step)?.let(::secondsToMinutes)

private fun formatIsoTime(value: String?): String? {
    if (value.isNullOrBlank()) return null
    val time = value.substringAfter('T', "").take(5)
    return time.takeIf { it.length == 5 && it[2] == ':' }
}

private fun shiftTime(time: String?, minutes: Int): String? {
    if (time == null) return null
    val hour = time.substringBefore(':').toIntOrNull() ?: return null
    val minute = time.substringAfter(':').take(2).toIntOrNull() ?: return null
    val total = (((hour * 60 + minute + minutes) % 1440) + 1440) % 1440
    return "%02d:%02d".format(total / 60, total % 60)
}

private fun formatDistance(meters: Int): String {
    if (meters < 1000) return "$meters m"
    val kilometers = meters / 1000.0
    val text = if (kilometers == kilometers.toInt().toDouble()) {
        kilometers.toInt().toString()
    } else {
        String.format(Locale.getDefault(), "%.1f", kilometers).replace('.', ',')
    }
    return "$text km"
}

private fun stopsLabel(stopCount: Int?): String? = when {
    stopCount == null -> null
    stopCount <= 0 -> "(sem paragens)"
    stopCount == 1 -> "(1 paragem)"
    else -> "($stopCount paragens)"
}

private fun transitSummary(transitDetails: List<TransitDetailsDto>): String {
    val lines = transitDetails.mapNotNull { it.transitLine }
    val operators = lines
        .flatMap { it.agencies }
        .mapNotNull { it.name?.takeIf { name -> name.isNotBlank() } }
        .distinct()
    val services = lines
        .filter(::isRailLine)
        .mapNotNull(::railServiceLabel)
        .distinct()

    return buildList {
        if (operators.isNotEmpty()) add(operators.joinToString(" / "))
        if (services.isNotEmpty()) add(services.joinToString(" / "))
    }.joinToString(" · ")
}

private fun parseTransitColor(value: String?): Color? =
    value?.let { hex ->
        runCatching { Color(android.graphics.Color.parseColor(hex)) }.getOrNull()
    }

@Composable
private fun TimelineRail(
    isLast: Boolean,
    filled: Boolean,
    color: Color,
    showDot: Boolean = true,
    modifier: Modifier = Modifier
) {
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
                            Modifier.background(color, CircleShape)
                        } else {
                            Modifier.border(2.dp, color, CircleShape)
                        }
                    )
            )
        }
        if (!isLast) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .width(2.dp)
                    .background(color.copy(alpha = 0.35f))
            )
        }
    }
}

internal fun routeEndpointNames(
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
        } -> Icons.Default.Train
        else -> Icons.AutoMirrored.Filled.DirectionsWalk
    }
}

private fun transportTypeLabel(transitDetails: List<TransitDetailsDto>): String {
    if (transitDetails.isEmpty()) return "A pé"

    val labels = transitDetails
        .mapNotNull { it.transitLine }
        .map { transportLabel(it) }
        .distinct()

    return labels.joinToString(" + ").ifBlank { "Transporte" }
}

private fun transportLabel(line: TransitLineDto): String {
    val type = line.vehicle?.type?.uppercase().orEmpty()
    val name = (line.name ?: line.nameShort).orEmpty().lowercase()

    return when {
        type.contains("RAIL") || type.contains("TRAIN") || name.contains("comboio") ->
            when {
                "regional" in name -> "Comboio regional"
                "intercidades" in name -> "Comboio intercidades"
                "alfa" in name -> "Comboio Alfa Pendular"
                "urban" in name -> "Comboio urbano"
                else -> "Comboio"
            }

        type.contains("SUBWAY") || type.contains("METRO") -> "Metro"
        type.contains("TRAM") || type.contains("LIGHT_RAIL") -> "Elétrico"
        type.contains("FERRY") || type.contains("BOAT") -> "Barco"
        type.contains("BUS") || type.contains("COACH") || type.contains("TROLLEY") ->
            "Autocarro"
        type.contains("WALK") -> "A pé"
        else -> line.vehicle?.name?.text ?: "Transporte"
    }
}

private fun isRailLine(line: TransitLineDto): Boolean {
    val type = line.vehicle?.type?.uppercase().orEmpty()
    if (
        type.contains("RAIL") ||
        type.contains("TRAIN") ||
        type.contains("SUBWAY") ||
        type.contains("METRO")
    ) {
        return true
    }

    val name = (line.name ?: line.nameShort).orEmpty().lowercase()
    return "comboio" in name
}

/**
 * Human readable rail service, e.g. "Intercidades (IC)" or "Regional (R)".
 */
private fun railServiceLabel(line: TransitLineDto): String? {
    val name = line.name.orEmpty()
    val short = line.nameShort.orEmpty()
    val text = "$name $short".lowercase()

    val service = when {
        "alfa" in text || short.equals("ap", ignoreCase = true) -> "Alfa Pendular"
        "intercidades" in text || short.equals("ic", ignoreCase = true) -> "Intercidades"
        "interregional" in text || short.equals("ir", ignoreCase = true) -> "InterRegional"
        "regional" in text || short.equals("r", ignoreCase = true) -> "Regional"
        "urbano" in text || short.equals("u", ignoreCase = true) -> "Urbano"
        else -> null
    }

    val abbreviation = when (service) {
        "Alfa Pendular" -> "AP"
        "Intercidades" -> "IC"
        "InterRegional" -> "IR"
        "Regional" -> "R"
        "Urbano" -> "U"
        else -> null
    }

    return when {
        service != null && abbreviation != null -> "$service ($abbreviation)"
        name.isNotBlank() -> name
        short.isNotBlank() -> short
        else -> null
    }
}

internal fun formatDuration(minutes: Int): String {
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
