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

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadCharacter(id: Int) {
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val details = getCharacterDetailsUseCase(id)
                _character.value = details
            } catch (e: Exception) {
                Log.e("CharacterDetailsViewModel", "Error loading character details", e)
                _error.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }
}

//@HiltViewModel
//class CharacterDetailsViewModel @Inject constructor(
//    private val getCharacterDetailsUseCase: GetCharacterDetailsUseCase
//) : ViewModel() {
//
//    private val _character = MutableStateFlow<CharacterDetails?>(null)
//    val character: StateFlow<CharacterDetails?> = _character
//
//    fun loadCharacter(id: Int) {
//        viewModelScope.launch {
//            val details = getCharacterDetailsUseCase(id)
//            _character.value = details
//        }
//    }
//}

