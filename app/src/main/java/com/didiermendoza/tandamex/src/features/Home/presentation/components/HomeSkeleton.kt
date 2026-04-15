package com.didiermendoza.tandamex.src.features.Home.presentation.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun HomeSkeleton() {
    Column(modifier = Modifier.padding(20.dp)) {
        Spacer(modifier = Modifier.height(20.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            SkeletonCard(modifier = Modifier.width(260.dp).height(140.dp))
            SkeletonCard(modifier = Modifier.width(260.dp).height(140.dp))
        }
        Spacer(modifier = Modifier.height(40.dp))
        repeat(3) {
            SkeletonCard(modifier = Modifier.fillMaxWidth().height(80.dp))
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun SkeletonCard(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "skeleton")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f, targetValue = 0.6f,
        animationSpec = infiniteRepeatable(animation = tween(1000), repeatMode = RepeatMode.Reverse), label = "alpha"
    )
    Box(modifier = modifier.clip(RoundedCornerShape(16.dp)).background(Color.Gray.copy(alpha = alpha)))
}