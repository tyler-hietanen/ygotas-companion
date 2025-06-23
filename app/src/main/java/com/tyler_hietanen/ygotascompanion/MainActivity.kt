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
import com.tyler_hietanen.ygotascompanion.ui.layout.CompanionBottomNavigationBar
import com.tyler_hietanen.ygotascompanion.ui.theme.CompanionMaterialTheme

class MainActivity : ComponentActivity()
{
    /***************************************************************************************************************************************
     *      Fields
     **************************************************************************************************************************************/
    //region Fields

    // TODO ViewModel instance.

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
     **************************************************************************************************************************************/
    @Composable
    @Preview
    fun MainActivityScreen()
    {
        CompanionBottomNavigationBar.ComposeBottomNavBar()
    }

    //endregion
}