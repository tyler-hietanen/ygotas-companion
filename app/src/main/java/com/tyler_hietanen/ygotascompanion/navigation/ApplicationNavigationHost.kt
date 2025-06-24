/*******************************************************************************************************************************************
 *           Source:    ApplicationNavigationHost.kt
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

object ApplicationNavigationHost
{
    /***************************************************************************************************************************************
     *      Methods
     **************************************************************************************************************************************/
    //region Methods

    /***************************************************************************************************************************************
     *           Method:    navigateTo
     *       Parameters:    controller
     *                          - Navigation Host Controller.
     *                      destination
     *                          - New destination.
     *          Returns:    None.
     *      Description:    Organized navigation to a specific destination. Ensures that ViewModel(s) are aware of change.
     *             Note:    This should only be called from a View component, ideally, the Main Activity.
     **************************************************************************************************************************************/
    fun navigateTo(controller: NavHostController, destination: Destination)
    {
        // Let ViewModel(s) know of navigation change.
        // TODO.

        // Actually navigate.
        controller.navigate(destination.routeID)
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
     *          Returns:    None.
     *      Description:    Composable function manages drawing the MainActivity screen..
     **************************************************************************************************************************************/
    @Composable
    fun SourceNavigationHost(controller: NavHostController, modifier: Modifier = Modifier)
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
                            WelcomeScreenComposable(navController = controller)
                        }
                        Destination.QUOTES ->
                        {
                            QuotesScreenComposable(navController = controller)
                        }
                        Destination.DUEL ->
                        {
                            DuelScreenComposable(navController = controller)
                        }
                        Destination.HOUSERULES ->
                        {
                            HouseRulesScreenComposable(navController = controller)
                        }
                        Destination.SETTINGS ->
                        {
                            SettingsScreenComposable(navController = controller)
                        }
                    }
                }
            }
        }
    }

    //endregion
}