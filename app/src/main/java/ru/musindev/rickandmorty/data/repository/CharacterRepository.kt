package ru.musindev.rickandmorty.data.repository

import android.util.Log
import ru.musindev.rickandmorty.data.api.RickAndMortyApi
import ru.musindev.rickandmorty.data.dao.CharacterDao
import ru.musindev.rickandmorty.data.mappers.toCharacter
import ru.musindev.rickandmorty.data.mappers.toCharacterDetails
import ru.musindev.rickandmorty.data.mappers.toEntity
import ru.musindev.rickandmorty.data.models.CharacterResponse
import ru.musindev.rickandmorty.domain.models.CharacterDetails
import ru.musindev.rickandmorty.domain.models.CharacterFilters
import ru.musindev.rickandmorty.utils.NetworkUtil
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val api: RickAndMortyApi,
    private val characterDao: CharacterDao,
    private val networkUtil: NetworkUtil
) {

    suspend fun getCharacters(
        page: Int,
        forceRefresh: Boolean = false,
        filters: CharacterFilters = CharacterFilters()
    ): CharacterResponse {
        // NETWORK-FIRST: всегда пытаемся загрузить с сервера если есть интернет
        if (networkUtil.isNetworkAvailable()) {
            return try {
                Log.d("CharacterRepository", "Loading from network, page: $page, filters: $filters")
                val response = api.getCharacters(
                    page = page,
                    status = filters.status,
                    species = filters.species,
                    type = filters.type,
                    gender = filters.gender
                )

                // Сохраняем в кеш только если нет фильтров
                if (!filters.isActive()) {
                    if (forceRefresh && page == 1) {
                        characterDao.clearAllCharacters()
                        Log.d("CharacterRepository", "Cache cleared for refresh")
                    }

                    characterDao.insertCharacters(response.results.map { it.toEntity() })
                    Log.d("CharacterRepository", "Saved ${response.results.size} characters from page $page to cache")
                }

                response
            } catch (e: Exception) {
                Log.e("CharacterRepository", "Network error, falling back to cache", e)
                // При ошибке сети возвращаем кеш с фильтрами
                loadFromCache(filters)
            }
        } else {
            // Нет интернета - возвращаем кеш с фильтрами
            Log.d("CharacterRepository", "No internet, loading from cache with filters")
            return loadFromCache(filters)
        }
    }

    private suspend fun loadFromCache(filters: CharacterFilters = CharacterFilters()): CharacterResponse {
        val cachedCharacters = if (filters.isActive()) {
            // Фильтрация через Room query
            characterDao.getFilteredCharacters(
                status = filters.status,
                species = filters.species,
                type = filters.type,
                gender = filters.gender
            )
        } else {
            // Все персонажи без фильтров
            characterDao.getAllCharacters()
        }

        if (cachedCharacters.isEmpty()) {
            throw Exception("No cached data available")
        }

        Log.d("CharacterRepository", "Loaded ${cachedCharacters.size} characters from cache (filters: ${filters.isActive()})")
        return CharacterResponse(
            info = ru.musindev.rickandmorty.data.models.PageInfo(
                count = cachedCharacters.size,
                pages = 1,
                next = null, // В офлайне пагинация недоступна
                prev = null
            ),
            results = cachedCharacters.map { it.toCharacter() }
        )
    }

    suspend fun getCharacterById(id: Int): CharacterDetails {
        if (networkUtil.isNetworkAvailable()) {
            return try {
                Log.d("CharacterRepository", "Loading character $id from network")
                val details = api.getCharacterById(id)
                characterDao.insertCharacter(details.toEntity())
                Log.d("CharacterRepository", "Saved character $id to cache")
                details
            } catch (e: Exception) {
                Log.e("CharacterRepository", "Network error for character $id, falling back to cache", e)
                loadCharacterFromCache(id)
            }
        } else {
            Log.d("CharacterRepository", "No internet, loading character $id from cache")
            return loadCharacterFromCache(id)
        }
    }

    private suspend fun loadCharacterFromCache(id: Int): CharacterDetails {
        val cachedCharacter = characterDao.getCharacterById(id)
            ?: throw Exception("Character not found in cache")

        return cachedCharacter.toCharacterDetails()
    }

    suspend fun searchCharactersInCache(query: String) =
        characterDao.searchCharacters(query).map { it.toCharacter() }
}
