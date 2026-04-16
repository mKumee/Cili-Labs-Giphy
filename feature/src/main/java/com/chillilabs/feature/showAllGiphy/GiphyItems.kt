package com.chillilabs.feature.showAllGiphy

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.request.ImageRequest
import com.chillilabs.core.data.Gif
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import androidx.compose.ui.layout.ContentScale
import coil.compose.SubcomposeAsyncImage
import kotlin.math.absoluteValue

@Composable
fun GifItem(
    gif: Gif,
    onClick: () -> Unit
) {
    val ratio = remember(gif.id) {
        listOf(0.7f, 1f, 1.3f, 1.6f)[gif.id.hashCode().absoluteValue % 2]
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .background(Color.LightGray)
    ) {

        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(gif.url)
                .crossfade(true)
                .build(),

            contentDescription = gif.title,

            contentScale = ContentScale.Crop,

            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(ratio),

            loading = { GifItemSkeleton() },

            error = {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .background(Color.LightGray)
                )
            }
        )
    }
}
@Composable
fun GifItemSkeleton() {
    val shimmer = rememberShimmer(ShimmerBounds.View)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .shimmer(shimmer)
            .background(Color.LightGray)
    )
}