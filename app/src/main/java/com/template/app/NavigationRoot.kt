package com.template.app

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.template.auth.presentation.login.LoginScreenRoot

@Composable
fun NavigationRoot(
    isLoggedIn: Boolean
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Routes.MainGraph else Routes.AuthGraph
    ) {
        // Auth Graph
        navigation<Routes.AuthGraph>(startDestination = Routes.Login) {
            composable<Routes.Login> {
                LoginScreenRoot(
                    onLoginSuccess = {
                        navController.navigate(Routes.MainGraph) {
                            popUpTo(Routes.AuthGraph) { inclusive = true }
                        }
                    }
                )
            }
        }

        // Main Graph (with bottom nav)
        composable<Routes.MainGraph> {
            MainScreen(
                onLogout = {
                    navController.navigate(Routes.AuthGraph) {
                        popUpTo(Routes.MainGraph) { inclusive = true }
                    }
                }
            )
        }
    }
}
