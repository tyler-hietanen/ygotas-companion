/*******************************************************************************************************************************************
 *           Source:    DuelScreen.kt
 *      Description:    Contains content for the Duel Screen.
 ******************************************************************************************************************************************/
package com.tyler_hietanen.yugioh_companion.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tyler_hietanen.yugioh_companion.R
import com.tyler_hietanen.yugioh_companion.business.duel.PlayerSlot
import com.tyler_hietanen.yugioh_companion.presentation.viewmodels.DuelViewModel
import com.tyler_hietanen.yugioh_companion.ui.layout.CompanionButtons.IconButton
import com.tyler_hietanen.yugioh_companion.ui.layout.CompanionButtons.TextButton
import com.tyler_hietanen.yugioh_companion.ui.theme.Typography
import kotlinx.coroutines.flow.collectLatest

object DuelScreen
{
    /***************************************************************************************************************************************
     *      (Public) Composable Methods
     **************************************************************************************************************************************/
    //region (Public) Composable Methods

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
        val runningLifePoints by duelViewModel.calculatorNumber
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
            LifePointModificationSection(
                isLocked = isLocked,
                lifePoints = runningLifePoints,
                onClick = { playerSlot, doAdd ->
                    duelViewModel.modifyPlayerLifePoints(
                        playerSlot = playerSlot,
                        doAdd = doAdd
                    )
                }
            )

            // Dice Roll, Coin Flip and Clear Section.
            CoinDiceClearSection(
                isLocked = isLocked,
                onDiceRollClick = { duelViewModel.simulateDiceRoll() },
                onClearClick = { duelViewModel.clearRunningLifePoints() },
                onCoinFlipClick = { duelViewModel.simulateCoinFlip() },
            )

            HorizontalDivider(modifier = Modifier.padding(8.dp))

            // Number button(s)
            // 7-9.
            CalculatorNumberRow(
                isLocked = isLocked,
                first = 7,
                second = 8,
                third = 9,
                onNumberClick = { number ->
                    duelViewModel.pressedCalculatorNumber(number)
                },
            )

            // 4-6.
            CalculatorNumberRow(
                isLocked = isLocked,
                first = 4,
                second = 5,
                third = 6,
                onNumberClick = { number ->
                    duelViewModel.pressedCalculatorNumber(number)
                },
            )

            // 1-3.
            CalculatorNumberRow(
                isLocked = isLocked,
                first = 1,
                second = 2,
                third = 3,
                onNumberClick = { number ->
                    duelViewModel.pressedCalculatorNumber(number)
                },
            )

            // x10 - x1000.
            CalculatorMultiplicationRow(
                isLocked = isLocked,
                onMultiplyClick = { number ->
                    duelViewModel.pressedCalculatorFactor(number)
                }
            )

            HorizontalDivider(modifier = Modifier.padding(8.dp))
        }
    }

    //endregion

    /***************************************************************************************************************************************
     *      (Private) Composable Methods
     **************************************************************************************************************************************/
    //region (Private) Composable Methods

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
    private fun PlayerSection(playerOneName: String, playerOneLifePoints: Int, playerTwoName: String, playerTwoLifePoints: Int)
    {
        Row (modifier = Modifier.fillMaxWidth()
        ){
            Column (modifier = Modifier.weight(1f)){
                PlayerTitleText(
                    name = playerOneName,
                    modifier = Modifier
                        .fillMaxWidth())
                LifePointsText(lifePoints = playerOneLifePoints,
                    modifier = Modifier
                        .fillMaxWidth(),
                    fontSize = 56.sp)
            }
            Column (modifier = Modifier.weight(1f)){
                PlayerTitleText(
                    name = playerTwoName,
                    modifier = Modifier
                        .fillMaxWidth())
                LifePointsText(lifePoints = playerTwoLifePoints,
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
    private fun PlayerTitleText(name: String, modifier: Modifier)
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
     *           Method:    LifePointsText
     *       Parameters:    lifePoints
     *                          - Player's life points.
     *                      modifier
     *                      fontSize
     *                          - Desired font size.
     *          Returns:    None.
     *      Description:    Draws a player's life points.
     **************************************************************************************************************************************/
    @Composable
    private fun LifePointsText(lifePoints: Int, modifier: Modifier, fontSize: TextUnit)
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
     *           Method:    LifePointModificationSection
     *       Parameters:    isLocked
     *                      lifePoints
     *                      onClick
     *          Returns:    None.
     *      Description:    Draws the life point modification section (Add,Minus buttons for both players, running life points).
     **************************************************************************************************************************************/
    @Composable
    private fun LifePointModificationSection(isLocked: Boolean, lifePoints: Int, onClick: (playerSlot: PlayerSlot, doAdd: Boolean) -> Unit)
    {
        Row (modifier = Modifier.fillMaxWidth()
        ){
            Column (modifier = Modifier
                .weight(3.5f)
                .padding(4.dp)) {
                AddSubtractButton(
                    isAdd = true,
                    playerSlotTarget = PlayerSlot.PLAYER_ONE,
                    doEnable = isLocked,
                    onClick = { playerSlot ->
                        onClick(playerSlot, true)
                    }
                )
                AddSubtractButton(
                    isAdd = false,
                    playerSlotTarget = PlayerSlot.PLAYER_ONE,
                    doEnable = isLocked,
                    onClick = { playerSlot ->
                        onClick(playerSlot, false)
                    }
                )
            }
            LifePointsText(
                lifePoints = lifePoints,
                modifier = Modifier
                    .weight(3f), 36.sp)
            Column (modifier = Modifier
                .weight(3.5f)
                .padding(4.dp)) {
                AddSubtractButton(
                    isAdd = true,
                    playerSlotTarget = PlayerSlot.PLAYER_TWO,
                    doEnable = isLocked,
                    onClick = { playerSlot ->
                        onClick(playerSlot, true)
                    }
                )
                AddSubtractButton(
                    isAdd = false,
                    playerSlotTarget = PlayerSlot.PLAYER_TWO,
                    doEnable = isLocked,
                    onClick = { playerSlot ->
                        onClick(playerSlot, false)
                    }
                )
            }
        }
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
    private fun AddSubtractButton(isAdd: Boolean, playerSlotTarget: PlayerSlot, doEnable: Boolean, onClick: (playerSlot: PlayerSlot) -> Unit)
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

    /***************************************************************************************************************************************
     *           Method:    CoinDiceClearSection
     *       Parameters:    isLocked
     *                      onDiceRollClick
     *                      onClearClick
     *                      onCoinFlipClick
     *      Description:    Draws a player's life points.
     **************************************************************************************************************************************/
    @Composable
    private fun CoinDiceClearSection(isLocked: Boolean, onDiceRollClick: () -> Unit, onClearClick: () -> Unit, onCoinFlipClick: () -> Unit)
    {
        Row (modifier = Modifier.fillMaxWidth()
        ){
            IconButton(
                modifier = Modifier
                    .weight(3.5f),
                resourceID = R.drawable.dice_outlined,
                isEnabled = isLocked,
                onClick = {
                    onDiceRollClick()
                }
            )
            TextButton(
                modifier = Modifier
                    .weight(3f),
                buttonText = "CLR",
                minSize = 64.dp,
                isEnabled = isLocked,
                onClick = {
                    onClearClick()

                }
            )
            IconButton(
                modifier = Modifier
                    .weight(3.5f),
                resourceID = R.drawable.chip_outlined,
                isEnabled = isLocked,
                onClick = {
                    onCoinFlipClick()
                }
            )
        }
    }

    /***************************************************************************************************************************************
     *           Method:    CalculatorNumberRow
     *       Parameters:    isLocked
     *                      first
     *                      second
     *                      third
     *                      onNumberClick
     *          Returns:    None.
     *      Description:    Draws a row of calculator buttons.
     **************************************************************************************************************************************/
    @Composable
    private fun CalculatorNumberRow(isLocked: Boolean, first: Int, second: Int, third: Int, onNumberClick: (number: Int) -> Unit)
    {
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
        ){
            TextButton(
                modifier = Modifier
                    .weight(1f),
                buttonText = first.toString(),
                isEnabled = isLocked,
                onClick = {
                    onNumberClick(first)
                }
            )
            TextButton(
                modifier = Modifier
                    .weight(1f),
                buttonText = second.toString(),
                isEnabled = isLocked,
                onClick = {
                    onNumberClick(second)
                }
            )
            TextButton(
                modifier = Modifier
                    .weight(1f),
                buttonText = third.toString(),
                isEnabled = isLocked,
                onClick = {
                    onNumberClick(third)
                }
            )
        }
    }

    /***************************************************************************************************************************************
     *           Method:    CalculatorMultiplicationRow
     *       Parameters:    isLocked
     *                      onMultiplyClick
     *          Returns:    None.
     *      Description:    Draws a multiplication row (10, 100, 1000).
     **************************************************************************************************************************************/
    @Composable
    private fun CalculatorMultiplicationRow(isLocked: Boolean, onMultiplyClick: (number: Int) -> Unit)
    {
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
                    onMultiplyClick(10)
                }
            )
            TextButton(
                modifier = Modifier
                    .weight(1f),
                buttonText = "00",
                isEnabled = isLocked,
                onClick = {
                    onMultiplyClick(100)
                }
            )
            TextButton(
                modifier = Modifier
                    .weight(1f),
                buttonText = "000",
                isEnabled = isLocked,
                onClick = {
                    onMultiplyClick(1000)
                }
            )
        }

    }

    //endregion
}