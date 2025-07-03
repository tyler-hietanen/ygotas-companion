/*******************************************************************************************************************************************
 *           Source:    MainActivity.kt
 *      Description:    Jetpack compose main activity. Acts as the sole main activity for the application.
 ******************************************************************************************************************************************/
package com.tyler_hietanen.yugioh_companion

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tyler_hietanen.yugioh_companion.presentation.ApplicationViewModel
import com.tyler_hietanen.yugioh_companion.ui.layout.CompanionBottomNavigationBar
import com.tyler_hietanen.yugioh_companion.ui.theme.CompanionMaterialTheme
import androidx.activity.viewModels
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.tyler_hietanen.yugioh_companion.business.settings.AppSettingsRepository
import com.tyler_hietanen.yugioh_companion.navigation.ApplicationNavigationHost
import com.tyler_hietanen.yugioh_companion.navigation.Destination
import com.tyler_hietanen.yugioh_companion.presentation.viewmodels.DuelViewModel
import com.tyler_hietanen.yugioh_companion.presentation.viewmodels.HouseRulesViewModel
import com.tyler_hietanen.yugioh_companion.presentation.viewmodels.QuotesViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

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

        // Whether this was due to a new launch (null) or just a recreation (rotation, config change).
        val isNewLaunch = (savedInstanceState == null)

        // Note: Loads settings (Should be first, or issues can occur).
        val settingsRepository = AppSettingsRepository(application.applicationContext)

        // Note: It is incredibly vital that this call happens before anything else and that any copied references to view models are copied
        // into the application view model. This allows nested compose functions access to view models.
        // Note: Do not place this in the setContent section, as that causes constant calls to the initialize functions (resetting things).
        // Create view model(s).
        val duelViewModel: DuelViewModel by viewModels()
        val houseRulesViewModel: HouseRulesViewModel by viewModels()
        val quotesViewModel: QuotesViewModel by viewModels ()

        // Only call initialization functions for a new launch.
        if (isNewLaunch)
        {
            duelViewModel.initialize(_applicationViewModel, settingsRepository)
            houseRulesViewModel.initialize(_applicationViewModel, application.applicationContext)
            quotesViewModel.initialize(_applicationViewModel, application.applicationContext)
        }

        // Set reference(s).
        _applicationViewModel.setDuelistViewModelReference(duelViewModel)
        _applicationViewModel.setHouseRulesViewModelReference(houseRulesViewModel)
        _applicationViewModel.setQuotesViewModelReference(quotesViewModel)

        // Sets app content.
        setContent {
            // Source context.
            val context = LocalContext.current

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

            // Will show a toast message if the custom message is changed.
            LaunchedEffect(key1 = _applicationViewModel) {
                _applicationViewModel.customMessages.collectLatest { message ->
                    // Check for length of message. If length is large enough, use a longer message time.
                    val messageLength = if (message.length > 20)
                    {
                        Toast.LENGTH_LONG
                    }
                    else
                    {
                        Toast.LENGTH_SHORT
                    }

                    // Actually show.
                    Toast.makeText(context, message, messageLength).show()
                }
            }

            // Starts drawing app.
            CompanionMaterialTheme {
                MainActivityScreen(navController)
            }

            // Only auto-navigates to destination if it's a new launch.
            if (isNewLaunch)
            {
                LaunchedEffect(Unit) {
                    delay(500)
                    ApplicationNavigationHost.navigateToSingleNewScreen(navController, Destination.QUOTES, _applicationViewModel)
                }
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
        CompanionBottomNavigationBar.BottomNavBar(navController, _applicationViewModel)
    }

    //endregion
}