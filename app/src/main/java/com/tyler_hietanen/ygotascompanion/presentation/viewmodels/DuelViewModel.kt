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
import com.tyler_hietanen.ygotascompanion.business.duel.PlayerSlot
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

// TODO Load various players from storage. If no players are in storage, then create default set.
class DuelViewModel: ViewModel()
{
    /***************************************************************************************************************************************
     *      Constants
     **************************************************************************************************************************************/
    //region Constants

    private companion object
    {
        // Starting and maximum life points.
        const val STARTING_LIFE_POINTS = 8000
        const val MAXIMUM_LIFE_POINTS = 80000
    }

    //endregion

    /***************************************************************************************************************************************
     *      Properties
     **************************************************************************************************************************************/
    //region Properties

    // Player slot 1.
    private val _duelist1 = mutableStateOf(Duelist())
    val duelist1: State<Duelist> = _duelist1

    // Player slot 2.
    private val _duelist2 = mutableStateOf(Duelist())
    val duelist2: State<Duelist> = _duelist2

    // Running life points.
    private val _runningLifePoints = mutableIntStateOf(0)
    val runningLifePoints: State<Int> = _runningLifePoints

    // Is duel enabled. Set to true when dueling is allowed.
    private val _isDuelEnabled = mutableStateOf(true)
    val isDuelEnabled: State<Boolean> = _isDuelEnabled

    // Shown user message (Used to show coin flips or dice rolls).
    private val _customMessages = Channel<String>()
    val customMessages = _customMessages.receiveAsFlow()

    //endregion

    /***************************************************************************************************************************************
     *      Fields
     **************************************************************************************************************************************/
    //region Fields

    // Whether user has been shamed for adding a bad life points.
    private var _didShameUser = false

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
        val playerSlotList = listOf(
            PlayerSlot.PLAYER_ONE, PlayerSlot.PLAYER_TWO
        )
        playerSlotList.forEach { slot ->
            val duelist = getDuelist(slot).copy()
            duelist.resetPlayer(STARTING_LIFE_POINTS)
            updateDuelist(slot, duelist)
        }

        // Reset other values.
        clearRunningLifePoints()
        _didShameUser = false
        _isDuelEnabled.value = true
    }

    /***************************************************************************************************************************************
     *           Method:    modifyPlayerLifePoints
     *       Parameters:    playerSlot
     *                          - Which player slot to adjust.
     *                      doAdd
     *                          - Whether it should be an addition (true) or subtraction (false).
     *          Returns:    None.
     *      Description:    Modifies a player's life points.
     **************************************************************************************************************************************/
    fun modifyPlayerLifePoints(playerSlot: PlayerSlot, doAdd: Boolean)
    {
        // Ignore if no change or if locked.
        if ((runningLifePoints.value != 0) && _isDuelEnabled.value)
        {
            // Did someone lose?
            var isALoss = false

            // Gathers a copy of the appropriate duelist.
            val duelist = getDuelist(playerSlot).copy()

            // Copies current life points and creates a new life point value.
            val lifePointChange = if (doAdd)
            {
                runningLifePoints.value
            }
            else
            {
                // Set as negative.
                (runningLifePoints.value * -1)
            }

            // Does change to life points (But does not commit it).
            var newLifePointValue = (duelist.lifePoints + lifePointChange)

            // Checks for safety of the change before committing it.
            val didSafelyModify = if (newLifePointValue > 0)
            {
                // Positive. Must be less than or equal to maximum.
                (newLifePointValue <= MAXIMUM_LIFE_POINTS)
            }
            else
            {
                // Life points are equal to 0, or negative.
                // Force to be zero and flag that user lost duel.
                newLifePointValue = 0
                isALoss = true

                // Force true, since it's safe.
                true
            }

            if (didSafelyModify)
            {
                // Update player's life points.
                duelist.modifyLifePoints(newLifePointValue)
                updateDuelist(playerSlot, duelist)

                // Before attempting any shame, check for a loss.
                if (isALoss)
                {
                    handleLoss(playerSlot, getOtherPlayer(playerSlot))
                }
                else
                {
                    // Check if user should be shamed for life points.
                    attemptUserShame(lifePointChange, playerSlot)
                }
            }
            // Otherwise, operation is ignored. Not a safe one.

            // No matter what, clear points.
            clearRunningLifePoints()
        }
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
    fun runningLifePointsCalculatorNumber(number: Int)
    {
        // Copies running life points.
        var runningLifePoints = _runningLifePoints.intValue

        // Performs operation. Multiply value by 10, then add value.
        runningLifePoints *= 10
        runningLifePoints += number

        // Check for safety.
        if (runningLifePoints <= MAXIMUM_LIFE_POINTS)
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
        if (runningLifePoints <= MAXIMUM_LIFE_POINTS)
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
     *       Parameters:    playerSlot
     *                          - Player slot to retrieve duelist class.
     *          Returns:    Duelist
     *                          - Duelist class.
     *      Description:    Retrieves duelist class for the associated player slot.
     **************************************************************************************************************************************/
    private fun getDuelist(playerSlot: PlayerSlot): Duelist
    {
        return when (playerSlot)
        {
            PlayerSlot.PLAYER_ONE -> _duelist1.value
            PlayerSlot.PLAYER_TWO -> _duelist2.value
        }
    }

    /***************************************************************************************************************************************
     *           Method:    getOtherPlayer
     *       Parameters:    playerSlot
     *                          - The current player slot.
     *          Returns:    Player
     *                          - The other player slot.
     *      Description:    Retrieves the player definition for the other player.
     **************************************************************************************************************************************/
    private fun getOtherPlayer(playerSlot: PlayerSlot): PlayerSlot
    {
        return when (playerSlot)
        {
            PlayerSlot.PLAYER_ONE -> PlayerSlot.PLAYER_TWO
            PlayerSlot.PLAYER_TWO -> PlayerSlot.PLAYER_ONE
        }
    }

    /***************************************************************************************************************************************
     *           Method:    updateDuelist
     *       Parameters:    playerSlot
     *                          - Player slot to update.
     *                      newDuelist
     *                          - New (or updated) duelist value.
     *          Returns:    None.
     *      Description:    Updates player's duelist value.
     **************************************************************************************************************************************/
    private fun updateDuelist(playerSlot: PlayerSlot, newDuelist: Duelist)
    {
        when (playerSlot)
        {
            PlayerSlot.PLAYER_ONE -> _duelist1.value = newDuelist
            PlayerSlot.PLAYER_TWO -> _duelist2.value = newDuelist
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

    /***************************************************************************************************************************************
     *           Method:    handleLoss
     *       Parameters:    losingPlayerSlot
     *                          - Player who lost.
     *                      winningPlayerSlot
     *                          - Player who won.
     *          Returns:    None.
     *      Description:    Address a duel loss. Locks until they reset and sends message(s).
     **************************************************************************************************************************************/
    private fun handleLoss(losingPlayerSlot: PlayerSlot, winningPlayerSlot: PlayerSlot)
    {
        // TODO Revisit this function once win/loss tracking is added.
        // This player has lost. We should mock them. Before anything else, lock the duel (so they have to reset and to avoid any of these
        // other changes from happening).
        _isDuelEnabled.value = false

        // Send out a loss message.
        viewModelScope.launch {
            // Address losing player.
            var duelist = getDuelist(losingPlayerSlot)
            var message = "Womp, womp! Better luck next time, ${duelist.name}"
            _customMessages.send(message)

            // Address winning player.
            duelist = getDuelist(winningPlayerSlot)
            message = "Good job, ${duelist.name}"
            _customMessages.send(message)
        }
    }

    /***************************************************************************************************************************************
     *           Method:    attemptUserShame
     *       Parameters:    lifePointChange
     *                          - The life point change (positive or negative)
     *                      actingPlayer
     *                          - The player to which the life point change is affecting.
     *          Returns:    None.
     *      Description:    Checks if it should shame user for dealing (or healing) a strange amount of life points.
     **************************************************************************************************************************************/
    private fun attemptUserShame(lifePointChange: Int, actingPlayerSlot: PlayerSlot)
    {
        // If app hasn't already shamed a user.
        if (!_didShameUser)
        {
            // Check if we need to shame. We shame if the points added (or subtracted) are not evenly divisible by 100.
            val doShame = ((lifePointChange % 100) != 0)
            if (doShame)
            {
                // Make it so we don't shame again.
                _didShameUser = true

                // Do we shame the player being acted upon, or the other player? We shame the acting player if the operation is an addition,
                // or we shame the other player if the operation is a subtraction.
                val doShameActingPlayer = (lifePointChange > 0)

                // Determine how we shame them.
                val playerShameMessage: String
                if (doShameActingPlayer)
                {
                    // We need to shame the acting player.
                    val playerName = getDuelist(actingPlayerSlot).name
                    playerShameMessage = "You added a strange amount of life points? For shame, $playerName..."
                }
                else
                {
                    // We need to shame the other player.
                    val otherPlayer = getOtherPlayer(actingPlayerSlot)
                    playerShameMessage = "You did a stupid amount of damage. Dick move, ${getDuelist(otherPlayer).name}!"
                }

                // Emit update (shame!).
                viewModelScope.launch {
                    _customMessages.send(playerShameMessage)
                }
            }
            // Otherwise, do nothing.
        }
    }

    //endregion
}