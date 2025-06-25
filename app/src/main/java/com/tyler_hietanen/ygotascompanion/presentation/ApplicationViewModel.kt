/*******************************************************************************************************************************************
 *           Source:    ApplicationViewModel.kt
 *      Description:    The ViewModel for the application, being used for a general state of the application.
 ******************************************************************************************************************************************/
package com.tyler_hietanen.ygotascompanion.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.tyler_hietanen.ygotascompanion.navigation.Destination

class ApplicationViewModel: ViewModel()
{
    /***************************************************************************************************************************************
     *      Properties
     **************************************************************************************************************************************/
    //region Properties

    // The current destination of the application.
    // Note: This is meant to be an observable property for other ViewModels or View components, to allow them to reflect based upon the
    // current destination of the application. However, do not navigate by changing this value. This value should only be changed by a call
    // to the ApplicationNavigationHost.navigateTo() method.
    private val _currentDestination = mutableStateOf(Destination.WELCOME)
    val currentDestination: State<Destination> = _currentDestination

    // Whether navigation is allowed by the application.
    private val _doEnableNavigation = mutableStateOf(true)
    val doEnableNavigation: State<Boolean> = _doEnableNavigation

    //endregion

    /***************************************************************************************************************************************
     *      Methods
     **************************************************************************************************************************************/
    //region Methods

    /***************************************************************************************************************************************
     *           Method:    applicationInitialization
     *       Parameters:    None.
     *          Returns:    None.
     *      Description:    Performs any necessary initialization, called upon the creation of the application.
     *             Note:    This should only be called once per application run, to avoid any duplication of code calls.
     **************************************************************************************************************************************/
    fun applicationInitialization()
    {
        // Initializes other ViewModel(s).




        // TODO.
    }

    /***************************************************************************************************************************************
     *           Method:    setCurrentDestination
     *       Parameters:    destination
     *                          - Current destination.
     *          Returns:    None.
     *      Description:    Sets the current application destination.
     **************************************************************************************************************************************/
    fun setCurrentDestination(destination: Destination)
    {
        _currentDestination.value = destination
    }

    /***************************************************************************************************************************************
     *           Method:    setDoEnableNavigation
     *       Parameters:    doEnable
     *                          - Whether navigation is enabled.
     *          Returns:    None.
     *      Description:    Determines whether navigation is enabled.
     *             Note:    Must be reset if it is toggled off. Does not actually block navigation.
     **************************************************************************************************************************************/
    fun setDoEnableNavigation(doEnable: Boolean)
    {
        _doEnableNavigation.value = doEnable
    }

    //endregion
}