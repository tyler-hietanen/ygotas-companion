/*******************************************************************************************************************************************
 *           Source:    SettingsScreen.kt
 *      Description:    Contains content for the Duel Screen.
 ******************************************************************************************************************************************/
package com.tyler_hietanen.yugioh_companion.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tyler_hietanen.yugioh_companion.R
import com.tyler_hietanen.yugioh_companion.presentation.ApplicationViewModel
import com.tyler_hietanen.yugioh_companion.ui.layout.CompanionButtons.IconTextButton
import androidx.core.net.toUri
import com.tyler_hietanen.yugioh_companion.ui.theme.Typography

object SettingsScreen
{
    /***************************************************************************************************************************************
     *      Constants
     **************************************************************************************************************************************/
    //region Constants

    // Github URL for this project.
    private const val PROJECT_GITHUB_URL = "https://github.com/tyler-hietanen/ygotas-companion"

    //endregion

    /***************************************************************************************************************************************
     *      (Public) Composable Methods
     **************************************************************************************************************************************/
    //region (Public) Composable Methods

    /***************************************************************************************************************************************
     *           Method:    DrawScreen
     *       Parameters:    applicationViewModel
     *          Returns:    None.
     *      Description:    Composable function manages drawing the Settings screen.
     **************************************************************************************************************************************/
    @Composable
    fun DrawScreen(applicationViewModel: ApplicationViewModel)
    {
        // Sets up variables and observation.
        val scrollState = rememberScrollState()
        val context = LocalContext.current

        // Actually draw.
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxWidth()
        ) {
            // Show large settings text - this page.
            Text(text = "Settings",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                style = Typography.titleLarge,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            HorizontalDivider(modifier = Modifier.padding(8.dp))

            // Github section (Text about project, link to project).
            GithubSection(context)
            HorizontalDivider(modifier = Modifier.padding(8.dp))
        }
    }

    //endregion

    /***************************************************************************************************************************************
     *      (Private) Composable Methods
     **************************************************************************************************************************************/
    //region (Private) Composable Methods

    /***************************************************************************************************************************************
     *           Method:    GithubSection
     *       Parameters:    context
     *          Returns:    None.
     *      Description:    Draws the Github section (Text talking about project, button link to project).
     **************************************************************************************************************************************/
    @Composable
    private fun GithubSection(context: Context)
    {
        Column (modifier = Modifier
            .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Text to provide more information.
            Text(
                modifier = Modifier.padding(8.dp),
                text = "Want to contribute? Or need a refresher on how to use this application? Check out the project source code on Github!",
            )

            // Button to link to project.
            IconTextButton(modifier = Modifier.fillMaxWidth(0.7f),
                resourceID = R.drawable.github_mark,
                buttonText = "Project Source Code.",
                isEnabled = true,
                minSize = 40.dp,
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, PROJECT_GITHUB_URL.toUri())
                    context.startActivity(intent)
                }
            )
        }
    }

    //endregion
}