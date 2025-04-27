package cz.uhk.fim.footballapp.items

import android.widget.ImageView
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import cz.uhk.fim.footballapp.consts.Routes
import cz.uhk.fim.footballapp.data.Match
import cz.uhk.fim.footballapp.utils.formatToLocalDateTime

@Composable
fun MatchItem(match: Match, navController: NavController) {
    val status = when {
        match.matchData.status.short in listOf("TBD", "NS") -> "S"
        match.matchData.status.short in listOf(
            "1H",
            "HT",
            "2H",
            "ET",
            "BT",
            "P",
            "SUSP",
            "INT",
            "LIVE"
        ) -> "L"

        match.matchData.status.short in listOf("FT", "AET", "PEN") -> "F"
        else -> "none"
    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
            ) {
                AsyncImage(
                    model = match.teams.home.logo,
                    contentDescription = "${match.teams.home.name} icon",
                    modifier = Modifier
                        .size(64.dp)
                        .clickable {
                            navController.navigate(Routes.teamDetail(match.teams.home.id.toString()))
                        },
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = match.teams.home.name,
                    fontWeight = if (match.teams.home.winner == true) FontWeight.Bold else FontWeight.Normal,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (status == "S") {
                    Text(
                        text = formatToLocalDateTime(match.matchData.date),
                        fontWeight = FontWeight.Bold
                    )
                } else if (status == "L") {
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
                            .padding(8.dp),
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
                } else if (status == "F") {
                    Text(text = "F")
                } else {
                    Text(
                        text = match.matchData.status.short,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
                Text(
                    text = "${match.goals.home ?: 0} - ${match.goals.away ?: 0}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = if (status == "L") Color.Red else Color.Black
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
            ) {
                AsyncImage(
                    model = match.teams.away.logo,
                    contentDescription = "${match.teams.away.name} icon",
                    modifier = Modifier
                        .size(64.dp)
                        .clickable {
                            navController.navigate(Routes.teamDetail(match.teams.away.id.toString()))
                        },
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = match.teams.away.name,
                    fontWeight = if (match.teams.away.winner == true) FontWeight.Bold else FontWeight.Normal,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Match events", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp, bottom = 4.dp))
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFFE0E0E0),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            match.events.forEach { event ->
                val arrangement = when {
                    event.team.name == match.teams.home.name -> Arrangement.Start
                    else -> Arrangement.End
                }
                val isYellow = when {
                    event.detail == "Yellow Card" -> true
                    else -> false
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = arrangement
                ) {
                    val isHomeTeam = event.team.name == match.teams.home.name
                    val elapsedTime = "${event.time.elapsed}'"
                    val playerName = event.player.name
                    val assistName = event.assist?.name.orEmpty()

                    when (event.type) {
                        "Goal" -> {
                            if (isHomeTeam) {
                                Text(text = "\u26BD", modifier = Modifier.padding(end = 4.dp))
                                Text(text = "$elapsedTime $playerName")
                            } else {
                                Text(text = "$playerName $elapsedTime")
                                Text(text = "\u26BD", modifier = Modifier.padding(start = 4.dp))
                            }
                        }

                        "subst" -> {
                            if (isHomeTeam) {
                                Text(text = "\uD83D\uDD04 $elapsedTime", modifier = Modifier.padding(end = 4.dp))
                                Text(text = playerName, fontWeight = FontWeight.Bold)
                                Text(text = " $assistName")
                            } else {
                                Text(text = "$assistName ")
                                Text(text = playerName, fontWeight = FontWeight.Bold)
                                Text(text = "$elapsedTime \uD83D\uDD04", modifier = Modifier.padding(start = 4.dp))
                            }
                        }

                        "Card" -> {
                            val cardEmoji = if (isYellow) "\uD83D\uDFE8" else "\uD83D\uDFE5"
                            if (isHomeTeam) {
                                Text(text = cardEmoji, modifier = Modifier.padding(end = 4.dp))
                                Text(text = "$elapsedTime $playerName")
                            } else {
                                Text(text = "$playerName $elapsedTime")
                                Text(text = cardEmoji, modifier = Modifier.padding(start = 4.dp))
                            }
                        }
                    }
                }
                HorizontalDivider(
                    color = Color.Gray.copy(alpha = 0.2f),
                    thickness = 2.dp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }

    }
}

