/*******************************************************************************************************************************************
 *           Source:    SettingsScreen.kt
 *      Description:    Contains content for the Duel Screen.
 ******************************************************************************************************************************************/
package com.tyler_hietanen.yugioh_companion.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tyler_hietanen.yugioh_companion.R
import com.tyler_hietanen.yugioh_companion.presentation.ApplicationViewModel
import com.tyler_hietanen.yugioh_companion.ui.layout.CompanionButtons.IconTextButton
import androidx.core.net.toUri
import com.tyler_hietanen.yugioh_companion.presentation.viewmodels.DuelViewModel
import com.tyler_hietanen.yugioh_companion.presentation.viewmodels.HouseRulesViewModel

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
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            HorizontalDivider(modifier = Modifier.padding(8.dp))

            // Github section (Text about project, link to project).
            GithubSection(context)
            HorizontalDivider(modifier = Modifier.padding(8.dp))

            // Duel(s) configuration.s
            DuelSettings(applicationViewModel.duelViewModel)
            HorizontalDivider(modifier = Modifier.padding(8.dp))

            // House Rule(s) configuration.
            HouseRulesSettings(applicationViewModel.houseRulesViewModel)
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
                modifier = Modifier.padding(16.dp),
                text = "Want to contribute? Or need a refresher on how to use this application? File formats?\n\nCheck out the project source code on Github!",
            )

            // Button to link to project.
            IconTextButton(modifier = Modifier.fillMaxWidth(0.9f),
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

    /***************************************************************************************************************************************
     *           Method:    DuelSettings
     *       Parameters:    None.
     *          Returns:    None.
     *      Description:    Draws the Settings that are related to dueling.
     **************************************************************************************************************************************/
    @Composable
    private fun DuelSettings(duelViewModel: DuelViewModel)
    {
        // Gathers out variables this cares about.
        val isSnarkEnabled by duelViewModel.isSnarkEnabled
        val isMockEnabled by duelViewModel.isMockEnabled

        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            // Starts displaying settings.
            // Text so they know what they're modifying it for.
            Text(text = "-- Duel Settings --",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Italic
            )
            // Snark about ugly life points.
            SettingsSwitch(
                settingsText = "Allow duel snark (Berating for ugly life points)?",
                isChecked = isSnarkEnabled,
                onCheckedChange = { isChecked ->
                    duelViewModel.onChangeSnarkSetting(isChecked)
                },
                modifier = Modifier)

            // Mocking for losses.
            SettingsSwitch(
                settingsText = "Mock user on loss?",
                isChecked = isMockEnabled,
                onCheckedChange = { isChecked ->
                    duelViewModel.onChangeMockSetting(isChecked)
                },
                modifier = Modifier)
        }
    }

    /***************************************************************************************************************************************
     *           Method:    HouseRulesSettings
     *       Parameters:    None.
     *          Returns:    None.
     *      Description:    Draws the Settings that are related to house rules.
     **************************************************************************************************************************************/
    @Composable
    private fun HouseRulesSettings(houseRulesViewModel: HouseRulesViewModel)
    {
        // Grab required information.
        val isImporting by houseRulesViewModel.isImportingHouseRules

        // Actually draw.
        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            // Starts displaying settings.
            // Text so they know what they're modifying it for.
            Text(text = "-- House Rule(s) Settings --",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Italic
            )
            // Allow importing of house rules.
            IconTextButtonWithProgress(modifier = Modifier,
                resourceID = R.drawable.add_filled,
                buttonText = "Import new House Rules.",
                isLoading = isImporting,
                minSize = 40.dp,
                onClick = {
                    houseRulesViewModel.onImportHouseRules()
                }
            )
        }
    }

    /***************************************************************************************************************************************
     *           Method:    DuelSettings
     *       Parameters:    None.
     *          Returns:    None.
     *      Description:    Draws the Settings that are related to dueling.
     **************************************************************************************************************************************/
    @Composable
    private fun SettingsSwitch(settingsText: String, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit, modifier: Modifier, enabled: Boolean = true)
    {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = settingsText,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(16.dp))
            Switch(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                enabled = enabled
            )
        }
    }

    /***************************************************************************************************************************************
     *           Method:    IconTextButtonWithProgress
     *       Parameters:    None.
     *          Returns:    None.
     *      Description:    Draws an IconTextButton with a circular progress indicator next to it.
     **************************************************************************************************************************************/
    @Composable
    private fun IconTextButtonWithProgress(modifier: Modifier, resourceID: Int, buttonText: String, isLoading: Boolean, minSize: Dp = 56.dp, onClick: () -> Unit, enabled: Boolean = true)
    {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconTextButton(
                modifier = modifier,
                resourceID = resourceID,
                buttonText = buttonText,
                isEnabled = (enabled && !isLoading),
                minSize = minSize,
                onClick = onClick
            )
            if (isLoading)
            {
                // Show the progress bar.
                Spacer(modifier = Modifier.width(8.dp))
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            }
        }
    }

    //endregion
}