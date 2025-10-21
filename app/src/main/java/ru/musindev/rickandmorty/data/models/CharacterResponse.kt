package ru.musindev.rickandmorty.data.models

data class CharacterResponse(
    val info: PageInfo,
    val results: List<Character>
)