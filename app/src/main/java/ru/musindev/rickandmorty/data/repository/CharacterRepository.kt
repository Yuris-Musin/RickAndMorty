package ru.musindev.rickandmorty.data.repository

import android.util.Log
import ru.musindev.rickandmorty.data.api.RickAndMortyApi
import ru.musindev.rickandmorty.data.dao.CharacterDao
import ru.musindev.rickandmorty.data.mappers.toCharacter
import ru.musindev.rickandmorty.data.mappers.toCharacterDetails
import ru.musindev.rickandmorty.data.mappers.toEntity
import ru.musindev.rickandmorty.data.models.CharacterResponse
import ru.musindev.rickandmorty.domain.models.CharacterDetails
import ru.musindev.rickandmorty.utils.NetworkUtil
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val api: RickAndMortyApi,
    private val characterDao: CharacterDao,
    private val networkUtil: NetworkUtil
) {

    suspend fun getCharacters(page: Int, forceRefresh: Boolean = false): CharacterResponse {
        // NETWORK-FIRST стратегия: всегда пытаемся загрузить с сервера если есть интернет
        if (networkUtil.isNetworkAvailable()) {
            return try {
                Log.d("CharacterRepository", "Loading from network, page: $page")
                val response = api.getCharacters(page)

                // ИСПРАВЛЕНИЕ: Сохраняем ВСЕ страницы в кеш
                if (forceRefresh && page == 1) {
                    // При force refresh очищаем кеш только один раз
                    characterDao.clearAllCharacters()
                    Log.d("CharacterRepository", "Cache cleared for refresh")
                }

                // Сохраняем каждую загруженную страницу
                characterDao.insertCharacters(response.results.map { it.toEntity() })
                Log.d("CharacterRepository", "Saved ${response.results.size} characters from page $page to cache")

                response
            } catch (e: Exception) {
                Log.e("CharacterRepository", "Network error, falling back to cache", e)
                // При ошибке сети пробуем вернуть кеш
                loadFromCache()
            }
        } else {
            // Нет интернета - сразу возвращаем кеш
            Log.d("CharacterRepository", "No internet, loading from cache")
            return loadFromCache()
        }
    }

    private suspend fun loadFromCache(): CharacterResponse {
        val cachedCharacters = characterDao.getAllCharacters()

        if (cachedCharacters.isEmpty()) {
            throw Exception("No internet connection and no cached data available")
        }

        Log.d("CharacterRepository", "Loaded ${cachedCharacters.size} characters from cache")
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
        // NETWORK-FIRST: всегда пытаемся загрузить с сервера
        if (networkUtil.isNetworkAvailable()) {
            return try {
                Log.d("CharacterRepository", "Loading character $id from network")
                val details = api.getCharacterById(id)

                // Сохраняем в кеш
                characterDao.insertCharacter(details.toEntity())
                Log.d("CharacterRepository", "Saved character $id to cache")

                details
            } catch (e: Exception) {
                Log.e("CharacterRepository", "Network error for character $id, falling back to cache", e)
                // При ошибке пробуем взять из кеша
                loadCharacterFromCache(id)
            }
        } else {
            // Нет интернета - берём из кеша
            Log.d("CharacterRepository", "No internet, loading character $id from cache")
            return loadCharacterFromCache(id)
        }
    }

    private suspend fun loadCharacterFromCache(id: Int): CharacterDetails {
        val cachedCharacter = characterDao.getCharacterById(id)
            ?: throw Exception("No internet connection and character not found in cache")

        return cachedCharacter.toCharacterDetails()
    }

    suspend fun searchCharactersInCache(query: String) =
        characterDao.searchCharacters(query).map { it.toCharacter() }
}
