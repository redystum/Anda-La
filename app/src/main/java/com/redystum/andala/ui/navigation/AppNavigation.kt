package com.redystum.andala.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.redystum.andala.ui.Screen
import com.redystum.andala.ui.screens.exploreScreen.ExploreScreen
import com.redystum.andala.ui.screens.exploreScreen.RouteResultScreen
import com.redystum.andala.ui.screens.planTripScreen.PlanTripScreen
import com.redystum.andala.ui.screens.savedTripScreen.SavedTripScreen
import com.redystum.andala.ui.screens.savedTripsScreen.SavedTripsScreen
import com.redystum.andala.ui.viewmodel.SavedTripsViewModel
import com.redystum.andala.ui.viewmodel.TripViewModel

@Composable
fun AppNavigation(innerPadding: PaddingValues) {
    val tripViewModel: TripViewModel = viewModel()
    val savedTripsViewModel: SavedTripsViewModel = viewModel()
    val navController = rememberNavController()

    val savedTrips by savedTripsViewModel.savedTrips.collectAsState()

    // null while the database is still being read, then whether a saved trip
    // should be offered when the app opens.
    var launchDecision by rememberSaveable { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(savedTrips) {
        val trips = savedTrips ?: return@LaunchedEffect
        if (launchDecision == null) {
            launchDecision = trips.isNotEmpty()
        }
    }

    val hasSavedTrip = launchDecision
    if (hasSavedTrip == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    NavHost(
        navController = navController,
        startDestination = if (hasSavedTrip) Screen.SavedTrips else Screen.Explore
    ) {
        // Ecra 1
        composable<Screen.Explore> {
            ExploreScreen(
                innerPadding = innerPadding,
                viewModel = tripViewModel,
                onNavigateToPlan = {
                    navController.navigate(Screen.PlanTrip)
                },
                onBack = if (savedTrips?.isNotEmpty() == true) {
                    { navController.popBackStack() }
                } else {
                    null
                }
            )
        }

        // Ecra 2
        composable<Screen.PlanTrip> {
            PlanTripScreen(
                innerPadding = innerPadding,
                viewModel = tripViewModel,
                onBack = { navController.popBackStack() },
                onNavigateToRoute = {
                    navController.navigate(Screen.RouteResult)
                }
            )
        }

        // Ecra 3
        composable<Screen.RouteResult> {
            RouteResultScreen(
                innerPadding = innerPadding,
                viewModel = tripViewModel,
                savedTripsViewModel = savedTripsViewModel,
                onBack = { navController.popBackStack() },
                onTripSaved = { tripId ->
                    navController.navigate(Screen.SavedTripDetail(tripId)) {
                        popUpTo(Screen.Explore)
                    }
                }
            )
        }

        // Saved trips, shown on launch when a trip exists.
        composable<Screen.SavedTrips> {
            SavedTripsScreen(
                innerPadding = innerPadding,
                viewModel = savedTripsViewModel,
                onOpenTrip = { tripId ->
                    navController.navigate(Screen.SavedTripDetail(tripId))
                },
                onNewTrip = {
                    tripViewModel.resetTrip()
                    navController.navigate(Screen.Explore) {
                        popUpTo(Screen.SavedTrips)
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<Screen.SavedTripDetail> { backStackEntry ->
            val tripId = backStackEntry.toRoute<Screen.SavedTripDetail>().tripId
            SavedTripScreen(
                innerPadding = innerPadding,
                savedTripId = tripId,
                viewModel = savedTripsViewModel,
                onBack = { navController.popBackStack() },
                onNewTrip = {
                    tripViewModel.resetTrip()
                    navController.navigate(Screen.Explore) {
                        popUpTo(Screen.SavedTrips)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
