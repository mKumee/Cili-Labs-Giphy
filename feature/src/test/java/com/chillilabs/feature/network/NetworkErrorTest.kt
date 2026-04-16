package com.chillilabs.feature.network

import com.chillilabs.core.data.mappers.toNetworkError
import com.chillilabs.core.utils.NetworkError
import org.junit.Test
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class NetworkErrorTest {

    @Test
    fun `unknown host maps to NoInternet`() {
        val error = UnknownHostException().toNetworkError()

        assert(error is NetworkError.NoInternet)
    }

    @Test
    fun `timeout maps correctly`() {
        val error = SocketTimeoutException().toNetworkError()

        assert(error is NetworkError.Timeout)
    }
}