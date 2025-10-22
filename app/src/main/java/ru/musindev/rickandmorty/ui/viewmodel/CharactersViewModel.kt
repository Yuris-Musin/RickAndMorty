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
import ru.musindev.rickandmorty.domain.models.CharacterFilters

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

    private val _filters = mutableStateOf(CharacterFilters())
    val filters = _filters

    private val _isOfflineMode = mutableStateOf(false)
    val isOfflineMode = _isOfflineMode

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
                val response = getCharactersUseCase(currentPage, forceRefresh = false, filters = _filters.value)
                _characters.addAll(response.results)
                currentPage++

                if (response.info.next == null) {
                    isLastPage = true
                }

                // Проверяем offline mode (если pages = 1 и next = null, это может быть кеш)
                _isOfflineMode.value = response.info.pages == 1 && response.info.next == null && _filters.value.isActive()

            } catch (e: Exception) {
                Log.e("CharactersViewModel", "Error loading characters", e)
                _error.value = when {
                    e.message?.contains("No cached data") == true -> "Нет сохранённых данных"
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

                val response = getCharactersUseCase(currentPage, forceRefresh = true, filters = _filters.value)
                _characters.addAll(response.results)
                currentPage++

                if (response.info.next == null) {
                    isLastPage = true
                }

                _isOfflineMode.value = false

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

    fun applyFilters(newFilters: CharacterFilters) {
        _filters.value = newFilters
        currentPage = 1
        isLastPage = false
        _characters.clear()
        loadCharacters()
    }

    fun clearFilters() {
        applyFilters(CharacterFilters())
    }
}
