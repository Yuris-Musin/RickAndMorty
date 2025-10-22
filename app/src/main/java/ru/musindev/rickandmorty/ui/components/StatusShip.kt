package ru.musindev.rickandmorty.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StatusChip(status: String, modifier: Modifier = Modifier) {
    val (color, statusText) = when (status) {
        "Alive" -> Color(0xFF4CAF50) to "Alive"
        "Dead" -> Color(0xFFF44336) to "Dead"
        else -> Color(0xFFC79100) to "Unknown"
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(Color(0xCC222228), shape = RoundedCornerShape(topStart = 24.dp))
            .padding(horizontal = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = statusText,
            color = Color.White,
            fontSize = 12.sp
        )
    }
}
