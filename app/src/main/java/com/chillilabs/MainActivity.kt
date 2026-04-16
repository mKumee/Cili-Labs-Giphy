package com.chillilabs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.entryProvider
import com.chillilabs.core.navigation.Navigator
import com.chillilabs.core.navigation.rememberNavigationState
import com.chillilabs.core.navigation.toEntries
import com.chillilabs.feature.GiphyViewModel
import com.chillilabs.ui.theme.ChilliLabsGiphyTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation3.ui.NavDisplay
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChilliLabsGiphyTheme {
                val state = rememberNavigationState(
                    startKey = GiphyKey,
                    topLevelKeys = setOf(GiphyKey)
                )

                val navigator = remember { Navigator(state) }

                val entries = state.toEntries { key ->
                    entryProvider(key, navigator)
                }

                NavDisplay(
                    entries = entries,
                    onBack = { navigator.goBack() }
                )
            }
        }
    }
}