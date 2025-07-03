/*******************************************************************************************************************************************
 *           Source:    QuotesFileHelper.kt
 *      Description:    Provides helper functions for loading, reading from and saving quotes from a zipped file.
 ******************************************************************************************************************************************/
package com.tyler_hietanen.yugioh_companion.business.quotes

import android.content.Context
import android.net.Uri
import com.tyler_hietanen.yugioh_companion.business.settings.AppStorageConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipInputStream

object QuotesFileHelper
{
    /***************************************************************************************************************************************
     *      Constants
     **************************************************************************************************************************************/
    //region Constants

    // Audio file format.
    private const val AUDIO_FILE_FORMAT_EXTENSION = "wav"

    //endregion

    /***************************************************************************************************************************************
     *      Public Methods
     **************************************************************************************************************************************/
    //region Public Methods

    /***************************************************************************************************************************************
     *           Method:    extractZippedFileContentToTemp
     *       Parameters:    content
     *                      fileUri
     *          Returns:    Boolean
     *                          - Whether at least one file was copied (true) or not (false).
     *      Description:    Extracts the contents of a folder (root-level files) to the temp folder.
     **************************************************************************************************************************************/
    suspend fun extractZippedFileContentToTemp(context: Context, fileUri: Uri): Boolean
    {
        var didExtractAFile = false
        withContext(Dispatchers.IO)
        {
            // Before anything else, make sure that the temp directory exists.
            val tempDirectory = File(context.filesDir, AppStorageConstants.TEMP_DIRECTORY)
            if (!tempDirectory.exists())
            {
                tempDirectory.mkdirs()
            }
            else
            {
                // Clean up directory by deleting contents within it.
                deleteFilesRecursively(tempDirectory, false)
            }

            // Everything is ready for import. Open import stream for the zipped file.
            context.contentResolver.openInputStream(fileUri)?.use { inputStream ->
                ZipInputStream(inputStream).use { zis ->
                    // Check each zip entry.
                    var zipEntry = zis.nextEntry
                    while (zipEntry != null)
                    {
                        // Create file to contain copied content.
                        val newFile = File(tempDirectory, zipEntry.name)

                        if (!newFile.canonicalPath.startsWith(tempDirectory.canonicalPath + File.separator))
                        {
                            didExtractAFile = false
                            break
                        }

                        // Only handles if file is not a directory.
                        if (!newFile.isDirectory)
                        {
                            // Copy file content into new file designation.
                            FileOutputStream(newFile).use { fos ->
                                // Allocate buffer for read. And copy entire file contents.
                                val buffer = ByteArray(1024)
                                var len: Int
                                while (zis.read(buffer).also { len = it} > 0)
                                {
                                    fos.write(buffer, 0, len)
                                }
                            }

                            // At least one file found.
                            didExtractAFile = true
                        }

                        // Closes current entry, gets next entry.
                        zis.closeEntry()
                        zipEntry = zis.nextEntry
                    }
                }
            }
        }

        return didExtractAFile
    }

    /***************************************************************************************************************************************
     *           Method:    getTempFileCount
     *       Parameters:    content
     *          Returns:    Int
     *                          - Number of files within the temp folder.
     *      Description:    Counts the number of files within the temp folder.
     *             Note:    Should be called only after extracting from a zipped folder.
     **************************************************************************************************************************************/
    fun getTempFileCount(context: Context): Int
    {
        // Setup return.
        var fileCount: Int? = 0

        // Get file and attempt to source count.
        val tempDirectory = File(context.filesDir, AppStorageConstants.TEMP_DIRECTORY)
        if (tempDirectory.exists())
        {
            // Set file count to the list of files (if it can be sourced).
            fileCount = tempDirectory.listFiles()?.count()
        }

        return fileCount ?: 0
    }

    /***************************************************************************************************************************************
     *           Method:    trimForAudioFiles
     *       Parameters:    content
     *          Returns:    Int
     *                          - Number of (audio) files within the temp folder.
     *      Description:    Trims the temp folder, deleting any non-audio files. Returns a count of files remaining.
     *             Note:    Should be called only after extracting from a zipped folder.
     **************************************************************************************************************************************/
    fun trimForAudioFiles(context: Context): Int
    {
        // Setup return.
        var audioFileCount: Int = 0

        // Get source directory and start souring.
        val tempDirectory = File(context.filesDir, AppStorageConstants.TEMP_DIRECTORY)
        if (tempDirectory.exists())
        {
            // Get a list of files.
            val fileList = tempDirectory.listFiles()
            fileList?.forEach { file ->
                // Check extension.
                if (file.extension == AUDIO_FILE_FORMAT_EXTENSION)
                {
                    // Matching extension. Keep this file.
                    audioFileCount++
                }
                else
                {
                    // Doesn't match extension. Delete this file.
                    file.delete()
                }
            }
        }

        return audioFileCount
    }

    //endregion

    /***************************************************************************************************************************************
     *      Private Methods
     **************************************************************************************************************************************/
    //region Private Methods

    /***************************************************************************************************************************************
     *           Method:    deleteFilesRecursively
     *       Parameters:    fileOrFolder
     *                      deleteThis
     *                          - Whether this specific file/folder should be deleted.
     *          Returns:    None.
     *      Description:    Recursively deletes the file/folder and any nested files located within this directory.
     **************************************************************************************************************************************/
    private fun deleteFilesRecursively(fileOrFolder: File, deleteThis: Boolean)
    {
        // Check if this file is a directory, and if it is, loop through all contents.
        if (fileOrFolder.isDirectory)
        {
            // Loop through all files (or folders).
            val fileList = fileOrFolder.listFiles()
            fileList?.forEach { file ->
                deleteFilesRecursively(file, true)
            }
        }

        // Delete this (if requested).
        if (deleteThis)
        {
            fileOrFolder.delete()
        }
    }

    //endregion
}