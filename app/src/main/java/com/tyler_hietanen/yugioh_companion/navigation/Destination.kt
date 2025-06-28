/*******************************************************************************************************************************************
 *           Source:    Destination.kt
 *      Description:    Defines a top-level navigation destination (or screen) for the application.
 ******************************************************************************************************************************************/
package com.tyler_hietanen.yugioh_companion.navigation

import com.tyler_hietanen.yugioh_companion.R

enum class Destination
(
    // Unique identifier used to 'type' a destination. These values must be unique amongst all different destinations/screens.
    val routeID: String,

    // String title, text used for said destination.
    val title: String,

    // Icon used to represent selected state.
    @androidx.annotation.DrawableRes
    val selectedIcon: Int? = R.drawable.question_mark_outlined,

    // Icon used to represent unselected state.
    @androidx.annotation.DrawableRes
    val unselectedIcon: Int? = R.drawable.question_mark_outlined,
)
{
    // Defines all top level destinations. All possible destinations should be defined here.
    WELCOME("Welcome", "Welcome"),
    QUOTES("Quotes", "Quotes", R.drawable.library_music_filled, R.drawable.library_music_outlined),
    DUEL("Duel", "Duel!", R.drawable.swords_filled, R.drawable.swords_outlined),
    HOUSERULES("HouseRules", "House Rules", R.drawable.home_filled, R.drawable.home_outlined),
    SETTINGS("Settings", "Settings", R.drawable.settings_filled, R.drawable.settings_outlined)
}