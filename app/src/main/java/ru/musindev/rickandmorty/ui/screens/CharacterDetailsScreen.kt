package ru.musindev.rickandmorty.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import ru.musindev.rickandmorty.R
import ru.musindev.rickandmorty.ui.viewmodel.CharacterDetailsViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun CharacterDetailsScreen(
    characterId: Int,
    onBackClick: () -> Unit,
    viewModel: CharacterDetailsViewModel = hiltViewModel()
) {
    val character by viewModel.character.collectAsState()

    LaunchedEffect(characterId) {
        viewModel.loadCharacter(characterId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        IconButton(
            onClick = { onBackClick() },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }

        character?.let { char ->
            AsyncImage(
                model = char.image,
                contentDescription = char.name,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(300.dp)
                    .padding(vertical = 16.dp),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.image),
                error = painterResource(R.drawable.image)
            )
            BoldLabelText("Name", char.name.ifEmpty { "Not available" })
            BoldLabelText("Status", char.status.ifEmpty { "Not available" })
            BoldLabelText("Species", char.species.ifEmpty { "Not available" })
            BoldLabelText("Abilities", char.type.ifEmpty { "Not available" })
            BoldLabelText("Gender", char.gender.ifEmpty { "Not available" })
            BoldLabelText("Origin", char.origin.name.ifEmpty { "Not available" })
            BoldLabelText("Current location", char.location.name.ifEmpty { "Not available" })
            BoldLabelText(
                "Episode count",
                if (char.episode.isEmpty()) "Not available" else char.episode.size.toString()
            )
            BoldLabelText(
                "Created",
                if (char.created.isNotEmpty()) formatDate(char.created) else "Not available"
            )
        }
    }
}

@Composable
fun BoldLabelText(label: String, value: String) {
    Text(
        buildAnnotatedString {
            append("$label: ")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(value)
            }
        },
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

fun formatDate(dateTime: String): String {
    return try {
        val ldt = LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_DATE_TIME)
        val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH)
        ldt.format(formatter)
    } catch (e: Exception) {
        "Not available"
    }
}
