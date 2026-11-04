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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.redystum.andala.data.Pousada
import com.redystum.andala.data.remote.LatLngDto
import com.redystum.andala.data.remote.RouteOption
import com.redystum.andala.data.remote.RoutesService
import com.redystum.andala.ui.screens.routeResultScreen.components.ResultMapScreen
import com.redystum.andala.ui.theme.AndaLaTheme
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
    var routeOptions by remember { mutableStateOf<List<RouteOption>>(emptyList()) }
    var selectedRouteByLeg by remember { mutableStateOf<Map<Int, Int>>(emptyMap()) }

    LaunchedEffect(pousadas, viewModel.selectedLocationCoordinates) {
        val origin = viewModel.selectedLocationCoordinates ?: return@LaunchedEffect
        if (pousadas.isEmpty()) return@LaunchedEffect

        val stops = buildList {
            add(LatLngDto(origin.latitude, origin.longitude))
            pousadas.forEach { pousada ->
                add(LatLngDto(pousada.coordinates.first, pousada.coordinates.second))
            }
            add(LatLngDto(origin.latitude, origin.longitude))
        }

        routeOptions = RoutesService().getRoutesForTrip(stops)
        selectedRouteByLeg = routeOptions
            .groupBy { it.legIndex }
            .mapValues { (_, options) -> options.indices.first() }
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
            routeOptions = routeOptions,
            selectedRouteByLeg = selectedRouteByLeg,
            onRouteSelected = { legIndex, optionIndex ->
                selectedRouteByLeg = selectedRouteByLeg + (legIndex to optionIndex)
            }
        )

    }
}


//@Preview(showBackground = true)
//@Composable
//fun RouteResultPreview() {
//    val tripViewModel: TripViewModel = viewModel()
//    val innerPadding = PaddingValues(16.dp)
//
//    AndaLaTheme {
//        RouteResultScreen(
//            innerPadding,
//            tripViewModel,
//            onBack = {},
//        ) {}
//    }
//}