package com.chillilabs.core.data

sealed interface UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent
}