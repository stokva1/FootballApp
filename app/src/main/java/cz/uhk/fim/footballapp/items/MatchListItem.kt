package cz.uhk.fim.footballapp.items

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import cz.uhk.fim.footballapp.consts.Routes
import cz.uhk.fim.footballapp.data.Match
import cz.uhk.fim.footballapp.utils.formatToLocalTime

@Composable
fun MatchListItem(match: Match, navController: NavController){
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                navController.navigate(Routes.matchDetail(match.matchData.id.toString()))
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {

        if (match.matchData.status.short in listOf("TBD", "NS")){
            Text(text = formatToLocalTime(match.matchData.date))
        } else if (match.matchData.status.short in listOf("1H", "HT", "2H", "ET", "BT", "P", "SUSP", "INT", "LIVE")){
//            Text(text = match.status)
//            Text(text = match.minute.toString())
            val infiniteTransition = rememberInfiniteTransition()
            val scale by infiniteTransition.animateFloat(
                initialValue = 0.8f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 1400,
                        easing = FastOutLinearInEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                )
            )

            Box(
                modifier = Modifier
                    .padding(end = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale
                        )
                        .clip(CircleShape)
                        .background(Color.Red.copy(alpha = 0.2f))
                )

                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Color.Red)
                )
            }
        } else if (match.matchData.status.short in listOf("FT", "AET", "PEN")){
            Text(text = "F")
        }else{
            Text(text = match.matchData.status.short)
        }


        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = match.teams.home.logo,
                    contentDescription = "${match.teams.home.name} icon",
                    modifier = Modifier.size(22.dp),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = match.teams.home.name,
                    fontWeight = if (match.teams.home.winner == true) FontWeight.Bold else FontWeight.Normal
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(text = (match.goals.home ?: 0).toString())
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = match.teams.away.logo,
                    contentDescription = "${match.teams.away.name} icon",
                    modifier = Modifier.size(22.dp),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = match.teams.away.name,
                    fontWeight = if (match.teams.away.winner == true) FontWeight.Bold else FontWeight.Normal
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(text = (match.goals.away ?: 0).toString())
            }
        }

        IconButton(onClick = {  navController.navigate(Routes.matchDetail(match.matchData.id.toString())) }) {
            Icon(Icons.Filled.Info, contentDescription = "Detail")
        }
    }
    HorizontalDivider(
        color = Color.LightGray.copy(alpha = 0.9f),
        thickness = 0.5.dp,
        modifier = Modifier.padding(start = 8.dp, end = 8.dp)
    )

}