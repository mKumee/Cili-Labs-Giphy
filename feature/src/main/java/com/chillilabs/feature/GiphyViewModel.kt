package com.chillilabs.feature

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.chillilabs.core.common.repository.GiphyPagingSource
import com.chillilabs.core.common.repository.GiphyRepositorySource
import com.chillilabs.core.data.GiphyUiState
import com.chillilabs.core.data.UiEvent
import com.chillilabs.core.data.mappers.toNetworkError
import com.chillilabs.core.utils.NetworkError
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

@HiltViewModel
class GiphyViewModel @Inject constructor(
    private val repository: GiphyRepositorySource
) : ViewModel() {

    private val _events = MutableSharedFlow<UiEvent>()
    val events = _events.asSharedFlow()

    private val _uiState = MutableStateFlow(GiphyUiState())
    val uiState = _uiState.asStateFlow()

    val queryFlow = MutableStateFlow("")

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val pagingFlow = queryFlow
        .debounce(400)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            Pager(
                config = PagingConfig(
                    pageSize = 20,
                    prefetchDistance = 8,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = {
                    GiphyPagingSource(repository, query)
                }
            ).flow
        }
        .cachedIn(viewModelScope)

    fun onQueryChanged(query: String) {
        queryFlow.value = query
    }

    fun onPagingError(error: Throwable) {
        viewModelScope.launch {

            val message = when (error.toNetworkError()) {
                NetworkError.NoInternet -> "No internet connection"
                NetworkError.Timeout -> "Connection timeout"
                NetworkError.ServerError -> "Server error"
                is NetworkError.Unknown -> "Unknown error"
            }

            _uiState.value = _uiState.value.copy(
                loading = false,
                error = message
            )

            _events.emit(UiEvent.ShowSnackbar(message))
        }
    }
    fun setLoading(isLoading: Boolean) {
        _uiState.value = _uiState.value.copy(loading = isLoading)
    }
    @VisibleForTesting
    fun emitTestSnackbar(message: String) {
        viewModelScope.launch {
            _events.emit(UiEvent.ShowSnackbar(message))
        }
    }
    @VisibleForTesting
    fun setErrorForTest(message: String) {
        _uiState.value = _uiState.value.copy(
            error = message,
            loading = false
        )
    }
}