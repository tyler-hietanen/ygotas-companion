/*******************************************************************************************************************************************
 *           Source:    ApplicationViewModel.kt
 *      Description:    The ViewModel for the application, being used for a general state of the application.
 ******************************************************************************************************************************************/
package com.tyler_hietanen.yugioh_companion.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tyler_hietanen.yugioh_companion.navigation.Destination
import com.tyler_hietanen.yugioh_companion.presentation.viewmodels.DuelViewModel
import com.tyler_hietanen.yugioh_companion.presentation.viewmodels.HouseRulesViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ApplicationViewModel: ViewModel()
{
    /***************************************************************************************************************************************
     *      Properties
     **************************************************************************************************************************************/
    //region Properties

    // ViewModel(s) instances, used to simplify access to these view models by other modules (mainly compose functions)
    // Note: It is imperative that these are set, by the appropriate reference setters, before they're accessed by any other components.
    lateinit var duelViewModel: DuelViewModel
    lateinit var houseRulesViewModel: HouseRulesViewModel

    // The current destination of the application.
    // Note: This is meant to be an observable property for other ViewModels or View components, to allow them to reflect based upon the
    // current destination of the application. However, do not navigate by changing this value. This value should only be changed by a call
    // to the ApplicationNavigationHost.navigateTo() method.
    private val _currentDestination = mutableStateOf(Destination.WELCOME)
    val currentDestination: State<Destination> = _currentDestination

    // Whether navigation is allowed by the application.
    private val _doEnableNavigation = mutableStateOf(true)
    val doEnableNavigation: State<Boolean> = _doEnableNavigation

    // Custom message shown to user.
    private val _customMessages = Channel<String>()
    val customMessages = _customMessages.receiveAsFlow()

    //endregion

    /***************************************************************************************************************************************
     *      Methods
     **************************************************************************************************************************************/
    //region Methods

    /***************************************************************************************************************************************
     *           Method:    setDuelistViewModelReference
     *       Parameters:    duelViewModel
     *                          - (Duelist) ViewModel.
     *          Returns:    None.
     *      Description:    Sets the reference to the Duelist View Model object.
     **************************************************************************************************************************************/
    fun setDuelistViewModelReference(duelViewModel: DuelViewModel)
    {
        this.duelViewModel = duelViewModel
    }

    /***************************************************************************************************************************************
     *           Method:    setHouseRulesViewModelReference
     *       Parameters:    houseRulesViewModel
     *                          - (House Rules) ViewModel.
     *          Returns:    None.
     *      Description:    Sets the reference to the House Rules View Model object.
     **************************************************************************************************************************************/
    fun setHouseRulesViewModelReference(houseRulesViewModel: HouseRulesViewModel)
    {
        this.houseRulesViewModel = houseRulesViewModel
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

    /***************************************************************************************************************************************
     *           Method:    showUserMessage
     *       Parameters:    message
     *                          - Message to be shown.
     *          Returns:    None.
     *      Description:    Sets a custom user message to be shown.
     **************************************************************************************************************************************/
    fun showUserMessage(message: String)
    {
        viewModelScope.launch {
            _customMessages.send(message)
        }
    }

    //endregion
}