package ru.musindev.rickandmorty.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.musindev.rickandmorty.domain.models.CharacterDetails
import ru.musindev.rickandmorty.domain.usecase.GetCharacterDetailsUseCase
import javax.inject.Inject

@HiltViewModel
class CharacterDetailsViewModel @Inject constructor(
    private val getCharacterDetailsUseCase: GetCharacterDetailsUseCase
) : ViewModel() {

    private val _character = MutableStateFlow<CharacterDetails?>(null)
    val character: StateFlow<CharacterDetails?> = _character

    fun loadCharacter(id: Int) {
        viewModelScope.launch {
            val details = getCharacterDetailsUseCase(id)
            _character.value = details
        }
    }
}

