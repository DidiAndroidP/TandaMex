package com.didiermendoza.tandamex.src.features.Profile.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ReputationStars(
    reputation: Float,
    modifier: Modifier = Modifier,
    starSize: Dp = 20.dp,
    showText: Boolean = true
) {
    val goldColor = Color(0xFFFFD700)
    val grayColor = Color.LightGray

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..5) {
            val icon = when {
                reputation >= i -> Icons.Filled.Star
                reputation >= i - 0.5f -> Icons.Filled.StarHalf
                else -> Icons.Outlined.Star
            }

            val tint = if (reputation >= i - 0.5f) goldColor else grayColor

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(starSize)
            )
        }

        if (showText) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = String.format("%.1f", reputation),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}