/*******************************************************************************************************************************************
 *           Source:    MainActivityViewModel.kt
 *      Description:    The ViewModel for the MainActivity, being used for a general state view model.
 ******************************************************************************************************************************************/
package com.tyler_hietanen.ygotascompanion.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tyler_hietanen.ygotascompanion.navigation.Destination

class MainActivityViewModel: ViewModel()
{
    /***************************************************************************************************************************************
     *      Properties
     **************************************************************************************************************************************/
    //region Properties

    // The current destination of the application.
    // Note: This is meant to be an observable property for other ViewModels or View components, to allow them to reflect based upon the
    // current destination of the application. However, do not navigate by changing this value. This value should only be changed by a call
    // to the ApplicationNavigationHost.navigateTo() method.
    private val _currentDestination = MutableLiveData(Destination.WELCOME)
    val currentDestination: LiveData<Destination> = _currentDestination

    //endregion

    /***************************************************************************************************************************************
     *      Methods
     **************************************************************************************************************************************/
    //region Methods

    /***************************************************************************************************************************************
     *           Method:    isNavigatedTo
     *       Parameters:    destination
     *                          - Current destination.
     *          Returns:    None.
     *      Description:    Sets the current application destination.
     **************************************************************************************************************************************/
    fun isNavigatedTo(destination: Destination)
    {
        _currentDestination.postValue(destination)
    }

    //endregion
}