/*******************************************************************************************************************************************
 *           Source:    AppSettingsRepository.kt
 *      Description:    A repository containing all of the app's general settings.
 ******************************************************************************************************************************************/
package com.tyler_hietanen.yugioh_companion.business.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map

// Creates a top-level property delegate for this app's preference file.
val Context.appSettingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "app_settings")

class AppSettingsRepository(private val context: Context)
{
    /***************************************************************************************************************************************
     *      Constants
     **************************************************************************************************************************************/
    //region Constants

    // Defines preference keys for every setting. It is important that for each setting in the AppSettings, there's an appropriate setting.
    private companion object
    {
        val IS_SNARK_ENABLED_KEY = booleanPreferencesKey("is_snark_enabled")
        val IS_MOCKING_ENABLED_KEY = booleanPreferencesKey("is_mocking_enabled")
    }

    //endregion

    /***************************************************************************************************************************************
     *      Properties
     **************************************************************************************************************************************/
    //region Properties

    // Flow used to observe the app's current settings.
    val appSettingsFlow: Flow<AppSettings> = context.appSettingsDataStore.data
        .catch { exception ->
            if (exception is IOException)
            {
                emit(emptyPreferences())
            }
            else
            {
                throw exception
            }
        }
        .map { preferences ->
            // Copy out current settings.
            val isSnarkEnabled = preferences[IS_SNARK_ENABLED_KEY] ?: true
            val isMockEnabled = preferences[IS_MOCKING_ENABLED_KEY] ?: true

            // Set settings.
            AppSettings(
                isSnarkEnabled = isSnarkEnabled,
                isMockingEnabled = isMockEnabled)
        }

    //endregion

    /***************************************************************************************************************************************
     *      Public Methods
     **************************************************************************************************************************************/
    //region Public Methods

    /***************************************************************************************************************************************
     *           Method:    getCurrentAppSettings
     *       Parameters:    None.
     *          Returns:    AppSettings
     *                          - Current app settings value.
     *      Description:    Gathers the current app settings.
     **************************************************************************************************************************************/
    suspend fun getCurrentAppSettings(): AppSettings
    {
        return appSettingsFlow.first()
    }

    /***************************************************************************************************************************************
     *           Method:    saveAppSettings
     *       Parameters:    newSettings
     *                          - New settings value.
     *          Returns:    None.
     *      Description:    Updates the app settings saved in memory, thereby causing the exposed flow to update (Updating UI).
     **************************************************************************************************************************************/
    suspend fun saveAppSettings(newSettings: AppSettings)
    {
        // Update value stored in memory. This should cause local change too.
        context.appSettingsDataStore.edit { preferences ->
            preferences[IS_SNARK_ENABLED_KEY] = newSettings.isSnarkEnabled
            preferences[IS_MOCKING_ENABLED_KEY] = newSettings.isMockingEnabled
        }
    }

    //endregion
}