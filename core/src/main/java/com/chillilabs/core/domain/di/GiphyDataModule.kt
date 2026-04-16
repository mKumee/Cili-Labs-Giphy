package com.chillilabs.core.domain.di

import com.chillilabs.core.BuildConfig
import com.chillilabs.core.common.repository.DefaultGiphyRepository
import com.chillilabs.core.common.repository.GiphyRepositorySource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
abstract class GiphyDataModule {

    @Binds
    abstract fun bindRepository(
        impl: DefaultGiphyRepository
    ): GiphyRepositorySource
}

@Module
@InstallIn(SingletonComponent::class)
object GiphyConfigModule {

    @Provides
    @GiphyApiKey
    fun provideApiKey(): String = BuildConfig.GIPHY_API_KEY
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GiphyApiKey