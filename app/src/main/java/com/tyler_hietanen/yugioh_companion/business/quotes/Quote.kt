/*******************************************************************************************************************************************
 *           Source:    Quote.kt
 *      Description:    Represents a single quote and information associated.
 ******************************************************************************************************************************************/
package com.tyler_hietanen.yugioh_companion.business.quotes

data class Quote(
    // Quote File name.
    var quoteFileName: String,

    // Source.
    var quoteSource: String? = null,

    // Quote text.
    var quoteText: String? = null,

    // Tags.
    var tags: List<String> = emptyList()
) {
    /***************************************************************************************************************************************
     *      Methods
     **************************************************************************************************************************************/
    //region Methods

    /***************************************************************************************************************************************
     *           Method:    addTag
     *       Parameters:    tag
     *          Returns:    None.
     *      Description:    Adds a tag to the list of tags for this quote (if it doesn't already exist).
     **************************************************************************************************************************************/
    fun addTag(tag: String)
    {
        // Only add if not already present.
        if (!tags.contains(tag))
        {
            // Extract current list of tags and add to it.
            val newList = tags.toMutableList()
            newList.add(tag)

            // Set value.
            tags = newList
        }
    }

    //endregion
}