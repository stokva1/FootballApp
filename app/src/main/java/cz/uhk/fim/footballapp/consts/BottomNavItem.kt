package cz.uhk.fim.footballapp.consts

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val title: String, val icon: ImageVector, val screenRoute: String) {
    object MatchList : BottomNavItem("Matches", Icons.Filled.Home, Routes.MatchList)
    object FavouriteTeams : BottomNavItem("Favorite", Icons.Filled.FavoriteBorder, Routes.FavoriteTeams)
}