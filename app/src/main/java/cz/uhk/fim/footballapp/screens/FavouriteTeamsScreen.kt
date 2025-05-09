package cz.uhk.fim.footballapp.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cz.uhk.fim.footballapp.api.ApiResult
import cz.uhk.fim.footballapp.viewmodels.FavouriteTeamsViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import cz.uhk.fim.footballapp.data.FavouriteTeamEntity
import cz.uhk.fim.footballapp.dialogs.DeleteFavouriteTeamConfirmDialog


@Composable
fun FavouriteMatchScreen(
    favouriteViewModel: FavouriteTeamsViewModel = koinViewModel(),
) {
    val favouriteListResult by favouriteViewModel.favouriteTeams.collectAsState()
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var teamToDelete by remember { mutableStateOf<FavouriteTeamEntity?>(null) }

    LaunchedEffect(Unit) {
        favouriteViewModel.loadFavouriteTeams()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Favourites",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(24.dp))

        when (favouriteListResult) {
            is ApiResult.Loading -> {
                CircularProgressIndicator()
            }

            is ApiResult.Success<*> -> {
                val favouriteList =
                    (favouriteListResult as ApiResult.Success<List<FavouriteTeamEntity>>).data
                if (favouriteList.isEmpty()) {
                    Text(text = "No favorite teams yet.")
                } else {
                    LazyColumn {
                        items(favouriteList) { team ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                AsyncImage(
                                    model = team.logo,
                                    contentDescription = "${team.name} logo",
                                    modifier = Modifier
                                        .size(48.dp),
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        team.name,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(team.country, fontSize = 16.sp, color = Color.Gray)
                                }
                                IconButton(
                                    onClick = {
                                        showDeleteConfirmDialog = true
                                        teamToDelete = team
                                    }
                                ) {
                                    Icon(
                                        Icons.Filled.Close,
                                        contentDescription = "Remove from Favorites"
                                    )
                                }
                            }

                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        }
                    }
                }
            }

            is ApiResult.Error -> {
                Text(text = "Error: ${(favouriteListResult as ApiResult.Error).message}")
            }
        }
    }

    if (showDeleteConfirmDialog && teamToDelete != null) {
        DeleteFavouriteTeamConfirmDialog(teamToDelete!!.name, onConfirmDelete = {
            favouriteViewModel.removeFavoriteTeam(teamToDelete!!.teamId)
            showDeleteConfirmDialog = false
            teamToDelete = null
        }, onDismiss = {
            showDeleteConfirmDialog = false
            teamToDelete = null
            Toast.makeText(
                context,
                "User canceled the deletion",
                Toast.LENGTH_SHORT
            ).show()
        })
    }

}
