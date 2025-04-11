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
 */
@Composable
fun AppBar(
    title: String,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: () -> Unit = {},
    showDot: Boolean = false,
    dotColor: Color = Accent
) {
    Surface(
        tonalElevation = 3.dp,
        color = Primary
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showBackButton) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onBackClick() }
                )
                Spacer(modifier = Modifier.width(16.dp))
            }
            
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.weight(1f)
            )
            
            trailingIcon?.let {
                Box(contentAlignment = Alignment.TopEnd) {
                    Icon(
                        imageVector = it,
                        contentDescription = "Action",
                        tint = MaterialTheme.colorScheme.onPrimary,
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
