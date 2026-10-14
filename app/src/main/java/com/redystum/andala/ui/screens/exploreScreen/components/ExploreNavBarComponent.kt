package com.redystum.andala.ui.screens.exploreScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ExploreNavBar(
    onSearchQueryChanged: (String, Boolean) -> Unit,
    logoPainter: Painter,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    BasicTextField(
        value = searchQuery,
        onValueChange = {
            searchQuery = it
            onSearchQueryChanged(it, true)
        },
        singleLine = true,
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.primary,
            fontSize = 16.sp
        ),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(50.dp)
                    )
                    .padding(horizontal = 14.dp)
                    .focusRequester(focusRequester),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    painter = logoPainter,
                    contentDescription = "App Logo",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(36.dp)
                )
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (searchQuery.isEmpty()) {
                        Text(
                            text = "Procurar pousadas...",
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                    alpha = 0.6f
                                ),
                                fontSize = 14.sp
                            )
                        )
                    }
                    innerTextField()
                }

                IconButton(
                    enabled = searchQuery.isNotEmpty(),
                    onClick = {
                        searchQuery = ""
                        onSearchQueryChanged("", false)
                        focusManager.clearFocus()
                        focusRequester.freeFocus()
                    },
                    modifier = Modifier.focusRequester(focusRequester).size(36.dp),
                    shape = RoundedCornerShape(50),
                    colors = IconButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                        disabledContainerColor = Color.Transparent,
                        disabledContentColor = Color.Transparent,
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear Search",
                        modifier = Modifier.size(18.dp)
                    )
                }

            }
        },
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}