//package ru.musindev.rickandmorty.data.entity
//
//import androidx.room.Embedded
//import androidx.room.Entity
//import androidx.room.PrimaryKey
//import ru.musindev.rickandmorty.data.models.Character
//import ru.musindev.rickandmorty.data.models.Location
//import ru.musindev.rickandmorty.domain.models.CharacterDetails
//
//@Entity(tableName = "characters")
//data class CharacterEntity(
//    @PrimaryKey val id: Int,
//    val name: String,
//    val status: String,
//    val species: String,
//    val type: String,
//    val gender: String,
//    @Embedded(prefix = "origin_") val origin: LocationEntity,
//    @Embedded(prefix = "location_") val location: LocationEntity,
//    val image: String,
//    val episode: String, // Сохраним как JSON строку
//    val url: String,
//    val created: String,
//    val cachedAt: Long = System.currentTimeMillis() // Время кеширования
//)
//
//data class LocationEntity(
//    val name: String,
//    val url: String
//)
//
//// Маппинг между Entity и Domain/Data моделями
//fun CharacterEntity.toCharacter(): CharacterDetails {
//    return CharacterDetails(
//        id = id,
//        name = name,
//        status = status,
//        species = species,
//        type = type,
//        gender = gender,
//        origin = Location(
//            name = origin.name,
//            url = origin.url
//        ),
//        location = ru.musindev.rickandmorty.data.models.Location(
//            name = location.name,
//            url = location.url
//        ),
//        image = image,
//        episode = episode.split(",").filter { it.isNotEmpty() },
//        url = url,
//        created = created
//    )
//}
//
//fun Character.toEntity(): CharacterEntity {
//    return CharacterEntity(
//        id = id,
//        name = name,
//        status = status,
//        species = species,
//        type = type,
//        gender = gender,
//        origin = LocationEntity(name = origin.name, url = origin.url),
//        location = LocationEntity(name = location.name, url = location.url),
//        image = image,
//        episode = episode.joinToString(","),
//        url = url,
//        created = created
//    )
//}
//
//fun CharacterEntity.toCharacterDetails(): CharacterDetails {
//    return CharacterDetails(
//        id = id,
//        name = name,
//        status = status,
//        species = species,
//        type = type,
//        gender = gender,
//        origin = Location(
//            name = origin.name,
//            url = origin.url
//        ),
//        location = Location(
//            name = location.name,
//            url = location.url
//        ),
//        image = image,
//        episode = episode.split(",").filter { it.isNotEmpty() },
//        url = url,
//        created = created
//    )
//}
//
//fun CharacterDetails.toEntity(): CharacterEntity {
//    return CharacterEntity(
//        id = id,
//        name = name,
//        status = status,
//        species = species,
//        type = type,
//        gender = gender,
//        origin = LocationEntity(name = origin.name, url = origin.url),
//        location = LocationEntity(name = location.name, url = location.url),
//        image = image,
//        episode = episode.joinToString(","),
//        url = url,
//        created = created
//    )
//}
