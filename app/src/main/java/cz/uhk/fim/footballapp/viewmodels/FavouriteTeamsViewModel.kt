package cz.uhk.fim.footballapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.uhk.fim.footballapp.api.ApiResult
import cz.uhk.fim.footballapp.data.FavouriteTeamEntity
import cz.uhk.fim.footballapp.data.Team
import cz.uhk.fim.footballapp.repository.FavouriteTeamsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class FavouriteTeamsViewModel(
    private val favouriteTeamRepository: FavouriteTeamsRepository,
) : ViewModel() {

    private val _favouriteTeams = MutableStateFlow<ApiResult<List<FavouriteTeamEntity>>>(ApiResult.Loading)
    val favouriteTeams: StateFlow<ApiResult<List<FavouriteTeamEntity>>> = _favouriteTeams.asStateFlow()

    init {
        loadFavouriteTeams()
    }

    fun addFavoriteTeam(team: Team) {
        viewModelScope.launch {
            favouriteTeamRepository.addFavouriteTeam(team)
            loadFavouriteTeams()
        }
    }

    fun removeFavoriteTeam(teamId: String) {
        viewModelScope.launch {
            favouriteTeamRepository.removeFavouriteTeam(teamId)
            loadFavouriteTeams()
        }
    }

    fun loadFavouriteTeams() {
        viewModelScope.launch {
            try {
                val favs = favouriteTeamRepository.getAllFavouriteTeams()
                _favouriteTeams.value = ApiResult.Success(favs)
            } catch (e: Exception) {
                _favouriteTeams.value = ApiResult.Error("Failed to load favourite teams: ${e.message}")
            }
        }
    }
}

