/*******************************************************************************************************************************************
 *           Source:    HouseRulesFileHelper.kt
 *      Description:    Provides helper functions for loading, reading from and saving the house rules file.
 ******************************************************************************************************************************************/
package com.tyler_hietanen.yugioh_companion.business.houserules

import android.content.Context
import android.net.Uri
import com.tyler_hietanen.yugioh_companion.business.settings.AppStorageConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader

object HouseRulesFileHelper
{
    /***************************************************************************************************************************************
     *      Public Methods
     **************************************************************************************************************************************/
    //region Public Methods

    /***************************************************************************************************************************************
     *           Method:    readHouseRulesContentFromUri
     *       Parameters:    None.
     *          Returns:    String?
     *                          - Contents of read. Set to null if issue occurred.
     *      Description:    Loads file content from a URI into a string. Works best with markdown files.
     **************************************************************************************************************************************/
    suspend fun readHouseRulesContentFromUri(context: Context, uri: Uri): String?
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

                // Check if string builder is empty (Empty house rules).
                if (stringBuilder.isEmpty())
                {
                    null
                }
                else
                {
                    stringBuilder.toString()
                }
            }
            catch (_: Exception)
            {
                null
            }
        }
    }

    /***************************************************************************************************************************************
     *           Method:    retrieveHouseRulesFromStorage
     *       Parameters:    context
     *          Returns:    String?
     *                          - Contents of the house rules file. Set to null if file does not exist.
     *      Description:    Attempts to grab the saved house rules content from app storage.
     **************************************************************************************************************************************/
    suspend fun retrieveHouseRulesFromStorage(context: Context): String?
    {
        // Get the House Rules file and verify that it actually exists.
        val houseRulesFile = getHouseRulesFile(context)
        return if (houseRulesFile.exists() && (houseRulesFile.length() > 0))
        {
            // There is a file with something. Attempt read.
            readHouseRulesContentFromUri(context, Uri.fromFile(houseRulesFile))
        }
        else
        {
            null
        }
    }

    /***************************************************************************************************************************************
     *           Method:    saveHouseRulesToStorage
     *       Parameters:    context
     *                      content
     *                          - The string content (actual file content, not the file name) of the house rules file. Markdown.
     *          Returns:    Boolean
     *                          - Success (true) or failure (false) of saving.
     *      Description:    Attempts to save passed house rules to app storage.
     **************************************************************************************************************************************/
    suspend fun saveHouseRulesToStorage(context: Context, content: String): Boolean
    {
        return withContext(Dispatchers.IO)
        {
            try
            {
                // Grab existing file and start write.
                val houseRulesFile = getHouseRulesFile(context)
                FileOutputStream(houseRulesFile).use { outputStream ->
                    outputStream.write(content.toByteArray())
                }
                true
            }
            catch (_: Exception)
            {
                false
            }
        }
    }

    //endregion

    /***************************************************************************************************************************************
     *      Private Methods
     **************************************************************************************************************************************/
    //region Private Methods

    /***************************************************************************************************************************************
     *           Method:    getHouseRulesFile
     *       Parameters:    context
     *          Returns:    File
     *                          - Retrieved file.
     *      Description:    Gets the file for the saved house rules.
     *             Note:    Responsibility of the caller to check if file is empty or exists.
     **************************************************************************************************************************************/
    private fun getHouseRulesFile(context: Context): File
    {
        // Look for directory that house rules is stored in.
        val houseRulesDirectory = File(context.filesDir, AppStorageConstants.HOUSE_RULES_DIRECTORY)
        if (!houseRulesDirectory.exists())
        {
            // Folder doesn't exist. Create it.
            houseRulesDirectory.mkdirs()
        }

        // Attempt sourcing file.
        return File(houseRulesDirectory, AppStorageConstants.HOUSE_RULES_FILE_NAME)
    }

    // endregion
}