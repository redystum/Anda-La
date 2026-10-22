package com.redystum.andala.ui.screens.routeResultScreen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
    onConfirmRoute: () -> Unit
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

        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            MapComponent(screen = 1, pousadas, highlightPousadas, selectedPousadas) { pousada ->
                viewModel.toggleSelection(pousada)
            }
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
