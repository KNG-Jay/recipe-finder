package com.example.recipeFinder.app

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
actual fun ScrollableLazyColumn(
    modifier: Modifier,
    lazyListState: LazyListState,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier) {
        LazyColumn(state = lazyListState) {
            item { content() } // Wrap the content in an item
        }
        VerticalScrollbar(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight(),
            adapter = rememberScrollbarAdapter(scrollState = lazyListState)
        )
    }
}
