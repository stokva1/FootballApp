package cz.uhk.fim.footballapp.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import cz.uhk.fim.footballapp.viewmodels.FavouriteMatchViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavouriteMatchScreen(
    navController: NavController,
    viewModel: FavouriteMatchViewModel = koinViewModel()
) {

}