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

// TODO Flash a yelling toast the first time that someone does a non-equal number of life points (Not divisible by 100).
// TODO Flash a toast when someone loses ("Womp womp") (TODO play losing quote, if any?)
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

    // Current user notification message (Usually shown as a toast or snack bar).
    private val _userMessage = mutableStateOf(String())
    val userMessage: State<String> = _userMessage

    //endregion

    /***************************************************************************************************************************************
     *      Fields
     **************************************************************************************************************************************/
    //region Fields

    // Whether user has already been warned about an ugly life point number.
    private var _hasWarnedUglyLifePoints = false;

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
        // Resets both duelists to default state.
        _duelist1.value.resetPlayer(startingLifePoints)
        _duelist2.value.resetPlayer(startingLifePoints)

        // Reset other values.
        _runningNumber.intValue = 0
        _userMessage.value = ""
    }

    //endregion
}