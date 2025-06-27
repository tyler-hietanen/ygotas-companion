/*******************************************************************************************************************************************
 *           Source:    DuelViewModel.kt
 *      Description:    The ViewModel for the duel experience, tracking an active duel.
 ******************************************************************************************************************************************/
package com.tyler_hietanen.ygotascompanion.presentation.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.tyler_hietanen.ygotascompanion.business.duel.Duelist
import com.tyler_hietanen.ygotascompanion.business.duel.Player

// TODO: Figure out clean means to cause updates when changing duelist value.
class DuelViewModel: ViewModel()
{
    /***************************************************************************************************************************************
     *      Constants
     **************************************************************************************************************************************/
    //region Constants

    // Default life points.
    private val startingLifePoints = 8000

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

    // Running number counter (used for life points, 0 - 20000).
    private val _runningNumber = mutableIntStateOf(0)
    val runningNumber: State<Int> = _runningNumber

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
        _duelist1.value.resetPlayer(startingLifePoints)
        _duelist2.value.resetPlayer(startingLifePoints)

        // Reset other values.
        _runningNumber.intValue = 0
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
        // TODO Modify selected player's life points. Clear running count.
    }

    /***************************************************************************************************************************************
     *           Method:    simulateDiceRoll
     *       Parameters:    None.
     *          Returns:    None.
     *      Description:    Simulates a dice roll (1 - 6).
     **************************************************************************************************************************************/
    fun simulateDiceRoll()
    {
        // TODO Generate from 1 - 6. Show message to user.
    }

    /***************************************************************************************************************************************
     *           Method:    simulateCoinFlip
     *       Parameters:    None.
     *          Returns:    None.
     *      Description:    Simulates a coin flip (Heads - Tail).
     **************************************************************************************************************************************/
    fun simulateCoinFlip()
    {
        // TODO Generate from 1 - 2. Show message to user.
    }

    /***************************************************************************************************************************************
     *           Method:    clearRunningLifePoints
     *       Parameters:    None.
     *          Returns:    None.
     *      Description:    Clears the running life points count.
     **************************************************************************************************************************************/
    fun clearRunningLifePoints()
    {
        // TODO Clear running life points.
    }

    /***************************************************************************************************************************************
     *           Method:    addNumberToRunningLifePoints
     *       Parameters:    number
     *                          - Number to be added to running point.
     *          Returns:    None.
     *      Description:    Adds number to running life point count at the end.
     **************************************************************************************************************************************/
    fun addNumberToRunningLifePoints(number:Int)
    {
        // TODO Adds number to life points. Multiply by 10 first.
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
        // TODO Multiplies running number by factor.
    }

    //endregion
}