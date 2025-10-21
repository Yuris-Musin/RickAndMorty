package ru.musindev.rickandmorty.domain.usecase

import ru.musindev.rickandmorty.domain.models.CharacterDetails
import ru.musindev.rickandmorty.data.repository.CharacterRepository
import javax.inject.Inject

class GetCharacterDetailsUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(id: Int): CharacterDetails {
        return repository.getCharacterById(id)
    }
}
