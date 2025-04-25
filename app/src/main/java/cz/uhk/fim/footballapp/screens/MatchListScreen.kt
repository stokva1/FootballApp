package cz.uhk.fim.footballapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cz.uhk.fim.footballapp.api.ApiResult
import cz.uhk.fim.footballapp.items.MatchListItem
import cz.uhk.fim.footballapp.viewmodels.MatchViewModel
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

@Composable
fun MatchListScreen(
    navController: NavController,
    viewModel: MatchViewModel = koinViewModel(),
) {
    val listState = rememberLazyListState()
    val matchList by viewModel.matchList.collectAsState()
    val calendar = remember { mutableStateOf(Calendar.getInstance()) }
    val sdf = remember { SimpleDateFormat("d.M.") }
    val apiSdf = remember { SimpleDateFormat("yyyy-MM-dd") }

    val currentDate = sdf.format(calendar.value.time)


    LaunchedEffect(calendar.value.time) {
        val formattedDate = apiSdf.format(calendar.value.time)
        viewModel.getMatchList(formattedDate)
    }


    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Previous Day",
                tint = Color.Gray,
                modifier = Modifier.clickable {
                    calendar.value = (calendar.value.clone() as Calendar).apply {
                        add(Calendar.DATE, -1)
                    }
                }
            )

            Text(
                text = currentDate,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
            )

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Next Day",
                tint = Color.Gray,
                modifier = Modifier.clickable {
                    calendar.value = (calendar.value.clone() as Calendar).apply {
                        add(Calendar.DATE, 1)
                    }
                }
            )
        }
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