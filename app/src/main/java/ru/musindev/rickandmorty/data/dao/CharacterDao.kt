package ru.musindev.rickandmorty.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.musindev.rickandmorty.data.entity.CharacterEntity

@Dao
interface CharacterDao {

    @Query("SELECT * FROM characters ORDER BY id ASC")
    suspend fun getAllCharacters(): List<CharacterEntity>

    @Query("SELECT * FROM characters WHERE id = :characterId")
    suspend fun getCharacterById(characterId: Int): CharacterEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<CharacterEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: CharacterEntity)

    @Query("DELETE FROM characters")
    suspend fun clearAllCharacters()

    @Query("SELECT COUNT(*) FROM characters")
    suspend fun getCharactersCount(): Int

    // Для поиска персонажей
    @Query("SELECT * FROM characters WHERE name LIKE '%' || :query || '%' ORDER BY id ASC")
    suspend fun searchCharacters(query: String): List<CharacterEntity>
}