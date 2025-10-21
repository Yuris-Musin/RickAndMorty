package ru.musindev.rickandmorty.data.models

data class PageInfo(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)