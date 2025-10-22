package ru.musindev.rickandmorty.data.repository

import ru.musindev.rickandmorty.data.api.RickAndMortyApi
import ru.musindev.rickandmorty.data.dao.CharacterDao
import ru.musindev.rickandmorty.data.mappers.toCharacter
import ru.musindev.rickandmorty.data.mappers.toCharacterDetails
import ru.musindev.rickandmorty.data.mappers.toEntity
import ru.musindev.rickandmorty.data.models.CharacterResponse
import ru.musindev.rickandmorty.data.models.PageInfo
import ru.musindev.rickandmorty.domain.models.CharacterDetails
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val api: RickAndMortyApi,
    private val characterDao: CharacterDao
) {

    suspend fun getCharacters(page: Int, forceRefresh: Boolean = false): CharacterResponse {
        // Если первая страница и не force refresh - пробуем взять из кеша
        if (page == 1 && !forceRefresh) {
            val cachedCharacters = characterDao.getAllCharacters()
            if (cachedCharacters.isNotEmpty()) {
                return CharacterResponse(
                    info = PageInfo(
                        count = cachedCharacters.size,
                        pages = 1,
                        next = null,
                        prev = null
                    ),
                    results = cachedCharacters.map { it.toCharacter() }
                )
            }
        }

        // Загружаем из API
        return try {
            val response = api.getCharacters(page)

            // Сохраняем в кеш только первую страницу при первой загрузке
            if (page == 1) {
                characterDao.clearAllCharacters()
                characterDao.insertCharacters(response.results.map { it.toEntity() })
            }

            response
        } catch (e: Exception) {
            // При ошибке пробуем вернуть кеш
            val cachedCharacters = characterDao.getAllCharacters()
            if (cachedCharacters.isNotEmpty()) {
                CharacterResponse(
                    info = ru.musindev.rickandmorty.data.models.PageInfo(
                        count = cachedCharacters.size,
                        pages = 1,
                        next = null,
                        prev = null
                    ),
                    results = cachedCharacters.map { it.toCharacter() }
                )
            } else {
                throw e
            }
        }
    }

    suspend fun getCharacterById(id: Int): CharacterDetails {
        // Сначала проверяем кеш
        val cachedCharacter = characterDao.getCharacterById(id)
        if (cachedCharacter != null && cachedCharacter.episode.isNotEmpty()) {
            return cachedCharacter.toCharacterDetails()
        }

        // Если в кеше нет или данные неполные - загружаем из API
        return try {
            val details = api.getCharacterById(id)
            // Сохраняем в кеш
            characterDao.insertCharacter(details.toEntity())
            details
        } catch (e: Exception) {
            // При ошибке возвращаем кеш если есть
            cachedCharacter?.toCharacterDetails() ?: throw e
        }
    }

    suspend fun searchCharactersInCache(query: String) =
        characterDao.searchCharacters(query).map { it.toCharacter() }
}