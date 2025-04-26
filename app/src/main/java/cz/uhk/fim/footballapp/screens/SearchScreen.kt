package cz.uhk.fim.footballapp.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cz.uhk.fim.footballapp.api.ApiResult
import cz.uhk.fim.footballapp.viewmodels.TeamViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import cz.uhk.fim.footballapp.consts.Routes
import cz.uhk.fim.footballapp.data.Team


@Composable
fun SearchScreen(
    navController: NavController,
    teamViewModel: TeamViewModel = koinViewModel()
) {
    var query by remember { mutableStateOf("") }
    val teamListResult by teamViewModel.teamList.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    var wasSearched by remember { mutableStateOf(false) } // Přidáme kontrolu

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Search teams") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (query.length >= 2) {
                    teamViewModel.getTeamListByName(query)
                    keyboardController?.hide()
                    wasSearched = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (teamListResult) {
            is ApiResult.Loading -> {
                CircularProgressIndicator()
            }

            is ApiResult.Error -> {
                Text(text = "Error: ${(teamListResult as ApiResult.Error).message}")
            }

            is ApiResult.Success<*> -> {
                val teams = (teamListResult as ApiResult.Success<List<Team>>).data
                println(teams)
                if (teams.isEmpty() && wasSearched) {
                    Text(text = "No teams found.")
                } else {
                    LazyColumn {
                        items(teams) { team ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        navController.navigate(Routes.teamDetail(team.team.id.toString()))
                                    }
                                    .padding(8.dp)) {
                                AsyncImage(
                                    model = team.team.logo,
                                    contentDescription = "${team.team.name} logo",
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .border(2.dp, Color.LightGray, CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        team.team.name,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(team.team.country, fontSize = 16.sp, color = Color.Gray)
                                }
                            }

                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        }
                    }
                }
            }
        }
    }
}
