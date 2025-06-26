/*******************************************************************************************************************************************
 *           Source:    DuelScreen.kt
 *      Description:    Contains content for the Duel Screen.
 ******************************************************************************************************************************************/
package com.tyler_hietanen.ygotascompanion.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.tyler_hietanen.ygotascompanion.presentation.viewmodels.DuelViewModel
import com.tyler_hietanen.ygotascompanion.ui.theme.Typography

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
        // Duelist(s).
        val duelist1 by duelViewModel.duelist1
        val duelist2 by duelViewModel.duelist2

        // Actually draw.
        Column {
            Row (modifier = Modifier.fillMaxWidth()
            ){
                Column (modifier = Modifier.weight(1f)){
                    PlayerTitle(duelist1.name, modifier = Modifier.fillMaxWidth())
                    LifePoints(duelist1.lifePoints, modifier = Modifier.fillMaxWidth())
                }
                Column (modifier = Modifier.weight(1f)){
                    PlayerTitle(duelist2.name, modifier = Modifier.fillMaxWidth())
                    LifePoints(duelist2.lifePoints, modifier = Modifier.fillMaxWidth())
                }
            }
            HorizontalDivider(modifier = Modifier.padding(8.dp))
            Button(modifier = Modifier
                .fillMaxWidth()
                .heightIn(48.dp)
                .padding(4.dp, 0.dp),
                onClick = { duelViewModel.resetDuel()}, ) {
                Text(text = "Reset Duel.",
                    style = Typography.titleMedium)
            }
        }
    }

    /***************************************************************************************************************************************
     *           Method:    PlayerTitle
     *       Parameters:    name
     *                          - Player name.
     *                      modifier
     *          Returns:    None.
     *      Description:    Draws a player title.
     **************************************************************************************************************************************/
    @Composable
    fun PlayerTitle(name: String, modifier: Modifier)
    {
        Card(
            modifier = Modifier
                .clickable { /*TODO*/ }
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = name,
                modifier.padding(8.dp),
                textAlign = TextAlign.Center,
                style = Typography.titleLarge,
            )
        }
    }

    /***************************************************************************************************************************************
     *           Method:    LifePoints
     *       Parameters:    lifePoints
     *                          - Player's life points.
     *                      modifier
     *          Returns:    None.
     *      Description:    Draws a player's life points.
     **************************************************************************************************************************************/
    @Composable
    fun LifePoints(lifePoints: Int, modifier: Modifier)
    {
        Text(
            text = lifePoints.toString(),
            modifier.padding(8.dp),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            style = Typography.titleLarge,
            fontSize = 40.sp
        )
    }

    //endregion
}