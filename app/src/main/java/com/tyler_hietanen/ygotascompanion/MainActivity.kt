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
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tyler_hietanen.ygotascompanion.presentation.MainActivityViewModel
import com.tyler_hietanen.ygotascompanion.ui.layout.CompanionBottomNavigationBar
import com.tyler_hietanen.ygotascompanion.ui.theme.CompanionMaterialTheme

class MainActivity : ComponentActivity()
{
    /***************************************************************************************************************************************
     *      Fields
     **************************************************************************************************************************************/
    //region Fields

    // MainActivity ViewModel instance. Initializes once it has been used.
    // Note: Still stays in reference for the lifetime of the application.
    private val mainActivityViewModel by lazy {ViewModelProvider(this)[MainActivityViewModel::class.java]}

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

        // Sets app content.
        setContent {
            CompanionMaterialTheme {
                MainActivityScreen()
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
    @Preview
    fun MainActivityScreen()
    {
        // Creates (and remembers) the NavHostController, used by other components.
        val navController: NavHostController = rememberNavController()

        // Actually draws the application view.
        CompanionBottomNavigationBar.ComposeBottomNavBar(navController)
    }

    //endregion
}