package ru.musindev.rickandmorty.domain.models

data class CharacterFilters(
    val status: String? = null,
    val species: String? = null,
    val type: String? = null,
    val gender: String? = null
) {
    fun isActive(): Boolean {
        return status != null || species != null || type != null || gender != null
    }

    fun clear(): CharacterFilters {
        return CharacterFilters()
    }
}
