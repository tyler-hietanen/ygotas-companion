/*******************************************************************************************************************************************
 *           Source:    CompanionBottomNavigationBar.kt
 *      Description:    Layout class for the bottom navigation bar. Contains UI binding behavior.
 ******************************************************************************************************************************************/
package com.tyler_hietanen.ygotascompanion.ui.layout

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.tyler_hietanen.ygotascompanion.navigation.ApplicationNavigationHost
import com.tyler_hietanen.ygotascompanion.navigation.Destination

object CompanionBottomNavigationBar
{
    /***************************************************************************************************************************************
     *      Composable Methods
     **************************************************************************************************************************************/
    //region Composable Methods

    /***************************************************************************************************************************************
     *           Method:    ComposeBottomNavBar
     *       Parameters:    None.
     *          Returns:    None.
     *      Description:    Composable function manages draws a Bottom Navigation Bar.
     **************************************************************************************************************************************/
    @Composable
    fun ComposeBottomNavBar(controller: NavHostController)
    {
        var selectedDestinationIndex by rememberSaveable {
            mutableIntStateOf(0) }

        // Lists the items accessible by the bottom navigation bar.
        val navigationBarItems: List<Destination> = listOf(
            Destination.QUOTES,
            Destination.DUEL,
            Destination.HOUSERULES
        )

        // Start composable drawing.
        Scaffold (
            bottomBar = {
                NavigationBar {
                    navigationBarItems.forEachIndexed{ index, item ->
                        NavigationBarItem(
                            selected = (selectedDestinationIndex == index),
                            onClick = {
                                selectedDestinationIndex = index
                                ApplicationNavigationHost.navigateTo(controller, item)
                            },
                            label = {
                                Text(item.title)
                            },
                            icon = {
                                val resourceID = if (selectedDestinationIndex == index)
                                {
                                    item.selectedIcon
                                }
                                else
                                {
                                    item.unselectedIcon
                                }
                                Icon(painter = painterResource(resourceID!!), contentDescription = item.title)
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            ApplicationNavigationHost.SourceNavigationHost(
                controller = controller,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }

    //endregion
}