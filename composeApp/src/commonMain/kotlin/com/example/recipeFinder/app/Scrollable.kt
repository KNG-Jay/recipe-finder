package com.example.recipeFinder.app

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun ScrollableLazyColumn(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState,
    content: @Composable () -> Unit
)