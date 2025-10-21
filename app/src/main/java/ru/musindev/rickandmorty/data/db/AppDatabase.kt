//package ru.musindev.rickandmorty.data.db
//
//import androidx.room.Database
//import androidx.room.RoomDatabase
//import ru.musindev.rickandmorty.data.dao.CharacterDao
//import ru.musindev.rickandmorty.data.entity.CharacterEntity
//
//@Database(
//    entities = [CharacterEntity::class],
//    version = 1,
//    exportSchema = false
//)
//abstract class AppDatabase : RoomDatabase() {
//    abstract fun characterDao(): CharacterDao
//}