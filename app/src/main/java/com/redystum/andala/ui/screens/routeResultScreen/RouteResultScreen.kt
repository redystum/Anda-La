package com.redystum.andala.ui.screens.exploreScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.redystum.andala.data.Pousada
import com.redystum.andala.ui.screens.routeResultScreen.components.ResultMapScreen
import com.redystum.andala.ui.viewmodel.TripViewModel

@RequiresApi(Build.VERSION_CODES.O)
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

    LaunchedEffect(pousadas, viewModel.selectedLocationCoordinates) {
        viewModel.loadRoutes(
            origin = viewModel.selectedLocationCoordinates,
            pousadas = pousadas
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ResultMapScreen(
            viewModel = viewModel,
            pousadas = pousadas,
            highlightPousadas = highlightPousada,
            scaffoldState = scaffoldState,
            onConfirmRoute = onConfirmRoute,
            routeOptions = viewModel.routeOptions,
            selectedRouteByLeg = viewModel.selectedRouteByLeg,
            onRouteSelected = viewModel::selectRoute,
            isLoadingRoutes = viewModel.isLoadingRoutes
        )
    }
}
