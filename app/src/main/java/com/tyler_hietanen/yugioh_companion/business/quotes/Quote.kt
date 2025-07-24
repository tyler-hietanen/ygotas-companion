/*******************************************************************************************************************************************
 *           Source:    Quote.kt
 *      Description:    Represents a single quote and information associated. Used for both data and view visibility.
 ******************************************************************************************************************************************/
package com.tyler_hietanen.yugioh_companion.business.quotes

data class Quote(
    // (Unique) Quote ID. Used for easier accessing by other modules, referencing which one was 'selected'.
    var quoteID: Int = 0,

    // Quote File name.
    var quoteFileName: String,

    // Quote Friendly name.
    var quoteFriendlyName: String,

    // Source (Episode name, video, etc).
    var quoteSource: String? = null,

    // Episode number (Set as number, or as a sequence of characters.).
    var quoteEpisodeNumber: String? = null,

    // Quote text.
    var quoteText: String? = null,

    // Tags.
    var tags: List<String> = emptyList(),

    // Whether this item is expanded (true) or not (false).
    var isExpanded: Boolean = false,

    // Whether this item is playing (true) or paused (false).
    var isPlaying: Boolean = false,
)