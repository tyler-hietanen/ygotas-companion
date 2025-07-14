/*******************************************************************************************************************************************
 *           Source:    QuotesScreen.kt
 *      Description:    Contains content for the Quotes Screen.
 ******************************************************************************************************************************************/
package com.tyler_hietanen.yugioh_companion.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tyler_hietanen.yugioh_companion.R
import com.tyler_hietanen.yugioh_companion.presentation.ApplicationViewModel
import com.tyler_hietanen.yugioh_companion.presentation.viewmodels.QuotesViewModel
import com.tyler_hietanen.yugioh_companion.ui.layout.CompanionButtons

object QuotesScreen
{
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
        // Sources view model(s).
        val quotesViewModel = applicationViewModel.quotesViewModel
        val listOfQuotes = quotesViewModel.filteredQuoteList

        // Depending on whether quotes are loaded, display different screens.
        if (listOfQuotes.isEmpty())
        {
            DrawEmptyQuotesScreen(quotesViewModel)
        }
        else
        {
            DrawQuotesScreen(quotesViewModel)
        }
    }

    //endregion

    /***************************************************************************************************************************************
     *      (Private) Composable Methods
     **************************************************************************************************************************************/
    //region (Private) Composable Methods

    /***************************************************************************************************************************************
     *           Method:    DrawEmptyQuotesScreen
     *       Parameters:    quotesViewModel
     *          Returns:    None.
     *      Description:    Draws an empty screen (if no quotes are loaded).
     **************************************************************************************************************************************/
    @Composable
    private fun DrawEmptyQuotesScreen(quotesViewModel: QuotesViewModel)
    {
        // Variables.
        val isImporting by quotesViewModel.isImportingQuotes
        val context = LocalContext.current

        // Sets up logic for picking a zipped file (Hopefully this contains quotes).
        val pickQuotesFileLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
            onResult = { fileUri ->
                fileUri?.let {
                    // If non-null, reports change to View Model with chosen file.
                    quotesViewModel.onImportQuotes(context, fileUri)
                }
            }
        )

        // Actually draw.
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Sad Kuriboh icon.
            Image(
                painter = painterResource(R.drawable.sad_kuriboh),
                contentDescription = "",
                modifier = Modifier.size(240.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Text block.
            Text(
                text = "No Quotes have been been loaded. That makes Kuriboh sad...",
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "You don't want to make Kuriboh sad, do you? Why don't you import some quotes?",
            )

            // Import house rules button.
            CompanionButtons.IconTextButtonWithProgress(
                modifier = Modifier,
                resourceID = R.drawable.add_filled,
                buttonText = "Import new quote package.",
                isLoading = isImporting,
                minSize = 40.dp,
                onClick = {
                    // Launch file picker.
                    pickQuotesFileLauncher.launch("application/zip")
                }
            )
        }
    }

    /***************************************************************************************************************************************
     *           Method:    DrawQuotesScreen
     *       Parameters:    quotesViewModel
     *          Returns:    None.
     *      Description:    Draws quotes screen (if quotes are loaded).
     **************************************************************************************************************************************/
    @Composable
    private fun DrawQuotesScreen(quotesViewModel: QuotesViewModel)
    {
        // Tracked variables.
        val listOfQuotes = quotesViewModel.filteredQuoteList

        // Actually draw.
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.1f)
            ) {
                // Search bar.
                QuotesSearchBar(quotesViewModel)
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {

            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun QuotesSearchBar(quotesViewModel: QuotesViewModel)
    {
        var searchQuery by remember { mutableStateOf("") }

        SearchBar(
            modifier = Modifier
                .fillMaxWidth(),
            query = searchQuery,
            onQueryChange = { query ->
                searchQuery = query
            },
            onSearch = { query ->
                quotesViewModel.onSearchQueryChanged(query)
            },
            active = false,
            onActiveChange = {
                // TODO.
            },
            placeholder = {
                Text(
                    text = "Search quotes by name, tag..."
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = ""
                )
            }
        ) {

        }
    }


















    //endregion
}