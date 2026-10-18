package com.redystum.andala.ui.screens.planTripScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.redystum.andala.data.Pousadas
import com.redystum.andala.ui.components.NavBar
import com.redystum.andala.ui.viewmodel.TripViewModel
import kotlin.math.roundToInt
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun PlanTripScreen(
    viewModel: TripViewModel,
    onBack: () -> Boolean,
    onNavigateToRoute: () -> Unit,
    innerPadding: PaddingValues
) {
    LaunchedEffect(Unit) {
        viewModel.convertPousadaToNights()
    }

    val pousadaNights = viewModel.pousadaNights

    var station by remember { mutableStateOf("") }
    val cardCenters = remember { mutableMapOf<Int, Float>() }
    var draggedId by remember { mutableStateOf<Int?>(null) }
    var dragOffset by remember { mutableFloatStateOf(0f) }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                NavBar("Planear Viagem", onBackNavigation = onBack)
            }
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.background,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Button(
                        onClick = { onNavigateToRoute() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(26.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = "Ver rotas",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Estação de partida e chegada",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )

                OutlinedTextField(
                    value = station,
                    onValueChange = { station = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                            alpha = 0.3f
                        )
                    ),
                    singleLine = true
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Definir ordem de visita",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Text(
                    text = "Total de noites: ${viewModel.getSumNights()}/${viewModel.maxNights}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.primary
                    )
                )

                pousadaNights.forEachIndexed { index, stop ->
                    key(stop.pousada.id) {
                        val stopId = stop.pousada.id
                        val isDragged = draggedId == stopId

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .onGloballyPositioned { coordinates ->
                                    cardCenters[stopId] = coordinates.boundsInParent().center.y
                                }
                                .zIndex(if (isDragged) 1f else 0f)
                                .graphicsLayer {
                                    translationY = if (isDragged) dragOffset else 0f
                                    shadowElevation = if (isDragged) 12f else 0f
                                },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    if (pousadaNights.size > 1) {
                                        Icon(
                                            imageVector = Icons.Default.DragIndicator,
                                            contentDescription = "Reordenar",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                                alpha = 0.6f
                                            ),
                                            modifier = Modifier
                                                .size(32.dp)
                                                .pointerInput(stopId) {
                                                    detectDragGestures(
                                                        onDragStart = {
                                                            draggedId = stopId
                                                            dragOffset = 0f
                                                        },
                                                        onDragEnd = {
                                                            draggedId = null
                                                            dragOffset = 0f
                                                        },
                                                        onDragCancel = {
                                                            draggedId = null
                                                            dragOffset = 0f
                                                        },
                                                        onDrag = { change, dragAmount ->
                                                            change.consume()
                                                            dragOffset += dragAmount.y

                                                            val list = viewModel.pousadaNights
                                                            val currentIndex =
                                                                list.indexOfFirst { it.pousada.id == stopId }
                                                            if (currentIndex == -1) return@detectDragGestures

                                                            val currentCenter =
                                                                cardCenters[stopId]
                                                                    ?: return@detectDragGestures
                                                            val draggedCenter =
                                                                currentCenter + dragOffset

                                                            val targetIndex = when {
                                                                dragOffset > 0 && currentIndex < list.lastIndex -> {
                                                                    val next = currentIndex + 1
                                                                    val nextCenter =
                                                                        cardCenters[list[next].pousada.id]
                                                                    if (nextCenter != null && draggedCenter > nextCenter) next else null
                                                                }

                                                                dragOffset < 0 && currentIndex > 0 -> {
                                                                    val prev = currentIndex - 1
                                                                    val prevCenter =
                                                                        cardCenters[list[prev].pousada.id]
                                                                    if (prevCenter != null && draggedCenter < prevCenter) prev else null
                                                                }

                                                                else -> null
                                                            }

                                                            if (targetIndex != null) {
                                                                val targetId =
                                                                    list[targetIndex].pousada.id
                                                                val targetCenter =
                                                                    cardCenters[targetId]
                                                                        ?: return@detectDragGestures

                                                                dragOffset -= targetCenter - currentCenter

                                                                cardCenters[stopId] = targetCenter
                                                                cardCenters[targetId] =
                                                                    currentCenter

                                                                viewModel.reorderPousada(
                                                                    currentIndex,
                                                                    targetIndex
                                                                )
                                                            }
                                                        }
                                                    )
                                                }
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "${index + 1}º",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = stop.pousada.name,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Medium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Surface(
                                        onClick = {
                                            if (stop.nights > 1) {
                                                viewModel.setPousadaNights(
                                                    stop.pousada,
                                                    stop.nights - 1
                                                )
                                            }
                                        },
                                        shape = CircleShape,
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = Icons.Default.Remove,
                                                contentDescription = "Menos",
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }

                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.width(36.dp)
                                    ) {
                                        Text(
                                            text = "${stop.nights}",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        )
                                        Text(
                                            text = if (stop.nights == 1) "noite" else "noites",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontSize = 10.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        )
                                    }

                                    Surface(
                                        onClick = {
                                            viewModel.setPousadaNights(
                                                stop.pousada,
                                                stop.nights + 1
                                            )
                                        },
                                        shape = CircleShape,
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = Icons.Default.Add,
                                                contentDescription = "Mais",
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlanTripScreenPreview() {
    val tripViewModel: TripViewModel = viewModel()
    val pousadas = Pousadas.listaPousadas.subList(0, 3)
    tripViewModel.toggleSelection(pousadas[0])
    tripViewModel.toggleSelection(pousadas[1])
    tripViewModel.toggleSelection(pousadas[2])

    PlanTripScreen(
        viewModel = tripViewModel,
        onBack = { true },
        onNavigateToRoute = {},
        innerPadding = PaddingValues(16.dp)
    )
}