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

    // Exposes a list of (filtered) quotes to be observed. If this is an empty list, then there are no quotes loaded into the application.
    private val _filteredQuoteList = mutableStateListOf<Quote>()
    val filteredQuoteList: List<Quote> = _filteredQuoteList

    // Current search query (Basic text).
    // While this should be the source of truth, to be displayed in search menu, it is only updated when the View calls the
    // onSearchQueryChanged method.
    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    //endregion

    /***************************************************************************************************************************************
     *      Fields
     **************************************************************************************************************************************/
    //region Fields

    // Stores reference to application view model for later usage.
    private lateinit var _applicationViewModel: ApplicationViewModel

    // Stores the latest (full, not filtered) list of quotes.
    private var _listOfQuotes: MutableList<Quote> = mutableListOf()

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
        val loadedQuoteList: List<Quote> = QuotesFileHelper.requestQuoteConsolidation(false, context)
        if (loadedQuoteList.isNotEmpty())
        {
            // Copy over internal values to source of truth.
            _listOfQuotes.clear()
            _listOfQuotes.addAll(loadedQuoteList)

            // Also copy to view source.
            _filteredQuoteList.clear()
            _filteredQuoteList.addAll(loadedQuoteList)
        }

        // Set values to default states.
        _isImportingQuotes.value = false
        _searchQuery.value = ""
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
            // Flags that the system is busy loading quote(s).
            _isImportingQuotes.value = true

            // Start by first extracting all the files from within the zipped folder (if any were found).
            var didExtractQuotes = QuotesFileHelper.extractAudioFilesFromZippedFolder(context, fileUri)
            if (didExtractQuotes)
            {
                // Requests a consolidation of files.
                // Note: This may take considerable time.
                val newQuoteList: List<Quote> = QuotesFileHelper.requestQuoteConsolidation(true, context)

                // Check for success at import.
                didExtractQuotes = newQuoteList.isNotEmpty()
                if (didExtractQuotes)
                {
                    // Copy over internal values to source of truth.
                    _listOfQuotes.clear()
                    _listOfQuotes.addAll(newQuoteList)

                    // Also copy to view source.
                    _filteredQuoteList.clear()
                    _filteredQuoteList.addAll(newQuoteList)

                    // Let user know.
                    _applicationViewModel.showUserMessage("New import package accepted and merged. Enjoy!")
                }
            }

            // Check for failure.
            if (!didExtractQuotes)
            {
                // Unable to extract files.
                _applicationViewModel.showUserMessage("Unable to import new quotes. Are you sure valid files were selected?")
            }

            // Success or failure, finished. Reset variables.
            _isImportingQuotes.value = false
            _searchQuery.value = ""
        }
    }

    fun onSearchQueryChanged(query: String)
    {

    }

    //endregion
}