/*******************************************************************************************************************************************
 *           Source:    AppSettings.kt
 *      Description:    Represents the current app settings state of the application.
 ******************************************************************************************************************************************/
package com.tyler_hietanen.yugioh_companion.business.settings

data class AppSettings(
    // Whether app's snarky behavior is allowed.
    val isSnarkEnabled: Boolean = true,

    // Whether the app can mock a user.
    val isMockingEnabled: Boolean = true
)