/*******************************************************************************************************************************************
 *           Source:    SettingsScreenComposable.kt
 *      Description:    Draws the composable screen for the Settings Screen.
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
fun SettingsScreenComposable()
{
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center)
    {
        Text("Settings Screen")
    }
}