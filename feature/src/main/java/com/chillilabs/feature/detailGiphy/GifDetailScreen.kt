package com.chillilabs.feature.detailGiphy

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.chillilabs.feature.R
import com.chillilabs.feature.design.AppColors
import com.chillilabs.feature.design.Dimens

@Composable
fun GifDetailScreen(
    url: String,
    title: String,
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.background)
            .verticalScroll(scrollState)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.padding_12)
        ) {

            IconButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = AppColors.textPrimary
                )
            }

            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(Dimens.logo_size)
                )
            }
        }

        Spacer(modifier = Modifier.height(Dimens.padding_12))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.padding_16),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = url,
                contentDescription = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.detail_image_height)
                    .clip(RoundedCornerShape(Dimens.radius_20)),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(Dimens.padding_16))

        Text(
            text = title,
            color = AppColors.textPrimary,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = Dimens.padding_20)
        )

        Spacer(modifier = Modifier.height(Dimens.padding_20))
    }
}