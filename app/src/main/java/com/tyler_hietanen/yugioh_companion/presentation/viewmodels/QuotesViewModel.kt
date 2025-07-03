/*******************************************************************************************************************************************
 *           Source:    QuotesViewModel.kt
 *      Description:    The ViewModel for the quotes experience, managing the import, viewing and listening of quotes.
 ******************************************************************************************************************************************/
package com.tyler_hietanen.yugioh_companion.presentation.viewmodels

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tyler_hietanen.yugioh_companion.business.quotes.Quote
import com.tyler_hietanen.yugioh_companion.business.quotes.QuotesFileHelper
import com.tyler_hietanen.yugioh_companion.presentation.ApplicationViewModel
import kotlinx.coroutines.launch

class QuotesViewModel: ViewModel()
{
    /***************************************************************************************************************************************
     *      Properties
     **************************************************************************************************************************************/
    //region Properties

    // Whether the application is busy importing Quotes.
    private val _isImportingQuotes = mutableStateOf(false)
    val isImportingQuotes: State<Boolean> = _isImportingQuotes

    // Exposes a list of (eventually) quotes to be observed. If this is an empty list, then there are no quotes loaded into the application.
    private val _quotes = mutableStateListOf<Quote>()
    val quotes: List<Quote> = _quotes

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

        // Load quotes from storage (if they exist).
        // TODO.

        // Set values to default states.
        _isImportingQuotes.value = false
    }

    /***************************************************************************************************************************************
     *           Method:    onImportQuotes
     *       Parameters:    None.
     *          Returns:    None.
     *      Description:    Call to import new quotes (from a zipped file).
     **************************************************************************************************************************************/
    fun onImportQuotes(context: Context, fileUri: Uri)
    {
        // Must be run from view model scope, since it runs with context.
        viewModelScope.launch {
            // Stores information associated with the quotes.
            var numberFilesFound: Int = 0
            var numberQuotesFound: Int = 0
            var numberQuotesSkipped: Int = 0
            var numberQuotesLoaded: Int = 0

            // Flags that the system is busy loading quote(s).
            _isImportingQuotes.value = true

            // Start by first extracting all the files from within the zipped folder (if any were found).
            val didExtractQuotes = QuotesFileHelper.extractZippedFileContentToTemp(context, fileUri)
            if (didExtractQuotes)
            {
                // TODO.


            }
            else
            {
                // Unable to extract files.
                _applicationViewModel.showUserMessage("Unable to extract files from zipped file. Are you sure there's files there?")
            }

            // Success or failure, finished.
            _isImportingQuotes.value = false

            /*


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
            */
        }
        // TODO.
    }

    //endregion

    /***************************************************************************************************************************************
     *      Private Methods
     **************************************************************************************************************************************/
    //region Private Methods

    //endregion
}