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
import okio.IOException
import org.jaudiotagger.audio.AudioFile
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.Tag
import org.jaudiotagger.tag.id3.AbstractID3v2Frame
import org.jaudiotagger.tag.id3.framebody.FrameBodyTXXX
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.zip.ZipInputStream

object QuotesFileHelper
{
    /***************************************************************************************************************************************
     *      Constants
     **************************************************************************************************************************************/
    //region Constants

    // Audio file format.
    private const val AUDIO_FILE_FORMAT_EXTENSION = "wav"

    // Tag field name (Used for custom type).
    private const val AUDIO_FILE_TAG_FIELD_NAME = "TXXX"

    // Custom quote tags.
    private const val TAG_NAME_EPISODE_NUMBER = "EPISODE_NUMBER"
    private const val TAG_NAME_EPISODE_TITLE = "EPISODE_TITLE"
    private const val TAG_NAME_QUOTE_TEXT = "QUOTE_TEXT"
    private const val TAG_NAME_TAGS = "TAGS"

    // Tag delimiter.
    private const val TAGS_DELIMITER = ','

    //endregion

    /***************************************************************************************************************************************
     *      Public Methods
     **************************************************************************************************************************************/
    //region Public Methods

    /***************************************************************************************************************************************
     *           Method:    extractAudioFilesFromZippedFolder
     *       Parameters:    content
     *                      fileUri
     *          Returns:    Boolean
     *                          - Whether at least one file was copied (true) or not (false).
     *      Description:    Extracts the contents of a folder (root-level files) to a temporary folder in the app space.
     *             Note:    requestFileConsolidation() should be called once this is complete (and valid), in order to actually import a
     *                      list of quotes.
     **************************************************************************************************************************************/
    suspend fun extractAudioFilesFromZippedFolder(context: Context, fileUri: Uri): Boolean
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
     *           Method:    requestQuoteConsolidation
     *       Parameters:    doImportNew
     *                          - Whether a new package should be used.
     *                      context
     *          Returns:    List<Quote>
     *                          - List of quotes that have been imported.
     *      Description:    Requests a list of quotes for the application.
     *             Note:    Calling this with a doImportNew of true will delete all files internally, once some have been imported, and set
     *                      the new list with the files imported from the package.
     *                      Should only be called with true if files have already been extracted.
     **************************************************************************************************************************************/
    fun requestQuoteConsolidation(doImportNew: Boolean, context: Context): List<Quote>
    {
        // Determines how to handle.
        return if (doImportNew)
        {
            // Needs to consolidate the import directory.
            consolidateImportPackage(context)
        }
        else
        {
            // No need to import new files. Just returns a list of quotes that match the currently-saved quotes.
            consolidateInternalDirectory(context)
        }
    }

    /***************************************************************************************************************************************
     *           Method:    getQuoteAbsolutePath
     *       Parameters:    quote
     *                          - The quote to get the path to.
     *                      context
     *          Returns:    String?
     *                          - Absolute file path (Set to null if not found).
     *      Description:    Gets the absolute file path to the quote.
     **************************************************************************************************************************************/
    fun getQuoteAbsolutePath(quote: Quote, context: Context): String?
    {
        // Default path, set to an empty string.
        var path = ""

        // Find the absolute path.
        val internalDirectory = File(context.filesDir, AppStorageConstants.QUOTES_DIRECTORY)
        if (internalDirectory.exists())
        {
            // Attempt to get the file.
            val quoteFile = File(internalDirectory, quote.quoteFileName)
            if (quoteFile.exists())
            {
                path = quoteFile.absolutePath
            }
        }

        return path
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

    /***************************************************************************************************************************************
     *           Method:    copyAllFiles
     *       Parameters:    sourceDirectory
     *                      targetDirectory
     *          Returns:    None.
     *      Description:    Copies all files from source directory to target directory.
     *             Note:    Does not copy nested directories.
     **************************************************************************************************************************************/
    private fun copyAllFiles(sourceDirectory: File, targetDirectory: File)
    {
        // Create target directory (if it doesn't already exist).
        if (!targetDirectory.exists())
        {
            targetDirectory.mkdirs()
        }

        // Validate.
        var isSafe = true
        isSafe = isSafe && sourceDirectory.isDirectory
        isSafe = isSafe && sourceDirectory.exists()
        isSafe = isSafe && targetDirectory.isDirectory
        isSafe = isSafe && targetDirectory.exists()
        if (!isSafe)
        {
            throw IOException("Cannot copy files. Directory(s) do no exist.")
        }

        // Assumes safety. Start by getting a list of files to copy (Only copies files, not directories).
        val filesToCopy = sourceDirectory.listFiles { file -> file.isFile}
        filesToCopy?.forEach { file ->
            val targetCopyFile = File(targetDirectory, file.name)
            try
            {
                Files.copy(file.toPath(), targetCopyFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
            }
            catch (_: IOException)
            {
                throw IOException("Unable to copy a file. Something went wrong.")
            }
        }
        // Else not considered. No files to copy, already good to go.
    }

    /***************************************************************************************************************************************
     *           Method:    consolidateImportDirectory
     *       Parameters:    context
     *          Returns:    List<Quote>
     *                          - New consolidated list of quotes. Set as empty list if no files were imported, otherwise set as the new
     *                          source of truth when it comes to a list of quotes.
     *      Description:    Manages importing new quotes (if they exist) and consolidating them with the quotes stored within the app.
     **************************************************************************************************************************************/
    private fun consolidateImportPackage(context: Context): List<Quote>
    {
        // List of quotes, to be returned. Setup as empty list to start.
        var newQuoteList: List<Quote> = mutableListOf()

        // Start by trimming non-audio files.
        if (trimForAudioFiles(context) > 0)
        {
            // Extracting from temporary directory. Get file for it.
            val directory = File(context.filesDir, AppStorageConstants.TEMP_DIRECTORY)

            // Source new list of quotes and check for contents.
            val quoteList = extractQuotes(directory)
            if (quoteList.isNotEmpty())
            {
                // There's new quotes. Go ahead and delete all saved quotes (in quotes folder).
                val file = File(context.filesDir, AppStorageConstants.QUOTES_DIRECTORY)
                deleteFilesRecursively(file, false)

                // Now copy all from temporary folder.
                val sourceDirectory = File(context.filesDir, AppStorageConstants.TEMP_DIRECTORY)
                val targetDirectory = File(context.filesDir, AppStorageConstants.QUOTES_DIRECTORY)
                copyAllFiles(sourceDirectory, targetDirectory)

                // If it got this far, set return list.
                newQuoteList = quoteList.toList()
            }
        }
        // Else not considered. Returns empty list by default.

        return newQuoteList
    }

    /***************************************************************************************************************************************
     *           Method:    consolidateInternalDirectory
     *       Parameters:    context
     *          Returns:    List<Quote>
     *                          - List of quotes loaded from the quotes directory.
     *      Description:    Creates a list of quotes from already-imported audio files.
     **************************************************************************************************************************************/
    private fun consolidateInternalDirectory(context: Context): List<Quote>
    {
        // Check internal directory.
        val internalDirectory = File(context.filesDir, AppStorageConstants.QUOTES_DIRECTORY)

        // Return list of extracted quotes.
        return extractQuotes(internalDirectory)
    }

    /***************************************************************************************************************************************
     *           Method:    trimForAudioFiles
     *       Parameters:    context
     *          Returns:    Int
     *                          - Number of (audio) files within the temp folder.
     *      Description:    Trims the temp folder, deleting any non-audio files. Returns a count of files remaining.
     *             Note:    Should be called only after extracting from a zipped folder.
     **************************************************************************************************************************************/
    private fun trimForAudioFiles(context: Context): Int
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


    /***************************************************************************************************************************************
     *           Method:    extractQuotes
     *       Parameters:    content
     *          Returns:    List<Quote>
     *                          - List of extracted quotes (may be empty).
     *      Description:    Generates a list of quotes based on the files from the temp folder.
     *             Note:    Should be called only after extracting from a zipped folder. Does not actually remove files from folder.
     **************************************************************************************************************************************/
    private fun extractQuotes(directory: File): List<Quote>
    {
        // Create new list of quotes to contain extracted ones.
        val extractedQuotes = mutableListOf<Quote>()
        if (directory.exists())
        {
            // Able to access directory. Start by iterating through every available file within the directory (Assumes it was cleaned first).
            val fileList = directory.listFiles()
            var uniqueQuoteNumber = 0
            fileList?.forEach { file ->
                // Attempt to get a quote from said file.
                val quote = generateQuote(file)
                if (quote != null)
                {
                    // This is a valid quote. Add quote ID and add to building list.
                    quote.quoteID = uniqueQuoteNumber
                    uniqueQuoteNumber++
                    extractedQuotes.add(quote)
                }
                else
                {
                    // Not a valid quote. Delete it.
                    file.delete()
                }
            }
        }
        // Else not needed. Will return empty list of quotes.

        return extractedQuotes.toList()
    }

    /***************************************************************************************************************************************
     *           Method:    generateQuote
     *       Parameters:    file
     *          Returns:    Quote?
     *                          - Generated quote (if it exists).
     *      Description:    Reads a potential file to check if it contains metadata. Generates an appropriate quote object.
     **************************************************************************************************************************************/
    private fun generateQuote(file: File): Quote?
    {
        // Setup return quote. Set to null, for now.
        var returnQuote: Quote? = null

        // Setup working variables.
        val metaDataTagsList = mutableListOf<AudioFileMetadataField>()

        // Make sure that the file actually exists.
        if (file.exists())
        {
            // Read the file, and check to see if there are any tags.
            val audioFile: AudioFile = AudioFileIO.read(file)
            val tag: Tag? = audioFile.tag
            if (tag != null)
            {
                // Start iterating through all frames in the tag, attempting to find custom identifiers for meta data (TXXX).
                val frameIterator: Iterator<*> = tag.fields
                while (frameIterator.hasNext())
                {
                    val frame = frameIterator.next()

                    // Verify frame is a ID3v2 frame.
                    if ((frame is AbstractID3v2Frame) && (frame.identifier == AUDIO_FILE_TAG_FIELD_NAME))
                    {
                        // Get the body of the frame.
                        val frameBody = frame.body
                        if (frameBody is FrameBodyTXXX)
                        {
                            // Create new meta data object with new information, and add to list.
                            metaDataTagsList.add(
                                AudioFileMetadataField(
                                    description = frameBody.description,
                                    value = frameBody.text
                                )
                            )
                        }
                    }
                }
            }
        }

        // Check to see if there was metadata found (not empty). If there is, then it's likely a quote. Start building it.
        if (metaDataTagsList.isNotEmpty())
        {
            // Create quote instance.
            returnQuote = Quote(quoteFileName = file.name)

            // TODO Create a quote title, based on the name.

            // Loop through every bit of meta-data, assigning appropriately.
            for (metadataField in metaDataTagsList)
            {
                when (metadataField.description)
                {
                    // Episode number, title and text can just be copied over directly.
                    TAG_NAME_EPISODE_NUMBER -> returnQuote.quoteSource = metadataField.value
                    TAG_NAME_EPISODE_TITLE -> returnQuote.quoteEpisodeNumber = metadataField.value
                    TAG_NAME_QUOTE_TEXT -> returnQuote.quoteText = metadataField.value

                    // Tags is a bit more complex.
                    TAG_NAME_TAGS ->
                    {
                        // Create a cleaned up list of tags from the value, split based off of delimiter and removing extra spaces.
                        val tagsList: List<String> = metadataField.value.split(TAGS_DELIMITER)
                            .map { it.trim() }
                            .filter { it.isNotEmpty() }

                        if (tagsList.isNotEmpty())
                        {
                            returnQuote.tags = tagsList.toList()
                        }
                    }

                    else ->
                    {
                        // Unknown tag type. Ignored entirely.
                    }
                }
            }
        }

        return returnQuote
    }

    //endregion
}