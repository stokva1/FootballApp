package cz.uhk.fim.footballapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cz.uhk.fim.footballapp.api.ApiResult
import cz.uhk.fim.footballapp.data.Team
import cz.uhk.fim.footballapp.viewmodels.FavouriteTeamsViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue


@Composable
fun FavouriteMatchScreen(
    navController: NavController,
    viewModel: FavouriteTeamsViewModel = koinViewModel()
) {
    val favouriteListResult by viewModel.favouriteTeams.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadFavouriteTeams()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.height(16.dp))

        when (favouriteListResult) {
            is ApiResult.Loading -> {
                CircularProgressIndicator()
            }

            is ApiResult.Success<*> -> {
                val favouriteList = (favouriteListResult as ApiResult.Success<List<Team>>).data
                if (favouriteList.isEmpty()) {
                    Text(text = "No favorite teams yet.")
                } else {
                    LazyColumn {
                        items(favouriteList) { team ->
                            Text(text = team.toString())
                        }
                    }
                }
            }

            is ApiResult.Error -> {
                Text(text = "Error: ${(favouriteListResult as ApiResult.Error).message}")
            }
        }
    }
}
