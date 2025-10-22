package ru.musindev.rickandmorty.data.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.musindev.rickandmorty.data.models.CharacterResponse
import ru.musindev.rickandmorty.domain.models.CharacterDetails

interface RickAndMortyApi {

    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int,
        @Query("status") status: String? = null,      // alive, dead, unknown
        @Query("species") species: String? = null,    // Human, Alien, etc
        @Query("type") type: String? = null,          // Rick's toxic side, etc
        @Query("gender") gender: String? = null,      // male, female, genderless, unknown
        @Query("name") name: String? = null           // search by name
    ): CharacterResponse

    @GET("character/{id}")
    suspend fun getCharacterById(
        @Path("id") id: Int
    ): CharacterDetails
}

//interface RickAndMortyApi {
//    @GET("character")
//    suspend fun getCharacters(
//        @Query("page") page: Int
//    ): CharacterResponse
//
//    @GET("character/{id}")
//    suspend fun getCharacterById(
//        @Path("id") id: Int
//    ): CharacterDetails
//}
