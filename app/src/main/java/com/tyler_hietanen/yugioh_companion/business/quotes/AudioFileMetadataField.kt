/*******************************************************************************************************************************************
 *           Source:    AudioFileMetadataField.kt
 *      Description:    Data class represents a single tag, discovered from an audio file. These are custom TXXX tags that are included in
 *                      potential quote metadata and used by this application to assign information.
 ******************************************************************************************************************************************/
package com.tyler_hietanen.yugioh_companion.business.quotes

data class AudioFileMetadataField(
    // Description (title, or name essentially).
    val description: String,

    // Value of the tag (body).
    val value: String
)