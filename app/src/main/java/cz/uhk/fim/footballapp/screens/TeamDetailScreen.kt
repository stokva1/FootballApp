package cz.uhk.fim.footballapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import cz.uhk.fim.footballapp.api.ApiResult
import cz.uhk.fim.footballapp.viewmodels.FavouriteTeamsViewModel
import cz.uhk.fim.footballapp.viewmodels.TeamViewModel
import org.koin.androidx.compose.koinViewModel
import kotlin.collections.first
import androidx.compose.runtime.setValue


@Composable
fun TeamDetailScreen(
    teamId: Int,
    viewModel: TeamViewModel = koinViewModel(),
    favouriteViewModel: FavouriteTeamsViewModel = koinViewModel(),
) {
    val teamDetailResult by viewModel.teamDetail.collectAsState()
    val scrollState = rememberScrollState()
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    val favourites by favouriteViewModel.favouriteTeams.collectAsState()
    val isFavourite = if (favourites is ApiResult.Success) {
        (favourites as ApiResult.Success).data.any { it.teamId == teamId.toString() }
    } else {
        false
    }

    LaunchedEffect(Unit) {
        viewModel.getTeamDetail(teamId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (teamDetailResult) {
            is ApiResult.Loading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }

            is ApiResult.Success -> {
                val teamDetail = (teamDetailResult as ApiResult.Success).data.first()
                if (teamDetail != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = teamDetail.team.logo,
                            contentDescription = "${teamDetail.team.name} logo",
                            modifier = Modifier
                                .size(64.dp),
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                teamDetail.team.name,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(teamDetail.team.country, fontSize = 16.sp, color = Color.Gray)
                        }
                        IconButton(onClick = {
                            if (!isFavourite) {
                                favouriteViewModel.addFavoriteTeam(teamDetail)
                            } else {
                                showDeleteConfirmDialog = true
                            }
                        }) {
                            if (isFavourite) {
                                Icon(
                                    Icons.Filled.Favorite,
                                    contentDescription = "Remove from Favorites"
                                )
                            } else {
                                Icon(
                                    Icons.Filled.FavoriteBorder,
                                    contentDescription = "Add to Favorites"
                                )
                            }
                        }
                    }


                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    Text("Team Information", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                    InfoRow("Founded", teamDetail.team.founded.toString())
                    teamDetail.team.code?.let { InfoRow("Code", it) }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    Text("Stadium", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)

                    AsyncImage(
                        model = teamDetail.venue.image,
                        contentDescription = "${teamDetail.venue.name} image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .padding(vertical = 6.dp),
                        contentScale = ContentScale.Crop
                    )

                    InfoRow("Stadium Name", teamDetail.venue.name)
                    InfoRow("City", teamDetail.venue.city)
                    InfoRow("Capacity", "${teamDetail.venue.capacity} seats")
                    InfoRow("Surface", teamDetail.venue.surface)

                } else {
                    Text(text = "Match not found", style = MaterialTheme.typography.bodyLarge)
                }
            }

            is ApiResult.Error -> {
                val errorMessage = (teamDetailResult as ApiResult.Error).message
                Text(text = "Error: $errorMessage", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontWeight = FontWeight.Medium)
        Text(text = value, color = Color.DarkGray)
    }
}