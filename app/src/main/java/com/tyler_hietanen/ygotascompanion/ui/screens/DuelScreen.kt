/*******************************************************************************************************************************************
 *           Source:    DuelScreen.kt
 *      Description:    Contains content for the Duel Screen.
 ******************************************************************************************************************************************/
package com.tyler_hietanen.ygotascompanion.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tyler_hietanen.ygotascompanion.R
import com.tyler_hietanen.ygotascompanion.business.duel.PlayerSlot
import com.tyler_hietanen.ygotascompanion.presentation.viewmodels.DuelViewModel
import com.tyler_hietanen.ygotascompanion.ui.theme.Typography
import kotlinx.coroutines.flow.collectLatest

object DuelScreen
{
    /***************************************************************************************************************************************
     *      Composable Methods
     **************************************************************************************************************************************/
    //region Composable Methods

    /***************************************************************************************************************************************
     *           Method:    DrawScreen
     *       Parameters:    duelViewModel
     *                          - View model for the duel.
     *          Returns:    None.
     *      Description:    Composable function manages drawing the Duel screen.
     **************************************************************************************************************************************/
    @Composable
    fun DrawScreen(duelViewModel: DuelViewModel)
    {
        // Set up variables (and observation).
        val duelist1 by duelViewModel.duelist1
        val duelist2 by duelViewModel.duelist2
        val runningLifePoints by duelViewModel.runningLifePoints
        val isLocked by duelViewModel.isDuelEnabled
        val scrollState = rememberScrollState()
        val context = LocalContext.current

        // Will show a toast message if the custom message is changed.
        LaunchedEffect(key1 = duelViewModel) {
            duelViewModel.customMessages.collectLatest { message ->
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

        // Actually draw.
        Column (modifier = Modifier.verticalScroll(scrollState)) {
            // Reset button (always enabled).
            TextButton(
                modifier = Modifier
                    .fillMaxWidth(1f),
                buttonText = "Reset Duel",
                isEnabled = true,
                onClick = {
                    duelViewModel.resetDuel()
                }
            )

            // Player section.
            PlayerSection(
                playerOneName = duelist1.name,
                playerOneLifePoints = duelist1.lifePoints,
                playerTwoName = duelist2.name,
                playerTwoLifePoints = duelist2.lifePoints)

            HorizontalDivider(modifier = Modifier.padding(8.dp, 0.dp))

            // Life Point Addition, Subtraction and Running section.
            Row (modifier = Modifier.fillMaxWidth()
            ){
                Column (modifier = Modifier
                    .weight(3.5f)
                    .padding(4.dp)) {
                    AddSubtractButton(
                        isAdd = true,
                        playerSlotTarget = PlayerSlot.PLAYER_ONE,
                        doEnable = isLocked,
                        onClick = { player ->
                            duelViewModel.modifyPlayerLifePoints(
                                playerSlot = player,
                                doAdd = true)
                        }
                    )
                    AddSubtractButton(
                        isAdd = false,
                        playerSlotTarget = PlayerSlot.PLAYER_ONE,
                        doEnable = isLocked,
                        onClick = { player ->
                            duelViewModel.modifyPlayerLifePoints(
                                playerSlot = player,
                                doAdd = false)
                        }
                    )
                }
                LifePoints(
                    lifePoints = runningLifePoints,
                    modifier = Modifier
                        .weight(3f), 36.sp)
                Column (modifier = Modifier
                    .weight(3.5f)
                    .padding(4.dp)) {
                    AddSubtractButton(
                        isAdd = true,
                        playerSlotTarget = PlayerSlot.PLAYER_TWO,
                        doEnable = isLocked,
                        onClick = { player ->
                            duelViewModel.modifyPlayerLifePoints(
                                playerSlot = player,
                                doAdd = true)
                        }
                    )
                    AddSubtractButton(
                        isAdd = false,
                        playerSlotTarget = PlayerSlot.PLAYER_TWO,
                        doEnable = isLocked,
                        onClick = { player ->
                            duelViewModel.modifyPlayerLifePoints(
                                playerSlot = player,
                                doAdd = false)
                        }
                    )
                }
            }

            // Dice Roll, Coin Flip and Clear Section.
            Row (modifier = Modifier.fillMaxWidth()
            ){
                IconButton(
                    modifier = Modifier
                        .weight(3.5f),
                    resourceID = R.drawable.dice_outlined,
                    isEnabled = isLocked,
                    onClick = {
                        duelViewModel.simulateDiceRoll()
                    })
                TextButton(
                    modifier = Modifier
                        .weight(3f),
                    buttonText = "CLR",
                    minSize = 64.dp,
                    isEnabled = isLocked,
                    onClick = {
                        duelViewModel.clearRunningLifePoints()
                    })
                IconButton(
                    modifier = Modifier
                        .weight(3.5f),
                    resourceID = R.drawable.chip_outlined,
                    isEnabled = isLocked,
                    onClick = {
                        duelViewModel.simulateCoinFlip()
                    })
            }

            HorizontalDivider(modifier = Modifier.padding(8.dp))

            // Number button(s)
            // 7-9
            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
            ){
                TextButton(
                    modifier = Modifier
                        .weight(1f),
                    buttonText = "7",
                    isEnabled = isLocked,
                    onClick = {
                        duelViewModel.runningLifePointsCalculatorNumber(7)
                    }
                )
                TextButton(
                    modifier = Modifier
                        .weight(1f),
                    buttonText = "8",
                    isEnabled = isLocked,
                    onClick = {
                        duelViewModel.runningLifePointsCalculatorNumber(8)
                    }
                )
                TextButton(
                    modifier = Modifier
                        .weight(1f),
                    buttonText = "9",
                    isEnabled = isLocked,
                    onClick = {
                        duelViewModel.runningLifePointsCalculatorNumber(9)
                    }
                )
            }

            // 4-6
            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
            ){
                TextButton(
                    modifier = Modifier
                        .weight(1f),
                    buttonText = "4",
                    isEnabled = isLocked,
                    onClick = {
                        duelViewModel.runningLifePointsCalculatorNumber(4)
                    }
                )
                TextButton(
                    modifier = Modifier
                        .weight(1f),
                    buttonText = "5",
                    isEnabled = isLocked,
                    onClick = {
                        duelViewModel.runningLifePointsCalculatorNumber(5)
                    }
                )
                TextButton(
                    modifier = Modifier
                        .weight(1f),
                    buttonText = "6",
                    isEnabled = isLocked,
                    onClick = {
                        duelViewModel.runningLifePointsCalculatorNumber(6)
                    }
                )
            }

            // 1-3
            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
            ){
                TextButton(
                    modifier = Modifier
                        .weight(1f),
                    buttonText = "1",
                    isEnabled = isLocked,
                    onClick = {
                        duelViewModel.runningLifePointsCalculatorNumber(1)
                    }
                )
                TextButton(
                    modifier = Modifier
                        .weight(1f),
                    buttonText = "2",
                    isEnabled = isLocked,
                    onClick = {
                        duelViewModel.runningLifePointsCalculatorNumber(2)
                    }
                )
                TextButton(
                    modifier = Modifier
                        .weight(1f),
                    buttonText = "3",
                    isEnabled = isLocked,
                    onClick = {
                        duelViewModel.runningLifePointsCalculatorNumber(3)
                    }
                )
            }

            // x10 - x1000
            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
            ){
                TextButton(
                    modifier = Modifier
                        .weight(1f),
                    buttonText = "0",
                    isEnabled = isLocked,
                    onClick = {
                        duelViewModel.multiplyRunningLifePoints(10)
                    }
                )
                TextButton(
                    modifier = Modifier
                        .weight(1f),
                    buttonText = "00",
                    isEnabled = isLocked,
                    onClick = {
                        duelViewModel.multiplyRunningLifePoints(100)
                    }
                )
                TextButton(
                    modifier = Modifier
                        .weight(1f),
                    buttonText = "000",
                    isEnabled = isLocked,
                    onClick = {
                        duelViewModel.multiplyRunningLifePoints(1000)
                    }
                )
            }

            HorizontalDivider(modifier = Modifier.padding(8.dp))
        }
    }

    /***************************************************************************************************************************************
     *           Method:    TextButton
     *       Parameters:    modifier
     *                      onClick
     *                          - Function called when button is clicked.
     *          Returns:    None.
     *      Description:    Draws a button with text.
     **************************************************************************************************************************************/
    @Composable
    fun TextButton(modifier: Modifier, buttonText: String, minSize: Dp = 56.dp, isEnabled: Boolean, onClick: () -> Unit)
    {
        Button(
            modifier = modifier
                .heightIn(minSize)
                .padding(4.dp, 0.dp),
            onClick = onClick,
            colors = ButtonDefaults.outlinedButtonColors(),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
            enabled = isEnabled
        ) {
            Text(text = buttonText,
                style = Typography.titleLarge)
        }
    }

    /***************************************************************************************************************************************
     *           Method:    IconButton
     *       Parameters:    modifier
     *                      resourceID
     *                          - Drawable resource to use for icon.
     *                      minSize
     *                          - (Optional) minimum size.
     *                      onClick
     *                          - Function called when button is clicked.
     *          Returns:    None.
     *      Description:    Draws a button with text.
     **************************************************************************************************************************************/
    @Composable
    fun IconButton(modifier: Modifier, resourceID: Int, minSize: Dp = 56.dp, isEnabled: Boolean, onClick: () -> Unit)
    {
        Surface (
            modifier = modifier
                .heightIn(minSize)
                .fillMaxWidth(1f)
                .padding(2.dp),
            shape = MaterialTheme.shapes.small
        ){
            Button(
                onClick = { onClick()},
                colors = ButtonDefaults.outlinedButtonColors(),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                enabled = isEnabled
            ) {
                Icon(
                    painter = painterResource(resourceID),
                    contentDescription = "",
                    modifier = modifier.size(minSize - 8.dp))
            }
        }
    }

    /***************************************************************************************************************************************
     *           Method:    PlayerSection
     *       Parameters:    playerOneName
     *                          - Name of player one.
     *                      playerOneLifePoints
     *                          - Life points of player one.
     *                      playerTwoName
     *                          - Name of player two.
     *                      playerTwoLifePoints
     *                          - Life points of player two.
     *          Returns:    None.
     *      Description:    Draws the player(s) section.
     **************************************************************************************************************************************/
    @Composable
    fun PlayerSection(playerOneName: String, playerOneLifePoints: Int, playerTwoName: String, playerTwoLifePoints: Int)
    {
        Row (modifier = Modifier.fillMaxWidth()
        ){
            Column (modifier = Modifier.weight(1f)){
                PlayerTitle(
                    name = playerOneName,
                    modifier = Modifier
                        .fillMaxWidth())
                LifePoints(lifePoints = playerOneLifePoints,
                    modifier = Modifier
                        .fillMaxWidth(),
                    fontSize = 56.sp)
            }
            Column (modifier = Modifier.weight(1f)){
                PlayerTitle(
                    name = playerTwoName,
                    modifier = Modifier
                        .fillMaxWidth())
                LifePoints(lifePoints = playerTwoLifePoints,
                    modifier = Modifier
                        .fillMaxWidth(),
                    fontSize = 56.sp)
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
                .padding(8.dp)
                .clickable { /*TODO*/ },
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = name,
                modifier.padding(4.dp),
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
    fun LifePoints(lifePoints: Int, modifier: Modifier, fontSize: TextUnit)
    {
        Text(
            text = lifePoints.toString(),
            modifier = modifier
                .padding(2.dp),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            style = Typography.titleLarge,
            fontSize = fontSize
        )
    }

    /***************************************************************************************************************************************
     *           Method:    AddSubtractButton
     *       Parameters:    isAdd
     *                          - Whether the button is an addition (true) or not (false).
     *                      player
     *                          - Player count (1 or 2).
     *                      onClick
     *                          - Button click function.
     *          Returns:    None.
     *      Description:    Draws a player's life points.
     **************************************************************************************************************************************/
    @Composable
    fun AddSubtractButton(isAdd: Boolean, playerSlotTarget: PlayerSlot, doEnable: Boolean, onClick: (playerSlot: PlayerSlot) -> Unit)
    {
        // Determines the icon resource used.
        val iconID = if (isAdd)
        {
            R.drawable.add_filled
        }
        else
        {
            R.drawable.remove_filled
        }

        // Actually draw.
        IconButton(
            modifier = Modifier,
            resourceID = iconID,
            minSize = 48.dp,
            isEnabled = doEnable,
            onClick = {
                onClick(playerSlotTarget)
            }
        )
    }

    //endregion
}