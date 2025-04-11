package com.bitwin.helperapp.core.shared_components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bitwin.helperapp.core.theme.HelperAppTheme
import com.bitwin.helperapp.core.theme.Primary
import com.bitwin.helperapp.core.theme.Accent

/**
 * A reusable app bar component that can be configured to match different designs.
 * 
 * @param title The text to display in the app bar
 * @param showBackButton Whether to show a back button on the left
 * @param onBackClick Callback when the back button is clicked
 * @param trailingIcon Optional trailing icon to display on the right
 * @param onTrailingIconClick Callback when the trailing icon is clicked
 * @param showDot Whether to show a notification dot on the trailing icon
 * @param dotColor Color of the notification dot
 * @param backgroundColor Background color of the app bar
 * @param contentColor Text and icon color for the app bar
 */
@Composable
fun AppBar(
    title: String,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: () -> Unit = {},
    showDot: Boolean = false,
    dotColor: Color = Accent,
    backgroundColor: Color = Color.White,
    contentColor: Color = Color.Black
) {
    Surface(
        color = backgroundColor
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            if (showBackButton) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = contentColor,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onBackClick() }
                        .align(Alignment.CenterStart)
                )
            }
            
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = contentColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
            
            trailingIcon?.let {
                Box(
                    contentAlignment = Alignment.TopEnd,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = it,
                        contentDescription = "Action",
                        tint = contentColor,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onTrailingIconClick() }
                    )
                    
                    if (showDot) {
                        Surface(
                            color = dotColor,
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier
                                .size(8.dp)
                                .align(Alignment.TopEnd)
                        ) {}
                    }
                }
            }
        }
    }
}
