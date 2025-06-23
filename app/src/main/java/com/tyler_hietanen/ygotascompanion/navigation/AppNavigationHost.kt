/*******************************************************************************************************************************************
 *           Source:    AppNavigationHost.kt
 *      Description:    Guides the navigation used by the application. Does not determine rules, just lists the different destinations.
 ******************************************************************************************************************************************/
package com.tyler_hietanen.ygotascompanion.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tyler_hietanen.ygotascompanion.ui.screens.DuelScreenComposable
import com.tyler_hietanen.ygotascompanion.ui.screens.HouseRulesScreenComposable
import com.tyler_hietanen.ygotascompanion.ui.screens.QuotesScreenComposable
import com.tyler_hietanen.ygotascompanion.ui.screens.SettingsScreenComposable
import com.tyler_hietanen.ygotascompanion.ui.screens.WelcomeScreenComposable

@Composable
fun AppNavigationHost(controller: NavHostController, modifier: Modifier = Modifier)
{
    NavHost(
        navController = controller,
        startDestination = Destination.WELCOME.routeID,
        modifier = modifier
    ) {
        // List of all possible destinations offered by the application.
        Destination.entries.forEach { destination ->
            composable(destination.routeID) {
                when (destination) {
                    Destination.WELCOME -> WelcomeScreenComposable()
                    Destination.QUOTES -> QuotesScreenComposable()
                    Destination.DUEL -> DuelScreenComposable()
                    Destination.HOUSERULES -> HouseRulesScreenComposable()
                    Destination.SETTINGS -> SettingsScreenComposable()
                }
            }
        }
    }
}