/*******************************************************************************************************************************************
 *           Source:    DuelScreen.kt
 *      Description:    Contains content for the House Rules Screen.
 ******************************************************************************************************************************************/
package com.tyler_hietanen.yugioh_companion.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tyler_hietanen.yugioh_companion.R
import com.tyler_hietanen.yugioh_companion.presentation.ApplicationViewModel

// Note: Full credit to the Sad Kuriboh given to Maxiuchiha22 (https://www.deviantart.com/maxiuchiha22/art/Kuriboh-render-4-Monster-Strike-855820171)

object HouseRulesScreen
{
    /***************************************************************************************************************************************
     *      (Public) Composable Methods
     **************************************************************************************************************************************/
    //region (Public) Composable Methods

    /***************************************************************************************************************************************
     *           Method:    DrawScreen
     *       Parameters:    applicationViewModel
     *          Returns:    None.
     *      Description:    Composable function manages drawing the House Rules screen.
     **************************************************************************************************************************************/
    @Composable
    fun DrawScreen(applicationViewModel: ApplicationViewModel)
    {
        // Grab the values they care about from the ViewModel.
        val houseRulesContent by applicationViewModel.houseRulesViewModel.houseRulesContent

        // Depending upon the value of the content, determines what is shown.
        if (houseRulesContent == null)
        {
            EmptyHouseRulesScreen()
        }
        else
        {
            // TODO.
        }
    }

    //endregion

    /***************************************************************************************************************************************
     *      (Private) Composable Methods
     **************************************************************************************************************************************/
    //region (Private) Composable Methods

    /***************************************************************************************************************************************
     *           Method:    EmptyHouseRulesScreen
     *       Parameters:    None.
     *          Returns:    None.
     *      Description:    Draws an empty screen (if no house rules are loaded).
     **************************************************************************************************************************************/
    @Composable
    private fun EmptyHouseRulesScreen()
    {
        Column (
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Image(
                painter = painterResource(R.drawable.sad_kuriboh),
                contentDescription = "",
                modifier = Modifier.size(240.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "No House Rules have been loaded. That makes Kuriboh sad...",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "You don't want to make Kuriboh sad do you? Why don't you import house rules from the Settings screen?",
                textAlign = TextAlign.Center
            )
        }
    }

    //endregion
}