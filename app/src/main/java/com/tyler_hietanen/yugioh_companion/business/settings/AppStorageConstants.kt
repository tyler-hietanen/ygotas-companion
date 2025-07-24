/*******************************************************************************************************************************************
 *           Source:    AppStorageConstants.kt
 *      Description:    Contains constants (directories and file names) for files stored by this application.
 ******************************************************************************************************************************************/
package com.tyler_hietanen.yugioh_companion.business.settings

object AppStorageConstants
{
    /***************************************************************************************************************************************
     *      Constants
     **************************************************************************************************************************************/
    //region Constants

    // Defines structure and related files used for House Rules.
    const val HOUSE_RULES_DIRECTORY = "house_rules"
    const val HOUSE_RULES_FILE_NAME = "custom_house_rules.md"

    // Defines structure and related file used for Quotes.
    // Note: File names are the same as the files used for the import.
    const val QUOTES_DIRECTORY = "quotes"

    // Folder used to share content.
    const val SHARE_DIRECTORY = "share"

    // Defines a working directory, used for temporary files (if needed).
    const val TEMP_DIRECTORY = "temp"

    //endregion
}