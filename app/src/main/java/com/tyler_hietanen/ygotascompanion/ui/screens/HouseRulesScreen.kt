/*******************************************************************************************************************************************
 *           Source:    HouseRulesScreenComposable.kt
 *      Description:    Draws the composable screen for the House Rules Screen.
 ******************************************************************************************************************************************/
package com.tyler_hietanen.ygotascompanion.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun HouseRulesScreenComposable()
{
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center)
    {
        Text("House Rules Screen")
    }
}