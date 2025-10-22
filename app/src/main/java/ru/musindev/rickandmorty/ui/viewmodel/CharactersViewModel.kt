package ru.musindev.rickandmorty.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.musindev.rickandmorty.domain.usecase.GetCharactersUseCase
import javax.inject.Inject
import ru.musindev.rickandmorty.data.models.Character

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase
) : ViewModel() {

    private val _characters = mutableStateListOf<Character>()
    val characters: List<Character> = _characters

    private val _isLoading = mutableStateOf(false)
    val isLoading = _isLoading

    private val _isRefreshing = mutableStateOf(false)
    val isRefreshing = _isRefreshing

    private val _error = mutableStateOf<String?>(null)
    val error = _error

    private var currentPage = 1
    private var isLastPage = false

    init {
        loadCharacters()
    }

    fun loadCharacters() {
        if (_isLoading.value || _isRefreshing.value || isLastPage) return

        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                // forceRefresh = false, но всегда пытаемся загрузить с сервера
                val response = getCharactersUseCase(currentPage, forceRefresh = false)
                _characters.addAll(response.results)
                currentPage++

                if (response.info.next == null) {
                    isLastPage = true
                }
            } catch (e: Exception) {
                Log.e("CharactersViewModel", "Error loading characters", e)
                _error.value = when {
                    e.message?.contains("No internet") == true -> "Нет интернета. Показаны сохранённые данные"
                    e.message?.contains("no cached data") == true -> "Нет интернета и нет сохранённых данных"
                    else -> "Ошибка загрузки: ${e.message}"
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun filterCharacters(query: String): List<Character> {
        if (query.isBlank()) return characters
        return characters.filter {
            it.name.contains(query, ignoreCase = true)
        }
    }

    fun refreshCharacters() {
        if (_isRefreshing.value) return

        _isRefreshing.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                currentPage = 1
                isLastPage = false
                _characters.clear()

                // forceRefresh = true для очистки кеша
                val response = getCharactersUseCase(currentPage, forceRefresh = true)
                _characters.addAll(response.results)
                currentPage++

                if (response.info.next == null) {
                    isLastPage = true
                }
            } catch (e: Exception) {
                Log.e("CharactersViewModel", "Error refreshing characters", e)
                _error.value = when {
                    e.message?.contains("No internet") == true -> "Нет интернета для обновления"
                    else -> "Ошибка обновления: ${e.message}"
                }
            } finally {
                _isRefreshing.value = false
            }
        }
    }
}
