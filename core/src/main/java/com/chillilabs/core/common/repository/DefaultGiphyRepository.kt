package com.chillilabs.core.common.repository

import com.chillilabs.core.common.ChilliDispatchers
import com.chillilabs.core.common.Dispatcher
import com.chillilabs.core.data.GifPage
import com.chillilabs.core.data.mappers.toDomain
import com.chillilabs.core.domain.di.GiphyApiKey
import com.chillilabs.core.network.retrofit.GiphyApi
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
class DefaultGiphyRepository @Inject constructor(
    private val api: GiphyApi,
    @GiphyApiKey private val apiKey: String,
    @Dispatcher(ChilliDispatchers.IO) private val io: CoroutineDispatcher
) : GiphyRepositorySource {

    override suspend fun getTrending(offset: Int, limit: Int): GifPage =
        withContext(io) {
            api.getTrendingGifs(apiKey, limit, offset).toDomain()
        }

    override suspend fun search(query: String, offset: Int, limit: Int): GifPage =
        withContext(io) {
            api.searchGifs(apiKey, query, limit, offset).toDomain()
        }
}