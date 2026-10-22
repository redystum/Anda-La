package com.redystum.andala.ui.screens.exploreScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.redystum.andala.data.Pousada
import com.redystum.andala.ui.screens.routeResultScreen.components.ResultMapScreen
import com.redystum.andala.ui.theme.AndaLaTheme
import com.redystum.andala.ui.viewmodel.TripViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteResultScreen(
    innerPadding: PaddingValues,
    viewModel: TripViewModel,
    onBack: () -> Unit,
    onConfirmRoute: () -> Unit
) {

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded
        )
    )

    val pousadas = viewModel.selectedPousadas
    val highlightPousada = remember { mutableStateListOf<Pousada>() }


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ResultMapScreen(
            viewModel = viewModel,
            pousadas = pousadas,
            highlightPousadas = highlightPousada,
            scaffoldState = scaffoldState,
            onConfirmRoute = onConfirmRoute
        )

    }
}


@Preview(showBackground = true)
@Composable
fun RouteResultPreview() {
    val tripViewModel: TripViewModel = viewModel()
    val innerPadding = PaddingValues(16.dp)

    AndaLaTheme {
        RouteResultScreen(
            innerPadding,
            tripViewModel,
            onBack = {},
        ) {}
    }
}