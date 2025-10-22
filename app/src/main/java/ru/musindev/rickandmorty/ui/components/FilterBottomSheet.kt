package ru.musindev.rickandmorty.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.musindev.rickandmorty.domain.models.CharacterFilters

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    currentFilters: CharacterFilters,
    onDismiss: () -> Unit,
    onApply: (CharacterFilters) -> Unit,
    onClear: () -> Unit
) {
    var selectedStatus by remember { mutableStateOf(currentFilters.status) }
    var selectedSpecies by remember { mutableStateOf(currentFilters.species) }
    var selectedType by remember { mutableStateOf(currentFilters.type) }
    var selectedGender by remember { mutableStateOf(currentFilters.gender) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxHeight() // Полный экран
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Заголовок с крестиком
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filters",
                    style = MaterialTheme.typography.headlineMedium
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = {
                        selectedStatus = null
                        selectedSpecies = null
                        selectedType = null
                        selectedGender = null
                    }) {
                        Text("Clear All")
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp))

            // Скроллируемый контент фильтров
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                // Фильтр по статусу
                FilterSection(
                    title = "Status",
                    options = listOf("Alive", "Dead", "Unknown"),
                    selectedOption = selectedStatus?.replaceFirstChar { it.uppercase() },
                    onOptionSelected = { selectedStatus = it?.lowercase() }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Фильтр по видам
                FilterSection(
                    title = "Species",
                    options = listOf(
                        "Human",
                        "Alien",
                        "Humanoid",
                        "Poopybutthole",
                        "Mythological Creature",
                        "Animal",
                        "Robot",
                        "Cronenberg",
                        "Disease"
                    ),
                    selectedOption = selectedSpecies,
                    onOptionSelected = { selectedSpecies = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Фильтр по типу
                FilterSection(
                    title = "Type",
                    options = listOf(
                        "Genetic experiment",
                        "Parasite",
                        "Rick's toxic side",
                        "Morty's toxic side",
                        "Clone",
                        "Superhuman"
                    ),
                    selectedOption = selectedType,
                    onOptionSelected = { selectedType = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Фильтр по полу
                FilterSection(
                    title = "Gender",
                    options = listOf("Male", "Female", "Genderless", "Unknown"),
                    selectedOption = selectedGender?.replaceFirstChar { it.uppercase() },
                    onOptionSelected = { selectedGender = it?.lowercase() }
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Кнопка применить (фиксированная внизу)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Button(
                    onClick = {
                        val filters = CharacterFilters(
                            status = selectedStatus,
                            species = selectedSpecies,
                            type = selectedType,
                            gender = selectedGender
                        )
                        onApply(filters)
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Apply Filters")
                }

                // Показываем количество активных фильтров
                val activeFiltersCount = listOf(
                    selectedStatus,
                    selectedSpecies,
                    selectedType,
                    selectedGender
                ).count { it != null }

                if (activeFiltersCount > 0) {
                    Text(
                        text = "$activeFiltersCount ${if (activeFiltersCount == 1) "filter" else "filters"} selected",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 8.dp)
                    )
                }
            }
        }
    }
}
