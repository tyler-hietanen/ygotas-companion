/*******************************************************************************************************************************************
 *           Source:    DuelScreen.kt
 *      Description:    Contains content for the House Rules Screen.
 ******************************************************************************************************************************************/
package com.tyler_hietanen.yugioh_companion.ui.screens

import android.content.Context
import android.text.Spanned
import android.widget.TextView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.tyler_hietanen.yugioh_companion.R
import com.tyler_hietanen.yugioh_companion.presentation.ApplicationViewModel
import com.tyler_hietanen.yugioh_companion.presentation.viewmodels.HouseRulesViewModel
import com.tyler_hietanen.yugioh_companion.ui.layout.CompanionButtons
import io.noties.markwon.Markwon

object HouseRulesScreen
{
    /***************************************************************************************************************************************
     *      (Public) Composable Methods
     **************************************************************************************************************************************/
    //region (Public) Composable Methods

    /***************************************************************************************************************************************
     *           Method:    DrawScreen
     *       Parameters:    applicationViewModel
     *          Returns:    None.
     *      Description:    Composable function manages drawing the House Rules screen.
     **************************************************************************************************************************************/
    @Composable
    fun DrawScreen(applicationViewModel: ApplicationViewModel)
    {
        // Grab the values they care about from the ViewModel.
        val houseRulesViewModel = applicationViewModel.houseRulesViewModel
        val houseRulesContent by houseRulesViewModel.houseRulesContent

        // Depending upon the value of the content, determines what is shown.
        if (houseRulesContent == null)
        {
            EmptyHouseRulesScreen(houseRulesViewModel)
        }
        else
        {
            HouseRulesScreen(houseRulesContent!!)
        }
    }

    //endregion

    /***************************************************************************************************************************************
     *      (Private) Composable Methods
     **************************************************************************************************************************************/
    //region (Private) Composable Methods

    /***************************************************************************************************************************************
     *           Method:    EmptyHouseRulesScreen
     *       Parameters:    None.
     *          Returns:    None.
     *      Description:    Draws an empty screen (if no house rules are loaded).
     **************************************************************************************************************************************/
    @Composable
    private fun EmptyHouseRulesScreen(houseRulesViewModel: HouseRulesViewModel)
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
        Column (
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            // Sad Kuriboh icon.
            Image(
                painter = painterResource(R.drawable.angry_kuriboh),
                contentDescription = "",
                modifier = Modifier.size(240.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Text block.
            Text(
                text = "No House Rules have been loaded. That pisses Kuriboh off!",
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Kuriboh hates modern Yu-Gi-Oh (Especially hand traps - fuck hand traps).",
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "You don't want to make Kuriboh angry, do you? Why don't you import some house rules?",
            )

            // Import house rules button.
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
    }

    /***************************************************************************************************************************************
     *           Method:    HouseRulesScreen
     *       Parameters:    content
     *          Returns:    None.
     *      Description:    Draws the house rules content screen.
     **************************************************************************************************************************************/
    @Composable
    private fun HouseRulesScreen(content: String)
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Show markdown content.
            MarkdownDisplay(markdownContent = content, context = LocalContext.current)
        }
    }

    /***************************************************************************************************************************************
     *           Method:    MarkdownDisplay
     *       Parameters:    markdownContent
     *                          - Content (hopefully formatted as markdown).
     *                      context
     *          Returns:    None.
     *      Description:    Draws markdown content into strings.
     **************************************************************************************************************************************/
    @Composable
    fun MarkdownDisplay(markdownContent: String, context: Context)
    {
        // Creates markwon, spanned.
        val markwon = remember(context) {
            Markwon.create(context)
        }
        val spanned: Spanned = remember(markdownContent, markwon) {
            markwon.toMarkdown(markdownContent)
        }
        val scrollState = rememberScrollState()

        // Copies text color (So Markdown isn't gray).
        val textColor = MaterialTheme.colorScheme.onBackground
        AndroidView(
            factory = {
                ctx -> TextView(ctx)
                    .apply {
                        setTextColor(textColor.toArgb())
                    }
                  },
            update = { textView ->
                textView.text = spanned
            },
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(8.dp)
        )
    }

    //endregion
}