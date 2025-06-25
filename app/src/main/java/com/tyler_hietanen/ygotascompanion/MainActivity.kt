/*******************************************************************************************************************************************
 *           Source:    MainActivity.kt
 *      Description:    Jetpack compose main activity. Acts as the sole main activity for the application.
 ******************************************************************************************************************************************/
package com.tyler_hietanen.ygotascompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tyler_hietanen.ygotascompanion.presentation.ApplicationViewModel
import com.tyler_hietanen.ygotascompanion.ui.layout.CompanionBottomNavigationBar
import com.tyler_hietanen.ygotascompanion.ui.theme.CompanionMaterialTheme
import androidx.activity.viewModels
import androidx.compose.runtime.DisposableEffect
import androidx.navigation.NavController
import com.tyler_hietanen.ygotascompanion.navigation.Destination

class MainActivity : ComponentActivity()
{
    /***************************************************************************************************************************************
     *      Fields
     **************************************************************************************************************************************/
    //region Fields

    // MainActivity ViewModel instance.
    private val _applicationViewModel: ApplicationViewModel by viewModels()

    //endregion

    /***************************************************************************************************************************************
     *      Overridden Methods
     **************************************************************************************************************************************/
    //region Overridden Methods.

    /***************************************************************************************************************************************
     *           Method:    onCreate
     *       Parameters:    savedInstanceState
     *          Returns:    None.
     *      Description:    Overrides the on create method, called upon the creation of the MainActivity.
     **************************************************************************************************************************************/
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Calls into MainActivityViewModel initialization, only on the first creation of this application.
        if (savedInstanceState == null)
        {
            _applicationViewModel.applicationInitialization()
        }

        // Sets app content.
        setContent {
            // Grabs instance of nav controller.
            val navController: NavHostController = rememberNavController()

            // Sets up listening to ensure that ViewModel is kept in sync with nav controller.
            DisposableEffect(navController, _applicationViewModel) {
                val listener = NavController.OnDestinationChangedListener { _, navigatedDestination, _ ->
                    Destination.entries.find { it.routeID == navigatedDestination.route }?.let { destEnum ->
                        _applicationViewModel.setCurrentDestination(destEnum)
                    }
                }
                navController.addOnDestinationChangedListener(listener)
                onDispose {
                    navController.removeOnDestinationChangedListener(listener)
                }
            }

            // Starts drawing app.
            CompanionMaterialTheme {
                MainActivityScreen(navController)
            }
        }
    }

    //endregion

    /***************************************************************************************************************************************
     *      Composable Methods
     **************************************************************************************************************************************/
    //region Composable Methods

    /***************************************************************************************************************************************
     *           Method:    MainActivityScreen
     *       Parameters:    None.
     *          Returns:    None.
     *      Description:    Composable function manages drawing the MainActivity screen.
     *             Note:    This should be called before any other components that need navigation.
     **************************************************************************************************************************************/
    @Composable
    fun MainActivityScreen(navController: NavHostController)
    {
        // Actually draws the application view.
        CompanionBottomNavigationBar.Compose(navController, _applicationViewModel)
    }

    //endregion
}