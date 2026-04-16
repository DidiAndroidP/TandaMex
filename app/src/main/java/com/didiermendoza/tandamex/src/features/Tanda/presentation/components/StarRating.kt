package com.didiermendoza.tandamex.src.features.Tanda.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StarRating(
    rating: Int,
    onRatingChange: ((Int) -> Unit)? = null,
    maxStars: Int = 5,
    starSize: Int = 24
) {
    Row {
        for (i in 1..maxStars) {
            val isSelected = i <= rating
            Icon(
                imageVector = if (isSelected) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = null,
                tint = if (isSelected) Color(0xFFFFC107) else Color.Gray,
                modifier = Modifier
                    .size(starSize.dp)
                    .clickable(enabled = onRatingChange != null) {
                        onRatingChange?.invoke(i)
                    }
            )
        }
    }
}