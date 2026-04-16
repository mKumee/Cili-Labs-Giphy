package com.chillilabs.core.data.mappers

import com.chillilabs.core.data.Gif
import com.chillilabs.core.data.GifPage
import com.chillilabs.core.network.data.GiphyResponseDto
import com.chillilabs.core.utils.NetworkError

fun GiphyResponseDto.toDomain(): GifPage {
    return GifPage(
        items = data.map {
            Gif(
                id = it.id,
                title = it.title,
                url = it.images.original.url,
                previewUrl = it.images.fixedHeight?.url ?: it.images.original.url
            )
        },
        nextOffset = (pagination?.offset ?: 0) + (pagination?.count ?: data.size),
        totalCount = pagination?.totalCount ?: 0
    )
}
fun Throwable.toNetworkError(): NetworkError {
    return when (this) {
        is java.net.UnknownHostException -> NetworkError.NoInternet
        is java.net.SocketTimeoutException -> NetworkError.Timeout
        else -> NetworkError.Unknown(message)
    }
}