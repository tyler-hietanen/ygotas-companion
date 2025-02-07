package com.tyler_hietanen.ygotas_companion.android

/*******************************************************************************************************************************************
 *           Source:    Connect.kt
 *
 *      Description:    This files give the rest of the application (Primarily those in the Domain layers) access to the application
 *                      context, which may be needed for various resources or linking.
 ******************************************************************************************************************************************/
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class YgotasCompanion: Application()
{
    // Allows access to context.
    companion object
    {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    /***************************************************************************************************************************************
     *      Overridden Functions
     **************************************************************************************************************************************/
    //region Overridden Functions

    /***************************************************************************************************************************************
     *      Function:   onCreate
     *    Parameters:   None.
     *       Returns:   None.
     *   Description:   The overridden onCreate function, called by the Android OS when this is created.
     *          Note:   This should be called everytime the application itself is created and is the best means of knowing when this
     *                  application "exists".
     **************************************************************************************************************************************/
    override fun onCreate()
    {
        super.onCreate()

        context = this
    }

    //endregion
}