/*******************************************************************************************************************************************
 *           Source:    HouseRulesViewModel.kt
 *      Description:    The ViewModel for the house rules experience, managing import and viewing of custom house rules.
 ******************************************************************************************************************************************/
package com.tyler_hietanen.yugioh_companion.presentation.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class HouseRulesViewModel: ViewModel()
{
    /***************************************************************************************************************************************
     *      Constants
     **************************************************************************************************************************************/
    //region Constants

    //endregion

    /***************************************************************************************************************************************
     *      Properties
     **************************************************************************************************************************************/
    //region Properties

    // Whether the application is busy importing House Rules.
    private val _isImportingHouseRules = mutableStateOf(false)
    val isImportingHouseRules: State<Boolean> = _isImportingHouseRules

    // House Rules Content (Markdown format).
    // Null indicates House Rules haven't been imported (or haven't been loaded yet).
    private val _houseRulesContent = mutableStateOf<String?>(null)
    val houseRulesContent: State<String?> = _houseRulesContent

    //endregion

    /***************************************************************************************************************************************
     *      Fields
     **************************************************************************************************************************************/
    //region Fields

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
        // Set values to default states.
        _isImportingHouseRules.value = false
    }

    /***************************************************************************************************************************************
     *           Method:    onImportHouseRules
     *       Parameters:    None.
     *          Returns:    None.
     *      Description:    Call to import new house rules.
     **************************************************************************************************************************************/
    fun onImportHouseRules()
    {
        // TODO. Either force import, or take result of import.
    }

    //endregion

    /***************************************************************************************************************************************
     *      Private Methods
     **************************************************************************************************************************************/
    //region Private Methods

    //endregion
}