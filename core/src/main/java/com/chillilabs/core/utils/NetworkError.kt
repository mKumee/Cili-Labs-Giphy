package com.chillilabs.core.utils

sealed class NetworkError {
    data object NoInternet : NetworkError()
    data object Timeout : NetworkError()
    data object ServerError : NetworkError()
    data class Unknown(val message: String?) : NetworkError()
}