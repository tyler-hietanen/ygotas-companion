/*******************************************************************************************************************************************
 *           Source:    ApplicationNavigationHost.kt
 *      Description:    Guides the navigation used by the application. Does not determine rules, just lists the different destinations.
 ******************************************************************************************************************************************/
package com.tyler_hietanen.yugioh_companion.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tyler_hietanen.yugioh_companion.presentation.ApplicationViewModel
import com.tyler_hietanen.yugioh_companion.ui.screens.DuelScreen
import com.tyler_hietanen.yugioh_companion.ui.screens.HouseRulesScreen
import com.tyler_hietanen.yugioh_companion.ui.screens.QuotesScreen
import com.tyler_hietanen.yugioh_companion.ui.screens.SettingsScreen
import com.tyler_hietanen.yugioh_companion.ui.screens.WelcomeScreen

object ApplicationNavigationHost
{
    /***************************************************************************************************************************************
     *      Methods
     **************************************************************************************************************************************/
    //region Methods

    /***************************************************************************************************************************************
     *           Method:    navigateToSingleNewScreen
     *       Parameters:    controller
     *                          - Navigation Host Controller.
     *                      destination
     *                          - New destination.
     *                      applicationViewModel
     *                          - View model for application.
     *          Returns:    None.
     *      Description:    Organized navigation to a specific destination. Ensures that ViewModel(s) are aware of change.
     *             Note:    This should only be called from a View component, ideally, the Main Activity.
     **************************************************************************************************************************************/
    fun navigateToSingleNewScreen(controller: NavHostController, destination: Destination, applicationViewModel: ApplicationViewModel)
    {
        // Only allows if navigation is allowed.
        val doEnableNavigation by applicationViewModel.doEnableNavigation
        if (doEnableNavigation)
        {
            // Let ViewModel(s) know of navigation change.
            applicationViewModel.setCurrentDestination(destination)

            // Actually navigate.
            controller.navigate(destination.routeID) {
                // If destination is not the Welcome destination (home), then make sure it's popped.
                if (destination != Destination.WELCOME)
                {
                    controller.popBackStack(Destination.WELCOME.routeID, true)
                }

                // Pop all other destinations up to the current one.
                popUpTo(destination.routeID) {
                    inclusive = true
                }

                // Ensures this new destination is the only one on the stack.
                launchSingleTop = true
                restoreState = true
            }
        }
        // Otherwise, ignored.
    }

    //endregion

    /***************************************************************************************************************************************
     *      Composable Methods
     **************************************************************************************************************************************/
    //region Composable Methods

    /***************************************************************************************************************************************
     *           Method:    SourceNavigationHost
     *       Parameters:    controller
     *                          - Navigation Host Controller.
     *                      modifier
     *                          - Modifier.
     *                      applicationViewModel
     *                          - View model for application.
     *          Returns:    None.
     *      Description:    Composable function manages drawing the MainActivity screen..
     **************************************************************************************************************************************/
    @Composable
    fun SourceNavigationHost(controller: NavHostController, modifier: Modifier = Modifier, applicationViewModel: ApplicationViewModel)
    {
        NavHost(
            modifier = modifier,
            navController = controller,
            startDestination = Destination.WELCOME.routeID
        ) {
            // List of all possible destinations offered by the application.
            Destination.entries.forEach { destination ->
                composable(destination.routeID) {
                    when (destination) {
                        Destination.WELCOME ->
                        {
                            WelcomeScreen.DrawScreen(navController = controller, applicationViewModel = applicationViewModel)
                        }
                        Destination.QUOTES ->
                        {
                            QuotesScreen.DrawScreen(applicationViewModel)
                        }
                        Destination.DUEL ->
                        {
                            DuelScreen.DrawScreen(applicationViewModel)
                        }
                        Destination.HOUSERULES ->
                        {
                            HouseRulesScreen.DrawScreen(applicationViewModel)
                        }
                        Destination.SETTINGS ->
                        {
                            SettingsScreen.DrawScreen(applicationViewModel)
                        }
                    }
                }
            }
        }
    }

    //endregion
}