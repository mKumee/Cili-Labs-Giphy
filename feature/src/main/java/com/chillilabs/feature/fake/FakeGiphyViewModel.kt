package com.chillilabs.feature.fake

import com.chillilabs.core.common.repository.GiphyPagingSource
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.chillilabs.core.common.repository.GiphyRepositorySource
import com.chillilabs.core.data.Gif
import com.chillilabs.core.data.UiEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow

class FakeGiphyViewModel(
    repository: GiphyRepositorySource,
    shouldFail: Boolean = false
) : ViewModel() {

    val queryFlow = MutableStateFlow("")

    val pagingFlow: Flow<PagingData<Gif>> =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                GiphyPagingSource(
                    repository,
                    query = null
                )
            }
        ).flow

    private val _events = MutableSharedFlow<UiEvent>()
    val events = _events.asSharedFlow()

    fun onQueryChanged(query: String) {
        queryFlow.value = query
    }

    fun setLoading(isLoading: Boolean) {}

    fun onPagingError(error: Throwable) {}
}