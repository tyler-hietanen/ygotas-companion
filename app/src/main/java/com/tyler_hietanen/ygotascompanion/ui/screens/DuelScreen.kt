/*******************************************************************************************************************************************
 *           Source:    DuelScreen.kt
 *      Description:    Contains content for the Duel Screen.
 ******************************************************************************************************************************************/
package com.tyler_hietanen.ygotascompanion.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.tyler_hietanen.ygotascompanion.presentation.viewmodels.DuelViewModel

object DuelScreen
{
    /***************************************************************************************************************************************
     *      Composable Methods
     **************************************************************************************************************************************/
    //region Composable Methods

    /***************************************************************************************************************************************
     *           Method:    DrawScreen
     *       Parameters:    navController
     *                          - Nav host controller.
     *          Returns:    None.
     *      Description:    Composable function manages drawing the Welcome screen.
     **************************************************************************************************************************************/
    @Composable
    fun DrawScreen(navController: NavHostController, duelViewModel: DuelViewModel)
    {
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center)
        {
            Text("Duel Screen - Under Development")
        }
    }

    //endregion
}