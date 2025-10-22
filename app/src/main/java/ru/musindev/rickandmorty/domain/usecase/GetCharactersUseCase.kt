package ru.musindev.rickandmorty.domain.usecase

import ru.musindev.rickandmorty.data.repository.CharacterRepository
import ru.musindev.rickandmorty.domain.models.CharacterFilters
import javax.inject.Inject

class GetCharactersUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(
        page: Int,
        forceRefresh: Boolean = false,
        filters: CharacterFilters = CharacterFilters()
    ) = repository.getCharacters(page, forceRefresh, filters)
}
