package ru.musindev.rickandmorty.domain.models

import ru.musindev.rickandmorty.data.models.Location
import ru.musindev.rickandmorty.data.models.Origin

data class CharacterDetails(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: Origin,
    val location: Location,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String
)