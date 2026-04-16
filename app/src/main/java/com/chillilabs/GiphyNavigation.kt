package com.chillilabs

import android.util.Log
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import com.chillilabs.core.navigation.Navigator
import com.chillilabs.feature.GiphyViewModel
import com.chillilabs.feature.detailGiphy.GifDetailScreen
import kotlinx.serialization.Serializable
import com.chillilabs.feature.showAllGiphy.GiphyScreen

@Serializable
data object GiphyKey : NavKey

@Serializable
data object HomeKey : NavKey
@Serializable
data class GifDetailKey(
    val url: String,
    val title: String
) : NavKey


fun entryProvider(
    key: NavKey,
    navigator: Navigator
): NavEntry<NavKey> = when (key) {

    GiphyKey -> {

        NavEntry(key) {
            val viewModel: GiphyViewModel = hiltViewModel()
            GiphyScreen(
                viewModel = viewModel,
                onGifClick = { gif ->
                    navigator.navigate(
                        GifDetailKey(
                            url = gif.url,
                            title = gif.title
                        )
                    )
                }
            )
        }
    }

    is GifDetailKey -> {
        NavEntry(key) {
            GifDetailScreen(
                url = key.url,
                title = key.title,
                onBack = { navigator.goBack() }
            )
        }
    }

    else -> error("Unknown key: $key")
}