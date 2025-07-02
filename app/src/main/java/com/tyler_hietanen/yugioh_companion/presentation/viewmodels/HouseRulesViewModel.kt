/*******************************************************************************************************************************************
 *           Source:    HouseRulesViewModel.kt
 *      Description:    The ViewModel for the house rules experience, managing import and viewing of custom house rules.
 ******************************************************************************************************************************************/
package com.tyler_hietanen.yugioh_companion.presentation.viewmodels

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tyler_hietanen.yugioh_companion.business.houserules.HouseRulesFileHelper
import com.tyler_hietanen.yugioh_companion.presentation.ApplicationViewModel
import kotlinx.coroutines.launch

class HouseRulesViewModel: ViewModel()
{
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

    // Stores reference to application view model for later usage.
    private lateinit var _applicationViewModel: ApplicationViewModel

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
    fun initialize(applicationViewModel: ApplicationViewModel, context: Context)
    {
        // Store reference to application view model.
        _applicationViewModel = applicationViewModel

        // Load house rules from storage (if they exist).
        loadHouseRulesFromStorage(context)

        // Set values to default states.
        _isImportingHouseRules.value = false
    }

    /***************************************************************************************************************************************
     *           Method:    onImportHouseRules
     *       Parameters:    None.
     *          Returns:    None.
     *      Description:    Call to import new house rules.
     **************************************************************************************************************************************/
    fun onImportHouseRules(context: Context, fileUri: Uri)
    {
        // Must be run from view model scope, since it runs with context.
        viewModelScope.launch {
            // Whether it succeeded in importing house rules file.
            var didImportHouseRules = false

            // Obtain lock, letting user know it is busy (And preventing additional attempts, hopefully).
            _isImportingHouseRules.value = true

            // Grab the contents and check if it is not null (no issues occurred on read).
            val rulesContent = HouseRulesFileHelper.readHouseRulesContentFromUri(context, fileUri)
            if ((rulesContent != null) && (rulesContent.isNotEmpty()))
            {
                // Successfully loaded a file (That has content)! Save house rules to memory for later usage.
                didImportHouseRules = HouseRulesFileHelper.saveHouseRulesToStorage(context, rulesContent)
                if (!didImportHouseRules)
                {
                    // This means it was unable to save to memory (for whatever reason).
                    _applicationViewModel.showUserMessage("Loaded house rules, but unable to save to memory.")
                    didImportHouseRules = true
                }

                // Set the current content.
                _houseRulesContent.value = rulesContent
            }

            // Show message based on success/failure.
            if (didImportHouseRules)
            {
                _applicationViewModel.showUserMessage("Loaded house rules.")
            }
            else
            {
                _applicationViewModel.showUserMessage("Unable to load house rules. Something went wrong.")
            }

            // Success or failure, finished loading.
            _isImportingHouseRules.value = false
        }
    }

    //endregion

    /***************************************************************************************************************************************
     *      Private Methods
     **************************************************************************************************************************************/
    //region Private Methods

    /***************************************************************************************************************************************
     *           Method:    loadHouseRulesFromStorage
     *       Parameters:    context
     *          Returns:    None.
     *      Description:    Loads house rules from storage.
     **************************************************************************************************************************************/
    private fun loadHouseRulesFromStorage(context: Context)
    {
        viewModelScope.launch {
            // Flag as busy to prevent other actions.
            _isImportingHouseRules.value = true

            // Grab content of existing house rules file. Will set as null if non-existent.
            _houseRulesContent.value =  HouseRulesFileHelper.retrieveHouseRulesFromStorage(context)

            // Flag as no longer busy.
            _isImportingHouseRules.value = false
        }
    }

    //endregion
}