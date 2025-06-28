/*******************************************************************************************************************************************
 *           Source:    CompanionBottomNavigationBar.kt
 *      Description:    Layout class for the bottom navigation bar. Contains UI binding behavior.
 ******************************************************************************************************************************************/
package com.tyler_hietanen.yugioh_companion.ui.layout

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.tyler_hietanen.yugioh_companion.navigation.ApplicationNavigationHost
import com.tyler_hietanen.yugioh_companion.navigation.Destination
import com.tyler_hietanen.yugioh_companion.presentation.ApplicationViewModel

object CompanionBottomNavigationBar
{
    /***************************************************************************************************************************************
     *      Composable Methods
     **************************************************************************************************************************************/
    //region Composable Methods

    /***************************************************************************************************************************************
     *           Method:    BottomNavBar
     *       Parameters:    None.
     *          Returns:    None.
     *      Description:    Composable function manages drawing a Bottom Navigation Bar.
     **************************************************************************************************************************************/
    @Composable
    fun BottomNavBar(controller: NavHostController, applicationViewModel: ApplicationViewModel)
    {
        // Gets current destination from VM.
        val currentDestination by applicationViewModel.currentDestination

        // Whether navigation is enabled.
        val doEnableNavigation by applicationViewModel.doEnableNavigation

        // Lists the items accessible by the bottom navigation bar.
        val navigationBarItems: List<Destination> = listOf(
            Destination.QUOTES,
            Destination.DUEL,
            Destination.HOUSERULES,
            Destination.SETTINGS
        )

        // Start composable drawing.
        Scaffold (
            bottomBar = {
                if (navigationBarItems.contains(currentDestination))
                {
                    NavigationBar {
                        navigationBarItems.forEach { item ->
                            val isSelected = (currentDestination == item)
                            NavigationBarItem(
                                selected = isSelected,
                                enabled = doEnableNavigation,
                                onClick = {
                                    ApplicationNavigationHost.navigateToSingleNewScreen(controller, item, applicationViewModel)
                                },
                                label = {
                                    Text(item.title)
                                },
                                icon = {
                                    val resourceID = if (isSelected) {
                                        item.selectedIcon
                                    }
                                    else {
                                        item.unselectedIcon
                                    }
                                    Icon(painter = painterResource(resourceID!!), contentDescription = item.title)
                                }
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            ApplicationNavigationHost.SourceNavigationHost(
                controller = controller,
                modifier = Modifier.padding(innerPadding),
                applicationViewModel = applicationViewModel
            )
        }
    }

    //endregion
}