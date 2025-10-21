package ru.musindev.rickandmorty.data.repository

import ru.musindev.rickandmorty.data.api.RickAndMortyApi
import ru.musindev.rickandmorty.domain.models.CharacterDetails
import ru.musindev.rickandmorty.data.models.CharacterResponse
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val api: RickAndMortyApi
) {
    suspend fun getCharacters(page: Int): CharacterResponse {
        return api.getCharacters(page)
    }

    suspend fun getCharacterById(id: Int): CharacterDetails {
        return api.getCharacterById(id)
    }
}