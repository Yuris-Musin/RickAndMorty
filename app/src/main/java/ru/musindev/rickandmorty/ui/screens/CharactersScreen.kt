package ru.musindev.rickandmorty.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import ru.musindev.rickandmorty.ui.theme.BackgroundColor
import ru.musindev.rickandmorty.ui.viewmodel.CharactersViewModel

@Composable
fun CharactersScreen(
    viewModel: CharactersViewModel = hiltViewModel(),
    onCharacterClick: (Int) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredCharacters = viewModel.filterCharacters(searchQuery)
    val isLoading by viewModel.isLoading
    val isRefreshing by viewModel.isRefreshing

    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(
            value = searchQuery,
            onValueChange = { searchQuery = it }
        )
        @Suppress("DEPRECATION")
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = { viewModel.refreshCharacters() },
            modifier = Modifier.fillMaxSize()
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(filteredCharacters.size) { index ->
                        if (index >= viewModel.characters.size - 1 && !isRefreshing) {
                            viewModel.loadCharacters()
                        }
                        CharacterItem(
                            character = filteredCharacters[index],
                            onClick = { onCharacterClick(filteredCharacters[index].id) }
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    FloatingActionButton(
                        onClick = { },
                        containerColor = BackgroundColor,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp),
                        shape = CircleShape
                    ) {
                        Canvas(
                            modifier = Modifier.size(36.dp),
                        ) {
                            val widths = listOf(
                                size.width * 0.7f,
                                size.width * 0.5f,
                                size.width * 0.3f
                            )
                            val lineHeight = size.height * 0.11f // Толщина линии, уменьшена
                            val spacing =
                                size.height * 0.09f    // Вертикальный отступ между полосками
                            val startY = (size.height - (3 * lineHeight + 2 * spacing)) / 2

                            widths.forEachIndexed { i, w ->
                                drawRoundRect(
                                    color = Color.White,
                                    topLeft = Offset(
                                        (size.width - w) / 2, // По центру
                                        startY + i * (lineHeight + spacing)
                                    ),
                                    size = Size(w, lineHeight),
                                    cornerRadius = CornerRadius(lineHeight / 2)
                                )
                            }
                        }
                    }
                }


                if (isLoading && !isRefreshing) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

