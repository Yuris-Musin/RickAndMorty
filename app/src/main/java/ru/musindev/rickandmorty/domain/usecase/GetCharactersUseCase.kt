package ru.musindev.rickandmorty.domain.usecase

import ru.musindev.rickandmorty.data.repository.CharacterRepository
import javax.inject.Inject

class GetCharactersUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(page: Int) = repository.getCharacters(page)
}
