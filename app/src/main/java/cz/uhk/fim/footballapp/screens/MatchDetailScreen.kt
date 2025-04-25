import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cz.uhk.fim.footballapp.api.ApiResult
import cz.uhk.fim.footballapp.items.MatchItem
import cz.uhk.fim.footballapp.viewmodels.MatchViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun MatchDetailScreen(
    matchId: Int,
    viewModel: MatchViewModel = koinViewModel(),
    navController: NavController,
) {
    val matchDetailResult by viewModel.matchDetail.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.getMatchDetail(matchId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (matchDetailResult) {
            is ApiResult.Loading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }

            is ApiResult.Success -> {
                val matchDetail = (matchDetailResult as ApiResult.Success).data
                if (matchDetail != null) {
                    MatchItem(matchDetail.first(), navController)
                } else {
                    Text(text = "Match not found", style = MaterialTheme.typography.bodyLarge)
                }
            }

            is ApiResult.Error -> {
                val errorMessage = (matchDetailResult as ApiResult.Error).message
                Text(text = "Error: $errorMessage", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }

}

@Composable
fun DetailRow(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
        Text(text = title, fontWeight = FontWeight.Bold)
        content()
        Spacer(modifier = Modifier.height(8.dp))
    }
}