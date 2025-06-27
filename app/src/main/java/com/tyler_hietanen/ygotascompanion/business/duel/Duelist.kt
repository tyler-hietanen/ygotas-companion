/*******************************************************************************************************************************************
 *           Source:    Duelist.kt
 *      Description:    Represents a single duelist and values associated with each.
 ******************************************************************************************************************************************/
package com.tyler_hietanen.ygotascompanion.business.duel

data class Duelist (
    // Player name.
    var name: String = "Player",

    // Player life point count.
    var lifePoints: Int = 0,

    // Player turn counter.
    var turnCounter: Int = 0
){
    /***************************************************************************************************************************************
     *      Methods
     **************************************************************************************************************************************/
    //region Methods

    /***************************************************************************************************************************************
     *           Method:    resetPlayer
     *       Parameters:    startingLifePoints
     *                          - The starting life points for this duel.
     *          Returns:    None.
     *      Description:    Resets a duelist in preparation for a new duel.
     **************************************************************************************************************************************/
    fun resetPlayer(startingLifePoints: Int)
    {
        lifePoints = startingLifePoints
        resetTurnCounter()
    }

    /***************************************************************************************************************************************
     *           Method:    setDuelistName
     *       Parameters:    name
     *                          - New duelist name.
     *          Returns:    None.
     *      Description:    Sets a duelist's name.
     **************************************************************************************************************************************/
    fun setDuelistName(name: String)
    {
        this.name = name
    }

    /***************************************************************************************************************************************
     *           Method:    modifyLifePoints
     *       Parameters:    lifePointChange
     *                          - Life points to change.
     *          Returns:    None.
     *      Description:    Adds (or subtracts) life points from a duelist.
     *             Note:    To add, use a positive parameter. To subtract, use a negative.
     **************************************************************************************************************************************/
    fun modifyLifePoints(lifePointChange: Int)
    {
        // Safely modify.
        if ((lifePointChange + lifePointChange) < 0)
        {
            lifePoints = 0
        }
        else
        {
            lifePoints += lifePointChange
        }
    }

    /***************************************************************************************************************************************
     *           Method:    resetTurnCounter
     *       Parameters:    None.
     *          Returns:    None.
     *      Description:    Resets the turn counter.
     **************************************************************************************************************************************/
    fun resetTurnCounter()
    {
        turnCounter = 0
    }

    /***************************************************************************************************************************************
     *           Method:    incrementTurnCounter
     *       Parameters:    None.
     *          Returns:    None.
     *      Description:    Increments turn counter by 1.
     **************************************************************************************************************************************/
    fun incrementTurnCounter()
    {
        turnCounter++
    }

    //endregion
}