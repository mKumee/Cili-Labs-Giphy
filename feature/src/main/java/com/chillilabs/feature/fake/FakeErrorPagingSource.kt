package com.chillilabs.feature.fake

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.chillilabs.core.data.Gif

class FakeErrorPagingSource : PagingSource<Int, Gif>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Gif> {
        return LoadResult.Error(RuntimeException("No internet"))
    }

    override fun getRefreshKey(state: PagingState<Int, Gif>): Int? = null
}