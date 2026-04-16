package com.chillilabs.feature.showAllGiphy

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.chillilabs.core.data.Gif
import com.chillilabs.core.data.UiEvent
import com.chillilabs.feature.GiphyViewModel
import com.chillilabs.feature.R
import com.chillilabs.feature.design.AppColors
import com.chillilabs.feature.design.Dimens
import kotlinx.coroutines.flow.collectLatest

@Composable
fun GiphyScreen(
    viewModel: GiphyViewModel,
    onGifClick: (Gif) -> Unit
) {

    val pagingItems = viewModel.pagingFlow.collectAsLazyPagingItems()
    val query by viewModel.queryFlow.collectAsState()
    val gridState = rememberLazyStaggeredGridState()
    val snackbarHostState = remember { SnackbarHostState() }

    val isInitialLoading =
        pagingItems.loadState.refresh is LoadState.Loading &&
                pagingItems.itemCount == 0

    val refreshError =
        pagingItems.loadState.refresh as? LoadState.Error


    LaunchedEffect(Unit) {
        snapshotFlow { pagingItems.loadState }
            .collectLatest { loadState ->

                viewModel.setLoading(
                    loadState.refresh is LoadState.Loading
                )

                val error = when {
                    loadState.refresh is LoadState.Error ->
                        (loadState.refresh as LoadState.Error).error

                    loadState.append is LoadState.Error ->
                        (loadState.append as LoadState.Error).error

                    else -> null
                }

                error?.let(viewModel::onPagingError)
            }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {

                is UiEvent.ShowSnackbar -> {

                    val result = snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = "Retry",
                        duration = SnackbarDuration.Long
                    )

                    if (result ==
                        androidx.compose.material3.SnackbarResult.ActionPerformed
                    ) {
                        pagingItems.retry()
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        containerColor = AppColors.background
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(AppColors.background)
        ) {

            AppLogoHeader()

            GifSearchBar(
                value = query,
                onValueChange = viewModel::onQueryChanged,
                onClear = { viewModel.onQueryChanged("") }
            )

            when {

                isInitialLoading -> {
                    GifLoadingScreen()
                }

                refreshError != null &&
                        pagingItems.itemCount == 0 -> {

                    GifErrorScreen(
                        message = "No internet connection",
                        onRetry = { pagingItems.retry() }
                    )
                }

                else -> {
                    GifGrid(
                        pagingItems = pagingItems,
                        gridState = gridState,
                        onGifClick = onGifClick
                    )
                }
            }
        }
    }
}

@Composable
fun AppLogoHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = Dimens.padding_16,
                bottom = Dimens.padding_8
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.app_logo),
            contentDescription = "App Logo",
            modifier = Modifier.size(Dimens.logo_size)
        )
    }
}
@Composable
fun GifSearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    val imeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0

    LaunchedEffect(imeVisible) {
        if (!imeVisible) {
            focusManager.clearFocus(force = true)
        }
    }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp)
            .padding(horizontal = Dimens.padding_16),

        singleLine = true,
        shape = RoundedCornerShape(Dimens.radius_20),

        textStyle = MaterialTheme.typography.bodySmall.copy(
            lineHeight = 18.sp
        ),

        placeholder = {
            Text(
                text = "Search GIFs",
                color = AppColors.textSecondary,
                style = MaterialTheme.typography.bodySmall
            )
        },

        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = AppColors.textSecondary,
                modifier = Modifier.size(Dimens.icon_small)
            )
        },

        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = onClear) {
                    Text("✕", color = AppColors.textSecondary)
                }
            }
        },

        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),

        keyboardActions = KeyboardActions(
            onSearch = {
                focusManager.clearFocus(force = true)
            },
            onDone = {
                focusManager.clearFocus(force = true)
            }
        ),

        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AppColors.searchBorderFocused,
            unfocusedBorderColor = AppColors.searchBorderUnfocused,
            cursorColor = AppColors.searchBorderFocused,
            focusedTextColor = AppColors.textPrimary,
            unfocusedTextColor = AppColors.textPrimary
        )
    )
}
@Composable
fun GifGrid(
    pagingItems: androidx.paging.compose.LazyPagingItems<Gif>,
    gridState: LazyStaggeredGridState,
    onGifClick: (Gif) -> Unit
) {

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        state = gridState,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp),

        contentPadding = PaddingValues(
            start = 8.dp,
            end = 8.dp,
            bottom = 8.dp
        ),

        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalItemSpacing = 8.dp
    ){
        items(
            count = pagingItems.itemCount,
            key = pagingItems.itemKey { it.id }
        ) { index ->

            val item = pagingItems[index]

            if (item == null) {
                GifItemSkeleton()
            } else {
                GifItem(
                    gif = item,
                    onClick = { onGifClick(item) }
                )
            }
        }

        if (pagingItems.loadState.append is LoadState.Loading) {
            item(span = StaggeredGridItemSpan.FullLine) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens.padding_16),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun GifLoadingScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun GifErrorScreen(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = message,
            color = AppColors.textPrimary
        )

        Button(
            onClick = onRetry,
            modifier = Modifier.padding(top = Dimens.padding_12)
        ) {
            Text("Retry")
        }
    }
}