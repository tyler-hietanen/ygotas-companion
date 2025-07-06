/*******************************************************************************************************************************************
 *           Source:    SettingsScreen.kt
 *      Description:    Contains content for the Settings Screen.
 ******************************************************************************************************************************************/
package com.tyler_hietanen.yugioh_companion.ui.screens

import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tyler_hietanen.yugioh_companion.R
import com.tyler_hietanen.yugioh_companion.presentation.ApplicationViewModel
import com.tyler_hietanen.yugioh_companion.ui.layout.CompanionButtons.IconTextButton
import androidx.core.net.toUri
import com.tyler_hietanen.yugioh_companion.presentation.viewmodels.DuelViewModel
import com.tyler_hietanen.yugioh_companion.presentation.viewmodels.HouseRulesViewModel
import com.tyler_hietanen.yugioh_companion.ui.layout.CompanionButtons

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
            Text(
                text = "Settings",
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

            // TODO Quotes configuration.
            //      TODO: Delete all stored quotes. Confirms before allowing.

            // Duel(s) configuration.s
            DuelSettings(duelViewModel =  applicationViewModel.duelViewModel)
            HorizontalDivider(modifier = Modifier.padding(8.dp))

            // House Rule(s) configuration.
            HouseRulesSettings(houseRulesViewModel = applicationViewModel.houseRulesViewModel)
            HorizontalDivider(modifier = Modifier.padding(8.dp))

            // At the very end, show a wonder Kuriboh.
            Image(
                painter = painterResource(id = R.drawable.wonder_kuriboh),
                contentDescription = "",
                modifier = Modifier.fillMaxWidth()
            )
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
        Column (
            modifier = Modifier
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
            IconTextButton(
                modifier = Modifier
                    .fillMaxWidth(0.9f),
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
            Text(
                text = "-- Duel Settings --",
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
        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Text(
                text = "-- House Rule(s) Settings --",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Italic
            )
            // Allow importing of house rules.
            LoadHouseRulesButton(houseRulesViewModel)
        }
    }

    /***************************************************************************************************************************************
     *           Method:    LoadHouseRulesButton
     *       Parameters:    None.
     *          Returns:    None.
     *      Description:    Draws the load house rules button. Contains logic for picking a .md file, for house rules.
     **************************************************************************************************************************************/
    @Composable
    private fun LoadHouseRulesButton(houseRulesViewModel: HouseRulesViewModel)
    {
        // Variables.
        val isImporting by houseRulesViewModel.isImportingHouseRules
        val context = LocalContext.current

        // Sets up logic for picking a markdown file (Hopefully this is house rules).
        val pickMarkdownFileLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
            onResult = { fileUri ->
                fileUri?.let {
                    // If non-null, reports change to View Model with chosen file.
                    houseRulesViewModel.onImportHouseRules(context, fileUri)
                }
            }
        )

        // Actually draws.
        CompanionButtons.IconTextButtonWithProgress(
            modifier = Modifier,
            resourceID = R.drawable.add_filled,
            buttonText = "Import new House Rules.",
            isLoading = isImporting,
            minSize = 40.dp,
            onClick = {
                // Launch file picker.
                pickMarkdownFileLauncher.launch("text/markdown")
            }
        )
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
                .padding(
                    horizontal = 16.dp,
                    vertical = 4.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = settingsText,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .weight(1f),
            )
            Spacer(
                modifier = Modifier
                    .width(16.dp))
            Switch(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                enabled = enabled
            )
        }
    }

    //endregion
}