/*******************************************************************************************************************************************
 *           Source:    QuotesViewModel.kt
 *      Description:    The ViewModel for the quotes experience, managing the import, viewing and listening of quotes.
 ******************************************************************************************************************************************/
package com.tyler_hietanen.yugioh_companion.presentation.viewmodels

import android.content.Context
import android.media.MediaPlayer
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

    //endregion

    /***************************************************************************************************************************************
     *      Fields
     **************************************************************************************************************************************/
    //region Fields

    // Stores reference to application view model for later usage.
    private lateinit var _applicationViewModel: ApplicationViewModel

    // Stores the latest (full, not filtered) list of quotes.
    private var _listOfQuotes: MutableList<Quote> = mutableListOf()

    // Tracks the active quote (Being played).
    private var _activeQuote: Quote? = null

    // Tracks the active media player (to allow for pausing/release).
    private var _mediaPlayer: MediaPlayer = MediaPlayer()

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
            updateFilteredQuotes(loadedQuoteList)
        }

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
                    updateFilteredQuotes(newQuoteList)

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
        }
    }

    /***************************************************************************************************************************************
     *           Method:    onPlayQuote
     *       Parameters:    quote
     *                      context
     *          Returns:    None.
     *      Description:    Plays the selected quote. Stops a currently-playing one if there is one.
     **************************************************************************************************************************************/
    fun onPlayQuote(quote: Quote, context: Context)
    {
        // Check on current quote.
        val quoteCopy = _activeQuote?.copy()
        if (_activeQuote != null)
        {
            // There's an active quote, or this is the same quote. Stops it by freeing resources.
            resetMediaPlayer()
        }

        // Only continue playing if the quote selected is different than the current quote.
        if (quoteCopy?.quoteID != quote.quoteID)
        {
            // Source the file path for the quote.
            val filePath = QuotesFileHelper.getQuoteAbsolutePath(quote, context)
            if (filePath != null)
            {
                // Valid path. Set quote.
                _activeQuote = quote

                // Flag that this quote is playing.
                for (n in _filteredQuoteList.indices)
                {
                    val currentQuote = _filteredQuoteList[n]
                    if (currentQuote.quoteID == quote.quoteID)
                    {
                        _filteredQuoteList[n] = currentQuote.copy(isPlaying = true)
                        break
                    }
                }

                // Let's try to play it.
                _mediaPlayer = MediaPlayer().apply {
                    try {
                        setDataSource(filePath)
                        prepareAsync()

                        setOnPreparedListener { mediaPlayer ->
                            try {
                                mediaPlayer.start()
                            } catch (e: IllegalStateException) {
                                resetMediaPlayer()
                            }
                        }

                        setOnCompletionListener {
                            resetMediaPlayer()
                        }

                        setOnErrorListener { _, what, extra ->
                            resetMediaPlayer()
                            true
                        }
                    } catch (e: Exception)
                    {
                        resetMediaPlayer()
                    }
                }
            }
        }
    }

    //endregion

    /***************************************************************************************************************************************
     *      Private Methods
     **************************************************************************************************************************************/
    //region Private Methods

    /***************************************************************************************************************************************
     *           Method:    resetMediaPlayer
     *       Parameters:    None.
     *          Returns:    None.
     *      Description:    Resets media player resources. Will also stop a currently-playing one.
     **************************************************************************************************************************************/
    private fun resetMediaPlayer()
    {
        // Attempt to stop playing.
        try
        {
            // Stop the current media player.
            _mediaPlayer.stop()

            // Attempt to reset the quote, if it can be found.
            for (n in _filteredQuoteList.indices)
            {
                val currentQuote = _filteredQuoteList[n]
                if (currentQuote.quoteID == _activeQuote?.quoteID)
                {
                    _filteredQuoteList[n] = currentQuote.copy(isPlaying = false)
                    break
                }
            }

            // Create a fresh media player instance.
            _mediaPlayer = MediaPlayer()

            // Reset quote.
            _activeQuote = null
        }
        catch (ex: Exception)
        {
            // Did it's best. Does nothing else.
        }
    }

    /***************************************************************************************************************************************
     *           Method:    updateFilteredQuotes
     *       Parameters:    listOfQuotes
     *          Returns:    None.
     *      Description:    Resets media player resources. Will also stop a currently-playing one.
     **************************************************************************************************************************************/
    private fun updateFilteredQuotes(listOfQuotes: List<Quote>)
    {
        // Copy over internal values to source of truth.
        _listOfQuotes.clear()
        _listOfQuotes.addAll(listOfQuotes)

        // Also copy to view source.
        _filteredQuoteList.clear()
        _filteredQuoteList.addAll(listOfQuotes)

        // Sort.
        _filteredQuoteList.sortBy { it.quoteSource }
    }

    //endregion
}