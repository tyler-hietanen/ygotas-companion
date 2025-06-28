/*******************************************************************************************************************************************
 *           Source:    DuelViewModel.kt
 *      Description:    The ViewModel for the duel experience, tracking an active duel.
 ******************************************************************************************************************************************/
package com.tyler_hietanen.ygotascompanion.presentation.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tyler_hietanen.ygotascompanion.business.duel.Duelist
import com.tyler_hietanen.ygotascompanion.business.duel.Player
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class DuelViewModel: ViewModel()
{
    /***************************************************************************************************************************************
     *      Constants
     **************************************************************************************************************************************/
    //region Constants

    // Default life points.
    private val startingLifePoints = 8000

    // Maximum life points.
    private val maximumLifePoints = 80000

    //endregion

    /***************************************************************************************************************************************
     *      Properties
     **************************************************************************************************************************************/
    //region Properties

    // Player 1.
    private val _duelist1 = mutableStateOf(Duelist())
    val duelist1: State<Duelist> = _duelist1

    // Player 2.
    private val _duelist2 = mutableStateOf(Duelist())
    val duelist2: State<Duelist> = _duelist2

    // Running life points.
    private val _runningLifePoints = mutableIntStateOf(0)
    val runningLifePoints: State<Int> = _runningLifePoints

    // Shown user message (Used to show coin flips or dice rolls).
    private val _customMessages = Channel<String>()
    val customMessages = _customMessages.receiveAsFlow()

    //endregion

    /***************************************************************************************************************************************
     *      Methods
     **************************************************************************************************************************************/
    //region Methods

    /***************************************************************************************************************************************
     *           Method:    initialize
     *       Parameters:    None.
     *          Returns:    None.
     *      Description:    Initializer function for this view model.
     **************************************************************************************************************************************/
    fun initialize()
    {
        // Set default names.
        _duelist1.value.setDuelistName("Player 1")
        _duelist2.value.setDuelistName("Player 2")

        // Reset duel to default state.
        resetDuel()
    }

    /***************************************************************************************************************************************
     *           Method:    resetDuel
     *       Parameters:    None.
     *          Returns:    None.
     *      Description:    Resets the duel to default state.
     **************************************************************************************************************************************/
    fun resetDuel()
    {
        // Resets both duelists to default state.
        val players = listOf(
            Player.PLAYER_ONE, Player.PLAYER_TWO
        )
        players.forEach { player ->
            val duelist = getDuelist(player).copy()
            duelist.resetPlayer(startingLifePoints)
            updateDuelist(player, duelist)
        }

        // Reset other values.
        clearRunningLifePoints()
    }

    /***************************************************************************************************************************************
     *           Method:    modifyPlayerLifePoints
     *       Parameters:    player
     *                          - Which player to adjust.
     *                      doAdd
     *                          - Whether it should be an addition (true) or subtraction (false).
     *          Returns:    None.
     *      Description:    Modifies a player's life points.
     **************************************************************************************************************************************/
    fun modifyPlayerLifePoints(player: Player, doAdd: Boolean)
    {
        // Gathers a copy of the appropriate duelist.
        val duelist = getDuelist(player).copy()

        // Copies current life points and creates a new life point value.
        val runningLifePointCount = runningLifePoints.value

        // Does the addition (or subtraction).
        var newLifePointValue = if (doAdd)
        {
            (duelist.lifePoints + runningLifePointCount)
        }
        else
        {
            (duelist.lifePoints - runningLifePointCount)
        }

        // Checks for safety of the operation before committing it.
        val didSafelyModify = if (doAdd)
        {
            (newLifePointValue <= maximumLifePoints)
        }
        else
        {
            // Cap life points to 0 if needed.
            if (newLifePointValue < 0)
            {
                newLifePointValue = 0
            }
            // Force true, since it's safe.
            true
        }

        if (didSafelyModify)
        {
            // Update player's life points.
            duelist.lifePoints = newLifePointValue
            updateDuelist(player, duelist)
        }
        // Otherwise, operation is ignored. Not a safe one.

        // No matter what, clear points.
        clearRunningLifePoints()
    }

    /***************************************************************************************************************************************
     *           Method:    simulateDiceRoll
     *       Parameters:    None.
     *          Returns:    None.
     *      Description:    Simulates a dice roll (1 - 6).
     **************************************************************************************************************************************/
    fun simulateDiceRoll()
    {
        // Generate random number from 1 to 6.
        val diceRollValue = Random.nextInt(1, 6)

        // Emit update.
        viewModelScope.launch {
            _customMessages.send("Dice Roll: $diceRollValue")
        }
    }

    /***************************************************************************************************************************************
     *           Method:    simulateCoinFlip
     *       Parameters:    None.
     *          Returns:    None.
     *      Description:    Simulates a coin flip (Heads - Tail).
     **************************************************************************************************************************************/
    fun simulateCoinFlip()
    {
        // Generate random number from 1 to 6.
        val coinFlipValue = if (Random.nextInt(1, 2) == 1)
        {
            "Heads"
        }
        else
        {
            "Tails"
        }

        // Emit update.
        viewModelScope.launch {
            _customMessages.send("Coin Flip: $coinFlipValue")
        }
    }

    /***************************************************************************************************************************************
     *           Method:    clearRunningLifePoints
     *       Parameters:    None.
     *          Returns:    None.
     *      Description:    Clears the running life points count.
     **************************************************************************************************************************************/
    fun clearRunningLifePoints()
    {
        updateRunningLifePoints(0)
    }

    /***************************************************************************************************************************************
     *           Method:    runningLifePointsCalculatorNumber
     *       Parameters:    number
     *                          - Calculator number clicked.
     *          Returns:    None.
     *      Description:    Adds number to running life point count at the end.
     **************************************************************************************************************************************/
    fun runningLifePointsCalculatorNumber(number:Int)
    {
        // Copies running life points.
        var runningLifePoints = _runningLifePoints.intValue

        // Performs operation. Multiply value by 10, then add value.
        runningLifePoints *= 10
        runningLifePoints += number

        // Check for safety.
        if (runningLifePoints <= maximumLifePoints)
        {
            // Commit change.
            updateRunningLifePoints(runningLifePoints)
        }
    }

    /***************************************************************************************************************************************
     *           Method:    multiplyRunningLifePoints
     *       Parameters:    number
     *                          - Number to be used for multiply factor.
     *          Returns:    None.
     *      Description:    Uses number for multiplication factor.
     **************************************************************************************************************************************/
    fun multiplyRunningLifePoints(factor: Int)
    {
        // Copies running life points.
        var runningLifePoints = _runningLifePoints.intValue

        // Performs operation. Multiply value by factor.
        runningLifePoints *= factor

        // Check for safety.
        if (runningLifePoints <= maximumLifePoints)
        {
            // Commit change.
            updateRunningLifePoints(runningLifePoints)
        }
    }

    //endregion

    /***************************************************************************************************************************************
     *      Private Methods
     **************************************************************************************************************************************/
    //region Private Methods

    /***************************************************************************************************************************************
     *           Method:    getDuelist
     *       Parameters:    player
     *                          - Player to retrieve duelist class.
     *          Returns:    Duelist
     *                          - Duelist class.
     *      Description:    Retrieves duelist class for the associated player.
     **************************************************************************************************************************************/
    private fun getDuelist(player: Player): Duelist
    {
        return when (player)
        {
            Player.PLAYER_ONE -> _duelist1.value
            Player.PLAYER_TWO -> _duelist2.value
        }
    }

    /***************************************************************************************************************************************
     *           Method:    updateDuelist
     *       Parameters:    player
     *                          - Player to update.
     *                      newDuelist
     *                          - New (or updated) duelist value.
     *          Returns:    None.
     *      Description:    Updates player's duelist value.
     **************************************************************************************************************************************/
    private fun updateDuelist(player: Player, newDuelist: Duelist)
    {
        when (player)
        {
            Player.PLAYER_ONE -> _duelist1.value = newDuelist
            Player.PLAYER_TWO -> _duelist2.value = newDuelist
        }
    }

    /***************************************************************************************************************************************
     *           Method:    updateRunningLifePoints
     *       Parameters:    runningPoints
     *                          - Running life points value to update to.
     *          Returns:    None.
     *      Description:    Updates running life points.
     **************************************************************************************************************************************/
    private fun updateRunningLifePoints(runningPoints: Int)
    {
        _runningLifePoints.intValue = runningPoints
    }

    //endregion
}