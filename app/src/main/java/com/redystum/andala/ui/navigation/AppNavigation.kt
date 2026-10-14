package com.redystum.andala.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.redystum.andala.ui.Screen
import com.redystum.andala.ui.screens.exploreScreen.ExploreScreen
import com.redystum.andala.ui.screens.PlanTripScreen
import com.redystum.andala.ui.screens.RouteResultScreen
import com.redystum.andala.ui.viewmodel.TripViewModel

@Composable
fun AppNavigation(innerPadding: PaddingValues) {
    val tripViewModel: TripViewModel = viewModel()
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Explore
    ) {
        // Ecra 1
        composable<Screen.Explore> {
            ExploreScreen(
                innerPadding = innerPadding,
                viewModel = tripViewModel,
                onNavigateToPlan = {
                    navController.navigate(Screen.PlanTrip)
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
                viewModel = tripViewModel,
                onBack = { navController.popBackStack() },
                onConfirmRoute = {
                    // save
                }
            )
        }
    }
}