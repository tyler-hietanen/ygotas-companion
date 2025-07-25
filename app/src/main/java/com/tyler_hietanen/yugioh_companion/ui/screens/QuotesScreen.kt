/*******************************************************************************************************************************************
 *           Source:    QuotesScreen.kt
 *      Description:    Contains content for the Quotes Screen.
 ******************************************************************************************************************************************/
package com.tyler_hietanen.yugioh_companion.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import com.tyler_hietanen.yugioh_companion.business.quotes.Quote
import com.tyler_hietanen.yugioh_companion.presentation.ApplicationViewModel
import com.tyler_hietanen.yugioh_companion.presentation.viewmodels.QuotesViewModel
import com.tyler_hietanen.yugioh_companion.ui.layout.CompanionButtons
import kotlinx.coroutines.launch

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

            // Import quotes button.
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
        val context = LocalContext.current

        // State for programmatic scrolling
        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()

        // Actually draw.
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
        ) {
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                // Play random quote button.
                CompanionButtons.IconTextButton(
                    modifier = Modifier,
                    resourceID = R.drawable.dice_outlined,
                    buttonText = "Play a random quote.",
                    minSize = 48.dp,
                    isEnabled = true,
                    onClick = {
                        coroutineScope.launch {
                            val targetIndex = quotesViewModel.onPlayRandomQuote(context)
                            if (targetIndex != -1)
                            {
                                // Scroll to quote being played.
                                if (listOfQuotes.size > targetIndex)
                                {
                                    listState.animateScrollToItem(index = targetIndex)
                                }
                            }
                        }
                    }
                )
            }
            HorizontalDivider(modifier = Modifier.padding(8.dp))

            // List of quotes.
            Text(
                text = "Total Number of Quotes: " + listOfQuotes.count().toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Italic
            )
            HorizontalDivider(modifier = Modifier.padding(8.dp))

            // List of filter options (Filter by tags).
            // TODO.

            // List of discovered quotes.
            DrawQuoteList(listOfQuotes, quotesViewModel, listState)
        }
    }

    /***************************************************************************************************************************************
     *           Method:    DrawQuoteList
     *       Parameters:    quoteList
     *                      quotesViewModel
     *          Returns:    None.
     *      Description:    Draws full quote list.
     **************************************************************************************************************************************/
    @Composable
    private fun DrawQuoteList(
        quoteList: List<Quote>,
        quotesViewModel: QuotesViewModel,
        listState: LazyListState)
    {
        LazyColumn (
            state = listState,
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(
                items = quoteList,
                key = {quote -> quote.quoteID}
            )
            { quote ->
                QuoteItem(quote, quotesViewModel)
            }
        }
    }

    /***************************************************************************************************************************************
     *           Method:    QuoteItem
     *       Parameters:    quote
     *                      quotesViewModel
     *          Returns:    None.
     *      Description:    Draws full quote.
     **************************************************************************************************************************************/
    @Composable
    private fun QuoteItem(quote: Quote, quotesViewModel: QuotesViewModel)
    {
        val context = LocalContext.current

        Card (
            modifier = Modifier
                .padding(vertical = 4.dp)
                .heightIn(48.dp)
                .fillMaxWidth()
                .clickable { quotesViewModel.onPlayQuote(quote, context) },
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = MaterialTheme.shapes.medium,

            // Sets a border, if currently playing.
            border = if (quote.isPlaying)
            {
                BorderStroke(4.dp, MaterialTheme.colorScheme.primary)
            }
            else
            {
                null
            }
        ){
            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Play/Pause Button.
                QuotePlayIcon(quote = quote, quotesViewModel = quotesViewModel)

                // Quote Name.
                Text(
                    text = quote.quoteFriendlyName,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(4.dp)
                        .weight(1f),
                    fontWeight = FontWeight.SemiBold)

                // Share icon.
                IconButton(
                    onClick = { quotesViewModel.onShareQuote(quote, context) },
                    enabled = (!quote.isPlaying),
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.share_outlined),
                        contentDescription = "",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }

    /***************************************************************************************************************************************
     *           Method:    QuotePlayIcon
     *       Parameters:    quote
     *          Returns:    None.
     *      Description:    Draws a quote icon, based on whether it's currently playing or not.
     **************************************************************************************************************************************/
    @Composable
    private fun QuotePlayIcon(quote: Quote, quotesViewModel: QuotesViewModel)
    {
        val context = LocalContext.current

        // Determines icon button resource, based upon the current state.
        // (Is Playing).
        val iconID = if (quote.isPlaying)
        {
            R.drawable.stop_filled
        }
        else
        {
            R.drawable.play_filled
        }

        IconButton(
            onClick = { quotesViewModel.onPlayQuote(quote, context)},
            enabled = true,
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(
                painter = painterResource(id = iconID),
                contentDescription = "",
                modifier = Modifier.size(96.dp)
            )
        }
    }

    //endregion
}