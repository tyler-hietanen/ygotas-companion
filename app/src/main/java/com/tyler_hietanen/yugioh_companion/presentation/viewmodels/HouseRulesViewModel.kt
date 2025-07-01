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
import com.tyler_hietanen.yugioh_companion.presentation.ApplicationViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

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
    fun initialize(applicationViewModel: ApplicationViewModel)
    {
        // Store reference to application view model.
        _applicationViewModel = applicationViewModel

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
        // Must be run from view model scope, since it requires context.
        viewModelScope.launch {
            // Obtain lock, letting user know it is busy (And preventing additional attempts, hopefully).
            _isImportingHouseRules.value = true

            // Grab the contents and check if it is not null (no issues occurred on read).
            val rulesContent = readFileContentFromUri(context, fileUri)
            if (rulesContent != null)
            {
                // TODO Save this to memory.
                // Set the current content.
                _houseRulesContent.value = rulesContent

                // If it reaches the end, it was successful. Show user message.
                _applicationViewModel.showUserMessage("Loaded house rules from selected file. Make sure to verify!")
            }
            else
            {
                // Failed to load or parse house rules.
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
     *           Method:    readFileContentFromUri
     *       Parameters:    None.
     *          Returns:    String?
     *                          - Contents of read. Set to null if issue occurred.
     *      Description:    Loads file content from a URI into a string. Works best with markdown files.
     **************************************************************************************************************************************/
    private suspend fun readFileContentFromUri(context: Context, uri: Uri): String?
    {
        return withContext(Dispatchers.IO)
        {
            try
            {
                // Setup string builder.
                val stringBuilder = StringBuilder()

                // Open input stream from selected file.
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    BufferedReader(InputStreamReader(inputStream)).use { reader ->
                        var line: String?
                        while (reader.readLine().also { line = it } != null)
                        {
                            stringBuilder.append(line).append('\n')
                        }
                    }
                }
                stringBuilder.toString()
            }
            catch (_: Exception)
            {
                null
            }
        }
    }

    //endregion
}