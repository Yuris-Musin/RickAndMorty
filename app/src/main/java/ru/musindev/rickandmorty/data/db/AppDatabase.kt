package ru.musindev.rickandmorty.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.musindev.rickandmorty.data.converters.Converters
import ru.musindev.rickandmorty.data.dao.CharacterDao
import ru.musindev.rickandmorty.data.entity.CharacterEntity

@Database(
    entities = [CharacterEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
}


