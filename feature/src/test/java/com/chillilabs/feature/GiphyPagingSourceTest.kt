package com.chillilabs.feature

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import com.chillilabs.core.common.repository.GiphyPagingSource
import com.chillilabs.core.common.repository.GiphyRepositorySource
import com.chillilabs.core.data.GifPage
import com.chillilabs.feature.fake.FakeGiphyRepository
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalPagingApi::class)
class GiphyPagingSourceTest {

    private val repository = FakeGiphyRepository()

    @Test
    fun `load returns paging data`() = runTest {

        val pagingSource = GiphyPagingSource(repository, null)

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 0,
                loadSize = 2,
                placeholdersEnabled = false
            )
        )

        val page = result as PagingSource.LoadResult.Page

        assert(page.data.isNotEmpty())
        assert(page.nextKey != null)
    }

    @Test
    fun `error returns LoadResult Error`() = runTest {

        val repo = object : GiphyRepositorySource {
            override suspend fun getTrending(offset: Int, limit: Int): GifPage {
                throw RuntimeException("Network error")
            }

            override suspend fun search(query: String, offset: Int, limit: Int): GifPage {
                throw RuntimeException("Network error")
            }
        }

        val pagingSource = GiphyPagingSource(repo, null)

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 0,
                loadSize = 10,
                placeholdersEnabled = false
            )
        )

        assert(result is PagingSource.LoadResult.Error)
    }
}