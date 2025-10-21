//package ru.musindev.rickandmorty.data.dao
//
//import ru.musindev.rickandmorty.data.entity.CharacterEntity
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//import kotlinx.coroutines.flow.Flow
//
//@Dao
//interface CharacterDao {
//
//    @Query("SELECT * FROM characters ORDER BY id ASC")
//    fun getAllCharacters(): Flow<List<CharacterEntity>>
//
//    @Query("SELECT * FROM characters WHERE id = :id")
//    fun getCharacterById(id: Int): Flow<CharacterEntity?>
//
//    @Query("SELECT * FROM characters WHERE id = :id")
//    suspend fun getCharacterByIdSync(id: Int): CharacterEntity?
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertCharacters(characters: List<CharacterEntity>)
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertCharacter(character: CharacterEntity)
//
//    @Query("DELETE FROM characters")
//    suspend fun clearAll()
//
//    @Query("SELECT COUNT(*) FROM characters")
//    suspend fun getCharactersCount(): Int
//
//    // Проверка устаревания кеша (например, старше 1 часа)
//    @Query("SELECT * FROM characters WHERE cachedAt < :timestamp")
//    suspend fun getStaleCharacters(timestamp: Long): List<CharacterEntity>
//}
