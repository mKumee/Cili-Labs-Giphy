package com.chillilabs.feature.fake

import com.chillilabs.core.common.repository.GiphyRepositorySource
import com.chillilabs.core.data.Gif
import com.chillilabs.core.data.GifPage

class FakeGiphyRepository(
    private val shouldFail: Boolean = false
) : GiphyRepositorySource {

    private val fakeGifs = listOf(
        Gif(
            id = "1",
            title = "Cat",
            url = "https://media.giphy.com/media/JIX9t2j0ZTN9S/giphy.gif"
        ),
        Gif(
            id = "2",
            title = "Dog",
            url = "https://media.giphy.com/media/ICOgUNjpvO0PC/giphy.gif"
        ),
        Gif(
            id = "3",
            title = "Dance",
            url = "https://media.giphy.com/media/l0MYt5jPR6QX5pnqM/giphy.gif"
        ),
        Gif(
            id = "4",
            title = "Thumbs Up",
            url = "https://media.giphy.com/media/111ebonMs90YLu/giphy.gif"
        )
    )

    override suspend fun getTrending(offset: Int, limit: Int): GifPage {
        if (shouldFail) throw RuntimeException("No internet connection")

        val items = fakeGifs.drop(offset).take(limit)

        return GifPage(
            items = items,
            nextOffset = offset + items.size,
            totalCount = fakeGifs.size
        )
    }

    override suspend fun search(query: String, offset: Int, limit: Int): GifPage {
        if (shouldFail) throw RuntimeException("No internet connection")

        val filtered = fakeGifs.filter {
            it.title.contains(query, ignoreCase = true)
        }

        val items = filtered.drop(offset).take(limit)

        return GifPage(
            items = items,
            nextOffset = offset + items.size,
            totalCount = filtered.size
        )
    }
}