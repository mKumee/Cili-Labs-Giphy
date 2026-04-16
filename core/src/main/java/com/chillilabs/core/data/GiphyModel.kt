package com.chillilabs.core.data

data class Gif(
    val id: String,
    val title: String,
    val url: String,
    val previewUrl: String? = null
)

data class GifPage(
    val items: List<Gif>,
    val nextOffset: Int,
    val totalCount: Int
){
    val endReached: Boolean
        get() = items.isEmpty() || nextOffset >= totalCount
}

data class GiphyUiState(
    val loading: Boolean = false,
    val error: String? = null
)