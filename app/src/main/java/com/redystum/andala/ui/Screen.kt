package com.redystum.andala.ui

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Explore : Screen

    @Serializable
    data object PlanTrip : Screen

    @Serializable
    data object RouteResult : Screen
}