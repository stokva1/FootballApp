package cz.uhk.fim.footballapp.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cz.uhk.fim.footballapp.api.ApiResult
import cz.uhk.fim.footballapp.items.MatchListItem
import cz.uhk.fim.footballapp.viewmodels.MatchViewModel
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun MatchListScreen(
    navController: NavController,
    viewModel: MatchViewModel = koinViewModel(),
//    favoriteViewModel: FavouriteCryptoViewModel = koinViewModel()
) {
    val listState = rememberLazyListState()
    val matchList by viewModel.matchList.collectAsState()
//    val favourites by favoriteViewModel.favouriteCryptos.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getMatchList()
//        favoriteViewModel.loadFavouriteCryptos()
    }

    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        val sdf = SimpleDateFormat("d.M.")
        val currentDate = sdf.format(Date())
        Text(text = currentDate,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(8.dp))
        when (matchList) {
            is ApiResult.Loading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }

            is ApiResult.Success -> {

                val matchList = (matchList as ApiResult.Success).data
                LazyColumn(state = listState) {
                    items(matchList) { match ->
                        MatchListItem(
                            match = match,
                            navController = navController,
                        )
                    }
                }
            }

            is ApiResult.Error -> {
                val errorMessage = (matchList as ApiResult.Error).message
                Text(text = "Error: $errorMessage")
            }
        }
    }
}