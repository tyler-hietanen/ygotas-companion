/*******************************************************************************************************************************************
 *           Source:    MainActivity.kt
 *      Description:    Jetpack compose main activity. Acts as the sole main activity for the application.
 ******************************************************************************************************************************************/
package com.tyler_hietanen.ygotascompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.tyler_hietanen.ygotascompanion.ui.theme.CompanionMaterialTheme
import com.tyler_hietanen.ygotascompanion.navigation.AppNavigationHost
import com.tyler_hietanen.ygotascompanion.navigation.Destination

class MainActivity : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CompanionMaterialTheme {
                MainAppScreen()
            }
        }
    }
}

@Composable
@Preview
fun MainAppScreen()
{
    BottomNavigationBar()
}

@Composable
fun BottomNavigationBar(modifier: Modifier = Modifier)
{
    val navController = rememberNavController()
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
                            navController.navigate(item.routeID)
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
        AppNavigationHost(
            controller = navController,
            modifier = Modifier.padding(innerPadding)
            )
    }
}