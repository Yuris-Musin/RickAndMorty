package ru.musindev.rickandmorty.ui.screens

import androidx.compose.foundation.Canvas
import ru.musindev.rickandmorty.ui.components.SearchBar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import kotlinx.coroutines.launch
import ru.musindev.rickandmorty.ui.components.CharacterItem
import ru.musindev.rickandmorty.ui.components.EmptyState
import ru.musindev.rickandmorty.ui.components.FilterBottomSheet
import ru.musindev.rickandmorty.ui.theme.BackgroundColor
import ru.musindev.rickandmorty.ui.viewmodel.CharactersViewModel

@Composable
fun CharactersScreen(
    viewModel: CharactersViewModel = hiltViewModel(),
    onCharacterClick: (Int) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var showFilterSheet by remember { mutableStateOf(false) }

    val filteredCharacters = viewModel.filterCharacters(searchQuery)
    val isLoading by viewModel.isLoading
    val isRefreshing by viewModel.isRefreshing
    val currentFilters by viewModel.filters
    val isOfflineMode by viewModel.isOfflineMode
    val error by viewModel.error

    val listState = rememberLazyGridState()

    val coroutineScope = rememberCoroutineScope()

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
                // Проверка на пустой результат
                when {
                    // Загрузка
                    isLoading && filteredCharacters.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    // Ошибка загрузки
                    error != null && filteredCharacters.isEmpty() -> {
                        EmptyState(
                            title = "Error",
                            message = error ?: "Unknown error occurred",
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp),
                                    tint = MaterialTheme.colorScheme.error
                                )
                            },
                            actionButton = {
                                Button(onClick = { viewModel.refreshCharacters() }) {
                                    Text("Retry")
                                }
                            }
                        )
                    }

                    // Поиск не дал результатов
                    searchQuery.isNotBlank() && filteredCharacters.isEmpty() -> {
                        EmptyState(
                            title = "No results found",
                            message = "We couldn't find any characters matching \"$searchQuery\"",
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp),
                                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                                )
                            },
                            actionButton = {
                                OutlinedButton(onClick = { searchQuery = "" }) {
                                    Text("Clear search")
                                }
                            }
                        )
                    }

                    // Фильтры не дали результатов
                    currentFilters.isActive() && filteredCharacters.isEmpty() && searchQuery.isBlank() -> {
                        EmptyState(
                            title = "No characters found",
                            message = "Try adjusting your filters to see more results",
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Face,
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp),
                                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                                )
                            },
                            actionButton = {
                                OutlinedButton(onClick = { viewModel.clearFilters() }) {
                                    Text("Clear filters")
                                }
                            }
                        )
                    }

                    // Показываем список
                    else -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            state = listState,
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(8.dp)
                        ) {
                            items(filteredCharacters.size) { index ->
                                if (index >= viewModel.characters.size - 1 && !isRefreshing && !isOfflineMode) {
                                    viewModel.loadCharacters()
                                }
                                CharacterItem(
                                    character = filteredCharacters[index],
                                    onClick = { onCharacterClick(filteredCharacters[index].id) }
                                )
                            }
                        }
                    }
                }

                // FAB для фильтров (всегда видна)
                FloatingActionButton(
                    onClick = { showFilterSheet = true },
                    containerColor = if (currentFilters.isActive())
                        MaterialTheme.colorScheme.primary
                    else
                        BackgroundColor,
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
                        val lineHeight = size.height * 0.11f
                        val spacing = size.height * 0.09f
                        val startY = (size.height - (3 * lineHeight + 2 * spacing)) / 2

                        widths.forEachIndexed { i, w ->
                            drawRoundRect(
                                color = Color.White,
                                topLeft = Offset(
                                    (size.width - w) / 2,
                                    startY + i * (lineHeight + spacing)
                                ),
                                size = Size(w, lineHeight),
                                cornerRadius = CornerRadius(lineHeight / 2)
                            )
                        }
                    }
                }
                if (filteredCharacters.isNotEmpty()) {
                    FloatingActionButton(
                        onClick = {
                            coroutineScope.launch {
                                listState.scrollToItem(0)
                            }
                        },
                        containerColor = BackgroundColor,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp),
                        shape = CircleShape
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = "Scroll to Top",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }

    // Bottom Sheet с фильтрами
    if (showFilterSheet) {
        FilterBottomSheet(
            currentFilters = currentFilters,
            onDismiss = { showFilterSheet = false },
            onApply = { filters ->
                viewModel.applyFilters(filters)
            },
            onClear = {
                viewModel.clearFilters()
            }
        )
    }
}

//@Composable
//fun CharactersScreen(
//    viewModel: CharactersViewModel = hiltViewModel(),
//    onCharacterClick: (Int) -> Unit
//) {
//    var searchQuery by remember { mutableStateOf("") }
//    var showFilterSheet by remember { mutableStateOf(false) }
//
//    val filteredCharacters = viewModel.filterCharacters(searchQuery)
//    val isLoading by viewModel.isLoading
//    val isRefreshing by viewModel.isRefreshing
//    val currentFilters by viewModel.filters
//    val isOfflineMode by viewModel.isOfflineMode
//
//    Column(modifier = Modifier.fillMaxSize()) {
//        SearchBar(
//            value = searchQuery,
//            onValueChange = { searchQuery = it }
//        )
//
//        @Suppress("DEPRECATION")
//        SwipeRefresh(
//            state = rememberSwipeRefreshState(isRefreshing),
//            onRefresh = { viewModel.refreshCharacters() },
//            modifier = Modifier.fillMaxSize()
//        ) {
//            Box(modifier = Modifier.fillMaxSize()) {
//                LazyVerticalGrid(
//                    columns = GridCells.Fixed(2),
//                    modifier = Modifier.fillMaxSize(),
//                    contentPadding = PaddingValues(8.dp)
//                ) {
//                    items(filteredCharacters.size) { index ->
//                        if (index >= viewModel.characters.size - 1 && !isRefreshing && !isOfflineMode) {
//                            viewModel.loadCharacters()
//                        }
//                        CharacterItem(
//                            character = filteredCharacters[index],
//                            onClick = { onCharacterClick(filteredCharacters[index].id) }
//                        )
//                    }
//                }
//
//                Box(
//                    modifier = Modifier.fillMaxSize()
//                ) {
//                    // FAB для фильтров
//                    FloatingActionButton(
//                        onClick = { showFilterSheet = true },
//                        containerColor = if (currentFilters.isActive())
//                            MaterialTheme.colorScheme.primary
//                        else
//                            BackgroundColor,
//                        modifier = Modifier
//                            .align(Alignment.BottomEnd)
//                            .padding(16.dp),
//                        shape = CircleShape
//                    ) {
//                        Canvas(
//                            modifier = Modifier.size(36.dp),
//                        ) {
//                            val widths = listOf(
//                                size.width * 0.7f,
//                                size.width * 0.5f,
//                                size.width * 0.3f
//                            )
//                            val lineHeight = size.height * 0.11f
//                            val spacing = size.height * 0.09f
//                            val startY = (size.height - (3 * lineHeight + 2 * spacing)) / 2
//
//                            widths.forEachIndexed { i, w ->
//                                drawRoundRect(
//                                    color = Color.White,
//                                    topLeft = Offset(
//                                        (size.width - w) / 2,
//                                        startY + i * (lineHeight + spacing)
//                                    ),
//                                    size = Size(w, lineHeight),
//                                    cornerRadius = CornerRadius(lineHeight / 2)
//                                )
//                            }
//                        }
//                    }
//                }
//
//                if (isLoading && !isRefreshing) {
//                    Box(
//                        modifier = Modifier.fillMaxSize(),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        CircularProgressIndicator()
//                    }
//                }
//            }
//        }
//    }
//
//    // Bottom Sheet с фильтрами
//    if (showFilterSheet) {
//        FilterBottomSheet(
//            currentFilters = currentFilters,
//            onDismiss = { showFilterSheet = false },
//            onApply = { filters ->
//                viewModel.applyFilters(filters)
//            },
//            onClear = {
//                viewModel.clearFilters()
//            }
//        )
//    }
//}
