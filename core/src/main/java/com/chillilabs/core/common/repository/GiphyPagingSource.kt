package com.chillilabs.core.common.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.chillilabs.core.data.Gif


class GiphyPagingSource(
    private val repository: GiphyRepositorySource,
    private val query: String?
) : PagingSource<Int, Gif>() {

    override fun getRefreshKey(state: PagingState<Int, Gif>): Int? {
        val anchor = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchor) ?: return null

        return page.prevKey?.plus(20) ?: page.nextKey?.minus(20)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Gif> {
        val offset = params.key ?: 0
        val limit = params.loadSize

        return try {
            val page = if (query.isNullOrBlank()) {
                repository.getTrending(offset, limit)
            } else {
                repository.search(query, offset, limit)
            }

            LoadResult.Page(
                data = page.items,
                prevKey = if (offset == 0) null else offset - limit,
                nextKey = if (page.endReached) null else offset + limit
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}