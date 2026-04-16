package com.chillilabs.core.domain

import com.chillilabs.core.common.repository.GiphyRepositorySource
import com.chillilabs.core.data.GifPage
import javax.inject.Inject

/** This usecases are just for demonstration in case of other adddig screens/cases not to call repository on viemodel
 * currently we are using pagination 2 PagingSource which is managed there.
 * */
class GetTrendingGifsUseCase @Inject constructor(
    private val repository: GiphyRepositorySource
) {
    suspend operator fun invoke(
        offset: Int = 0,
        limit: Int = 20
    ): GifPage {
        return repository.getTrending(offset, limit)
    }
}
class SearchGifsUseCase @Inject constructor(
    private val repository: GiphyRepositorySource
) {
    suspend operator fun invoke(
        query: String,
        offset: Int = 0,
        limit: Int = 20
    ): GifPage {
        return repository.search(query, offset, limit)
    }
}
class LoadMoreGifsUseCase @Inject constructor(
    private val repository: GiphyRepositorySource
) {

    suspend fun trending(offset: Int, limit: Int): GifPage {
        return repository.getTrending(offset, limit)
    }

    suspend fun search(query: String, offset: Int, limit: Int): GifPage {
        return repository.search(query, offset, limit)
    }
}