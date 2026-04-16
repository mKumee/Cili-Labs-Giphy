package com.chillilabs.core.network.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GiphyResponseDto(
    val data: List<GifDto>,
    val pagination: PaginationDto? = null
)

@Serializable
data class GifDto(
    val id: String,
    val title: String,
    val url: String,
    val images: ImagesDto
)

@Serializable
data class ImagesDto(
    val original: ImageOriginalDto,
    @SerialName("fixed_height")
    val fixedHeight: ImageDto? = null
)

@Serializable
data class ImageDto(
    val url: String
)
@Serializable
data class ImageOriginalDto(
    val url: String
)

@Serializable
data class PaginationDto(
    @SerialName("total_count") val totalCount: Int = 0,
    val count: Int = 0,
    val offset: Int = 0
)