/*******************************************************************************************************************************************
 *           Source:    WelcomeScreen.kt
 *      Description:    Contains content for the Welcome Screen.
 ******************************************************************************************************************************************/
package com.tyler_hietanen.yugioh_companion.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.tyler_hietanen.yugioh_companion.navigation.ApplicationNavigationHost
import com.tyler_hietanen.yugioh_companion.navigation.Destination
import com.tyler_hietanen.yugioh_companion.presentation.ApplicationViewModel
import com.tyler_hietanen.yugioh_companion.ui.theme.Typography

object WelcomeScreen
{
    /***************************************************************************************************************************************
     *      Enum + Class Definitions
     **************************************************************************************************************************************/
    //region Enum + Class Definitions

    // Defines a shared types used for navigation card items.
    data class NavCardItem(
        // Text for card.
        val titleText: String,

        // Icon used to represent selected state.
        @DrawableRes
        val buttonIcon: Int,

        // On Click callback.
        val onClick: () -> Unit
    )

    //endregion

    /***************************************************************************************************************************************
     *      Constants
     **************************************************************************************************************************************/
    //region Constants

    // Title text for the welcome screen.
    private const val WELCOME_SCREEN_TITLE = "Welcome to the Yu-Gi-Oh Companion!"

    // Body text for the welcome screen.
    private const val WELCOME_SCREEN_BODY = "To begin, select one of the following button(s) to jump to this app's content."

    //endregion

    /***************************************************************************************************************************************
     *      (Public) Composable Methods
     **************************************************************************************************************************************/
    //region (Public) Composable Methods

    /***************************************************************************************************************************************
     *           Method:    DrawScreen
     *       Parameters:    navController
     *                          - Nav host controller.
     *                      applicationViewModel
     *                          - View model for application.
     *          Returns:    None.
     *      Description:    Composable function manages drawing the Welcome screen.
     **************************************************************************************************************************************/
    @Composable
    fun DrawScreen(navController: NavHostController, applicationViewModel: ApplicationViewModel)
    {
        // Defines list of nav card items.
        val navCardItems: List<NavCardItem> = listOf(
            NavCardItem(Destination.QUOTES.title, Destination.QUOTES.unselectedIcon!!) {
                ApplicationNavigationHost.navigateToSingleNewScreen(
                    navController,
                    Destination.QUOTES,
                    applicationViewModel) },
            NavCardItem(Destination.DUEL.title, Destination.DUEL.unselectedIcon!!) {
                ApplicationNavigationHost.navigateToSingleNewScreen(
                    navController,
                    Destination.DUEL,
                    applicationViewModel) },
            NavCardItem(Destination.HOUSERULES.title, Destination.HOUSERULES.unselectedIcon!!) {
                ApplicationNavigationHost.navigateToSingleNewScreen(
                    navController,
                    Destination.HOUSERULES,
                    applicationViewModel) },
            NavCardItem(Destination.SETTINGS.title, Destination.SETTINGS.unselectedIcon!!) {
                ApplicationNavigationHost.navigateToSingleNewScreen(
                    navController,
                    Destination.SETTINGS,
                    applicationViewModel) },
        )

        // Actually draw.
        Column {
            Text(text = WELCOME_SCREEN_TITLE,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
                )
            HorizontalDivider(modifier = Modifier)
            Text(text = WELCOME_SCREEN_BODY,
                style = Typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp))
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row (modifier = Modifier) {
                    Column (modifier = Modifier.weight(1f)){
                        NavCardComposable(navCardItems[0])
                        NavCardComposable(navCardItems[2])
                    }
                    Column (modifier = Modifier.weight(1f)){
                        NavCardComposable(navCardItems[1])
                        NavCardComposable(navCardItems[3])
                    }
                }
            }
        }
    }

    //endregion

    /***************************************************************************************************************************************
     *      (Private) Composable Methods
     **************************************************************************************************************************************/
    //region (Private) Composable Methods

    /***************************************************************************************************************************************
     *           Method:    NavCardComposable
     *       Parameters:    cardItem
     *                          - Item used to create a navigation card.
     *          Returns:    None.
     *      Description:    Composable function manages drawing a Navigation Card.
     **************************************************************************************************************************************/
    @Composable
    private fun NavCardComposable(cardItem: NavCardItem)
    {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .aspectRatio(1f)
                .clickable { cardItem.onClick() },
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = cardItem.buttonIcon),
                    contentDescription = "",
                    modifier = Modifier.size(96.dp)
                )
                Text(
                    text = cardItem.titleText,
                    style = Typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    //endregion
}