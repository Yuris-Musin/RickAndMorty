package ru.musindev.rickandmorty.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.musindev.rickandmorty.ui.screens.CharacterDetailsScreen
import ru.musindev.rickandmorty.ui.screens.CharactersScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = "characters",
        modifier = Modifier.fillMaxSize().padding(8.dp)
    ) {
        composable("characters") {
            CharactersScreen(onCharacterClick = { characterId ->
                navController.navigate("characterDetails/$characterId")
            })
        }
        composable("characterDetails/{characterId}") { backStackEntry ->
            val characterId = backStackEntry.arguments?.getString("characterId")?.toInt() ?: 0
            CharacterDetailsScreen(
                characterId = characterId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
