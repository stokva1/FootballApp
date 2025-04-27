package cz.uhk.fim.footballapp

import MatchDetailScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cz.uhk.fim.footballapp.consts.Routes
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import android.widget.Toast
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import cz.uhk.fim.footballapp.consts.BottomNavItem
import cz.uhk.fim.footballapp.helpers.NotificationSchedulerHelper
import cz.uhk.fim.footballapp.helpers.PermissionHelper
import cz.uhk.fim.footballapp.screens.FavouriteMatchScreen
import cz.uhk.fim.footballapp.screens.MatchListScreen
import cz.uhk.fim.footballapp.screens.SearchScreen
import cz.uhk.fim.footballapp.screens.TeamDetailScreen
import cz.uhk.fim.footballapp.ui.theme.FootballAppTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin {
            androidContext(this@MainActivity)
            modules(
                repositoryModule,
                viewModelModule,
                networkModule,
                objectBoxModule,
                imageModule,
                helperModule
            )
        }
        setContent {
            FootballAppTheme {
                val navController = rememberNavController()
                MainScreen(navController)
            }
        }

        val permissionHelper = PermissionHelper(this)
        permissionHelper.requestNotificationPermission()

        val notificationSchedulerHelper: NotificationSchedulerHelper by inject()
        notificationSchedulerHelper.scheduleNotificationWorker()
    }


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    var selectedItem by remember { mutableStateOf(0) }

    val items = listOf(
        BottomNavItem.MatchList,
        BottomNavItem.FavouriteTeams
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Futbolive", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    if (currentRoute == Routes.MatchDetail || currentRoute == Routes.TeamDetail || currentRoute == Routes.Search) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                tint = Color.White,
                                contentDescription = "Go Back"
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(Routes.Search) }) {
                        Icon(
                            Icons.Default.Search,
                            tint = Color.White,
                            contentDescription = "Search"
                        )
                    }
                }
            )
        },

        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)) 
            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                item.icon,
                                contentDescription = item.title,
                                modifier = Modifier.size(28.dp)
                            )
                        },
                        label = {
                            Text(
                                item.title,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (currentRoute == item.screenRoute) Color.White else Color.LightGray
                            )
                        },
                        selected = currentRoute == item.screenRoute,
                        onClick = {
                            if (currentRoute != item.screenRoute) {
                                navController.navigate(item.screenRoute) {
                                    if (item.screenRoute == Routes.MatchList) {
                                        popUpTo(Routes.MatchList) {
                                            inclusive = true
                                        }
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            unselectedIconColor = Color.DarkGray,
                            selectedTextColor = Color.White,
                            unselectedTextColor = Color.LightGray,
                            indicatorColor = MaterialTheme.colorScheme.secondary
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Navigation(navController = navController, innerPadding = innerPadding)
    }
}

@Composable
fun Navigation(navController: NavHostController, innerPadding: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = Routes.MatchList,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(Routes.MatchList) { MatchListScreen(navController) }
        composable(Routes.MatchDetail) { navBackStackEntry ->
            val matchId = navBackStackEntry.arguments?.getString("matchId")
            if (matchId != null) {
                println(matchId)
                MatchDetailScreen(matchId.toInt(), navController = navController)
            }
        }
        composable(Routes.TeamDetail) { navBackStackEntry ->
            val teamId = navBackStackEntry.arguments?.getString("teamId")
            if (teamId != null) {
                println(teamId)
                TeamDetailScreen(teamId.toInt())
            }
        }
        composable(Routes.FavoriteTeams) { FavouriteMatchScreen() }
        composable(Routes.Search) { SearchScreen(navController) }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    FootballAppTheme {
        MainScreen(rememberNavController())
    }
}
