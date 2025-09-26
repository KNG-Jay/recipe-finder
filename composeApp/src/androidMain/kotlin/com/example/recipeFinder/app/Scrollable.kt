package com.example.recipeFinder.app

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun ScrollableLazyColumn(
    modifier: Modifier,
    lazyListState: LazyListState,
    content: @Composable () -> Unit
) {
    LazyColumn(
        modifier = modifier,
        state = lazyListState
    ) {
        item { content() } // Wrap the content in an item
    }
}
