package ru.musindev.rickandmorty.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.musindev.rickandmorty.data.converters.Converters

@Entity(tableName = "characters")
@TypeConverters(Converters::class)
data class CharacterEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val image: String,
    val originName: String,
    val originUrl: String,
    val locationName: String,
    val locationUrl: String,
    val episode: List<String>,
    val url: String,
    val created: String,
    val timestamp: Long = System.currentTimeMillis() // Для cache invalidation
)