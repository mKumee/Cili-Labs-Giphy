package com.chillilabs.core.network.retrofit

import com.chillilabs.core.network.data.GiphyResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyApi {

    @GET("gifs/trending")
    suspend fun getTrendingGifs(
        @Query("api_key") apiKey: String,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0,
        @Query("rating") rating: String = "g"
    ): GiphyResponseDto

    @GET("gifs/search")
    suspend fun searchGifs(
        @Query("api_key") apiKey: String,
        @Query("q") query: String,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0,
        @Query("rating") rating: String = "g",
        @Query("lang") lang: String = "en"
    ): GiphyResponseDto
}