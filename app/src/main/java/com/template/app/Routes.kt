package com.template.app

import kotlinx.serialization.Serializable

sealed interface Routes {
    // Auth graph
    @Serializable data object AuthGraph : Routes
    @Serializable data object Login : Routes

    // Main graph (bottom nav)
    @Serializable data object MainGraph : Routes
    @Serializable data object Home : Routes
    @Serializable data object Profile : Routes
}
