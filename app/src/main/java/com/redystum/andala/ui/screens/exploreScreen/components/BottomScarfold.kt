package com.redystum.andala.ui.screens.exploreScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRightAlt
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.redystum.andala.data.Pousada
import com.redystum.andala.data.Pousadas
import com.redystum.andala.ui.theme.AndaLaTheme
import com.redystum.andala.ui.viewmodel.TripViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PousadaMapScreen(
    pousadas: List<Pousada>,
    pousadasScaffold: List<Pousada>,
    highlightPousadas: MutableList<Pousada>,
    onNavigateToPlan: () -> Unit,
    viewModel: TripViewModel,
    scaffoldState: BottomSheetScaffoldState
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
                    .fillMaxHeight(0.85f)
                    .padding(20.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Pousadas de Juventude",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        text = "${selectedPousadas.size} de 3",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = false),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(pousadasScaffold, key = { it.id }) { pousada ->
                            PousadaCard(
                                pousada = pousada,
                                onSelectToggle = {
                                    viewModel.toggleSelection(pousada)
                                },
                                isSelected = selectedPousadas.contains(pousada),
                                onClickToggle = {
                                    highlightPousadas.removeAll(highlightPousadas)
                                    highlightPousadas.add(pousada)
                                }
                            )
                        }
                    }

                    Button(
                        onClick = { onNavigateToPlan() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Planear Viagem")
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowRightAlt,
                            contentDescription = "Arrow Forward",
                            modifier = Modifier.padding(start = 8.dp)
                        )
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
            MapComponent(screen = 0, pousadas, highlightPousadas, selectedPousadas)
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
    val b = mutableListOf<Pousada>()
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded
        )
    )

    AndaLaTheme {
        PousadaMapScreen(pousadas, a, b, {}, viewModel, scaffoldState)
    }
}
