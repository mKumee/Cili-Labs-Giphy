package com.chillilabs.feature.fake

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.chillilabs.core.data.Gif

class FakeGifPagingSource(
    private val shouldFail: Boolean = false
) : PagingSource<Int, Gif>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Gif> {
        return if (shouldFail) {
            LoadResult.Error(RuntimeException("No internet connection"))
        } else {
            LoadResult.Page(
                data = listOf(
                    Gif(
                        id = "1",
                        title = "Cat",
                        url = "https://media.giphy.com/media/JIX9t2j0ZTN9S/giphy.gif"
                    ),
                    Gif(
                        id = "2",
                        title = "Dog",
                        url = "https://media.giphy.com/media/ICOgUNjpvO0PC/giphy.gif"
                    )
                ),
                prevKey = null,
                nextKey = null
            )
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Gif>): Int? = null
}