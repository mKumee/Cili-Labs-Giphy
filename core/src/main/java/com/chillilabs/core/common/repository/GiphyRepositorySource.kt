package com.chillilabs.core.common.repository

import com.chillilabs.core.data.GifPage

interface GiphyRepositorySource {
    suspend fun getTrending(offset: Int, limit: Int): GifPage
    suspend fun search(query: String, offset: Int, limit: Int): GifPage
}