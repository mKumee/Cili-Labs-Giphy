package com.chillilabs.feature

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.chillilabs.feature.fake.FakeGiphyRepository
import com.chillilabs.feature.showAllGiphy.GiphyScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class GiphyScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var viewModel: GiphyViewModel

    @Before
    fun setup() {
        hiltRule.inject()

        viewModel = GiphyViewModel(FakeGiphyRepository())
    }

    @Test
    fun searchBar_isVisible_onLaunch() {
        composeRule.setContent {
            GiphyScreen(
                viewModel = viewModel,
                onGifClick = {}
            )
        }

        composeRule.onNodeWithText("Search GIFs")
            .assertExists()
    }

    @Test
    fun snackbar_is_shown() {

        composeRule.setContent {
            GiphyScreen(viewModel = viewModel, onGifClick = {})
        }

        composeRule.runOnUiThread {
            viewModel.emitTestSnackbar("No internet")
        }

        composeRule.onNodeWithText("No internet")
            .assertIsDisplayed()
    }

    @Test
    fun snackbar_isShown_whenEventEmitted() {
        composeRule.setContent {
            GiphyScreen(
                viewModel = viewModel,
                onGifClick = {}
            )
        }

        composeRule.runOnUiThread {
            viewModel.onPagingError(Exception("No internet"))
        }

        composeRule.onNodeWithText("Retry")
            .assertExists()
    }
}