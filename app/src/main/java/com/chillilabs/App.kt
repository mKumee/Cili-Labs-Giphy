package com.chillilabs

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import coil.ImageLoader
import coil.ImageLoaderFactory
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), ImageLoaderFactory {
    @Inject
    lateinit var imageLoader: dagger.Lazy<ImageLoader>

    override fun onCreate() {
        super.onCreate()
    }

    override fun newImageLoader(): ImageLoader = imageLoader.get()


}
