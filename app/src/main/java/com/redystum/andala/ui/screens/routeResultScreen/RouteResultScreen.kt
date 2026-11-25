package com.redystum.andala.ui.screens.exploreScreen

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.redystum.andala.data.Pousada
import com.redystum.andala.ui.components.MapBackButton
import com.redystum.andala.ui.screens.routeResultScreen.components.ResultMapScreen
import com.redystum.andala.ui.viewmodel.SavedTripsViewModel
import com.redystum.andala.ui.viewmodel.TripViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteResultScreen(
    innerPadding: PaddingValues,
    viewModel: TripViewModel,
    savedTripsViewModel: SavedTripsViewModel,
    onBack: () -> Unit,
    onTripSaved: (Long) -> Unit
) {

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded
        )
    )

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isSaving by remember { mutableStateOf(false) }

    val pousadas = viewModel.currentPousadas()
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
            isConfirmingRoute = isSaving,
            onConfirmRoute = {
                if (!isSaving) {
                    val input = viewModel.currentTripInput()
                    if (input == null) {
                        Toast.makeText(
                            context,
                            "Ainda não há uma viagem para guardar.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        coroutineScope.launch {
                            isSaving = true
                            val tripId = savedTripsViewModel.saveTrip(input)
                            isSaving = false
                            if (tripId != null) {
                                Toast.makeText(
                                    context,
                                    "Viagem guardada.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                onTripSaved(tripId)
                            } else {
                                Toast.makeText(
                                    context,
                                    "Não foi possível guardar a viagem.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            },
            routeOptions = viewModel.routeOptions,
            selectedRouteByLeg = viewModel.selectedRouteByLeg,
            onRouteSelected = viewModel::selectRoute,
            isLoadingRoutes = viewModel.isLoadingRoutes
        )

        AnimatedVisibility(
            visible = scaffoldState.bottomSheetState.currentValue != SheetValue.Expanded,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(8.dp)
        ) {
            MapBackButton(onClick = onBack)
        }

    }
}
