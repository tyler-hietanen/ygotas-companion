/*******************************************************************************************************************************************
 *           Source:    CompanionButtons.kt
 *      Description:    Layout class for buttons used in the companion application.
 ******************************************************************************************************************************************/
package com.tyler_hietanen.yugioh_companion.ui.layout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tyler_hietanen.yugioh_companion.ui.theme.Typography

object CompanionButtons
{
    /***************************************************************************************************************************************
     *           Method:    TextButton
     *       Parameters:    modifier
     *                      onClick
     *                          - Function called when button is clicked.
     *          Returns:    None.
     *      Description:    Draws a button with text.
     **************************************************************************************************************************************/
    @Composable
    fun TextButton(modifier: Modifier, buttonText: String, minSize: Dp = 56.dp, isEnabled: Boolean, onClick: () -> Unit)
    {
        Button(
            modifier = modifier
                .heightIn(minSize)
                .padding(4.dp, 0.dp),
            onClick = onClick,
            colors = ButtonDefaults.outlinedButtonColors(),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
            enabled = isEnabled
        ) {
            Text(text = buttonText,
                style = Typography.titleLarge)
        }
    }

    /***************************************************************************************************************************************
     *           Method:    IconButton
     *       Parameters:    modifier
     *                      resourceID
     *                          - Drawable resource to use for icon.
     *                      minSize
     *                          - (Optional) minimum size.
     *                      onClick
     *                          - Function called when button is clicked.
     *          Returns:    None.
     *      Description:    Draws a button with text.
     **************************************************************************************************************************************/
    @Composable
    fun IconButton(modifier: Modifier, resourceID: Int, minSize: Dp = 56.dp, isEnabled: Boolean, onClick: () -> Unit)
    {
        Surface (
            modifier = modifier
                .heightIn(minSize)
                .fillMaxWidth(1f)
                .padding(2.dp),
            shape = MaterialTheme.shapes.small
        ){
            Button(
                onClick = { onClick()},
                colors = ButtonDefaults.outlinedButtonColors(),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                enabled = isEnabled
            ) {
                Icon(
                    painter = painterResource(resourceID),
                    contentDescription = "",
                    modifier = modifier.size(minSize - 8.dp))
            }
        }
    }

    /***************************************************************************************************************************************
     *           Method:    IconTextButton
     *       Parameters:    modifier
     *                      resourceID
     *                      buttonText
     *                      minSize
     *                      isEnabled
     *                      onClick
     *          Returns:    None.
     *      Description:    Draws a button with an icon and a text. Icon is shown on left, text on right.
     **************************************************************************************************************************************/
    @Composable
    fun IconTextButton(modifier: Modifier, resourceID: Int, buttonText: String, minSize: Dp = 56.dp, isEnabled: Boolean, onClick: () -> Unit)
    {
        Button(
            modifier = modifier
                .heightIn(minSize),
            colors = ButtonDefaults.outlinedButtonColors(),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
            onClick = onClick,
            enabled = isEnabled
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(resourceID),
                    contentDescription = "",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                Text (
                    text = buttonText
                )
            }
        }
    }
}

