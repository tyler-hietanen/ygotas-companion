/*******************************************************************************************************************************************
 *           Source:    DuelScreen.kt
 *      Description:    Contains content for the House Rules Screen.
 ******************************************************************************************************************************************/
package com.tyler_hietanen.yugioh_companion.ui.screens

import android.content.Context
import android.text.Spanned
import android.widget.TextView
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.tyler_hietanen.yugioh_companion.R
import com.tyler_hietanen.yugioh_companion.presentation.ApplicationViewModel
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
            EmptyHouseRulesScreen()
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
    private fun EmptyHouseRulesScreen()
    {
        Column (
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Image(
                painter = painterResource(R.drawable.sad_kuriboh),
                contentDescription = "",
                modifier = Modifier.size(240.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "No House Rules have been loaded. That makes Kuriboh sad...",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "You don't want to make Kuriboh sad do you? Why don't you import house rules from the Settings screen?",
                textAlign = TextAlign.Center
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
            Text(
                "House Rules",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

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

        AndroidView(
            factory = {
                ctx -> TextView(ctx)
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