package ru.musindev.rickandmorty.data.mappers

import ru.musindev.rickandmorty.data.entity.CharacterEntity
import ru.musindev.rickandmorty.data.models.Character
import ru.musindev.rickandmorty.data.models.Location
import ru.musindev.rickandmorty.data.models.Origin
import ru.musindev.rickandmorty.domain.models.CharacterDetails

// Character -> CharacterEntity
fun Character.toEntity(): CharacterEntity {
    return CharacterEntity(
        id = id,
        name = name,
        status = status,
        species = species,
        type = "",
        gender = gender,
        image = image,
        originName = "",
        originUrl = "",
        locationName = "",
        locationUrl = "",
        episode = emptyList(),
        url = "",
        created = ""
    )
}

// CharacterEntity -> Character
fun CharacterEntity.toCharacter(): Character {
    return Character(
        id = id,
        name = name,
        status = status,
        species = species,
        gender = gender,
        image = image
    )
}

// CharacterDetails -> CharacterEntity
fun CharacterDetails.toEntity(): CharacterEntity {
    return CharacterEntity(
        id = id,
        name = name,
        status = status,
        species = species,
        type = type,
        gender = gender,
        image = image,
        originName = origin.name,
        originUrl = origin.url,
        locationName = location.name,
        locationUrl = location.url,
        episode = episode,
        url = url,
        created = created
    )
}

// CharacterEntity -> CharacterDetails
fun CharacterEntity.toCharacterDetails(): CharacterDetails {
    return CharacterDetails(
        id = id,
        name = name,
        status = status,
        species = species,
        type = type,
        gender = gender,
        origin = Origin(name = originName, url = originUrl),
        location = Location(name = locationName, url = locationUrl),
        image = image,
        episode = episode,
        url = url,
        created = created
    )
}
