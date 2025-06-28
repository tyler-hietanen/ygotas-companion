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
        modifyLifePoints(startingLifePoints)
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
        // Check for actual change.
        if (this.name != name)
        {
            // Set name.
            this.name = name
        }
        // Otherwise ignored.
    }

    /***************************************************************************************************************************************
     *           Method:    modifyLifePoints
     *       Parameters:    lifePoints
     *                          - Life points to set.
     *          Returns:    None.
     *      Description:    Sets the duelist's life points.
     **************************************************************************************************************************************/
    fun modifyLifePoints(lifePoints: Int)
    {
        // Set life points.
        this.lifePoints = lifePoints
    }

    //endregion
}