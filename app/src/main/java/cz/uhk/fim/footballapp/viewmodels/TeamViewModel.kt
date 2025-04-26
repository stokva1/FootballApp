package cz.uhk.fim.footballapp.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.uhk.fim.footballapp.api.ApiResult
import cz.uhk.fim.footballapp.api.FootballApi
import cz.uhk.fim.footballapp.data.Match
import cz.uhk.fim.footballapp.data.Team
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TeamViewModel(private val footballApi: FootballApi): ViewModel() {
    private val _teamList = MutableStateFlow<ApiResult<List<Team>>>(ApiResult.Success(emptyList()))
    val teamList: StateFlow<ApiResult<List<Team>>> = _teamList.asStateFlow()

    private val _teamDetail = MutableStateFlow<ApiResult< List<Team>>>(ApiResult.Loading)
    val teamDetail: StateFlow<ApiResult<List<Team>>> = _teamDetail.asStateFlow()

    fun getTeamDetail(id: Int){
        viewModelScope.launch {
            _teamDetail.value = ApiResult.Loading
            try {
                val response = footballApi.getTeamDetails(id)
                if (response.isSuccessful) {
                    _teamDetail.value = ApiResult.Success(response.body()!!.data)
                } else {
                    _teamDetail.value = ApiResult.Error("Error fetching match detail: ${response.message()}")
                    Log.e("MatchViewModel", "Error fetching match detail: ${response.message()}")
                }
            } catch (e: Exception) {
                _teamDetail.value = ApiResult.Error("Exception fetching match detail: ${e.message}")
                Log.e("MatchViewModel", "Exception fetching match detail: ${e.message}")
            }
        }
    }

    fun getTeamListByName(name: String){
        viewModelScope.launch {
            _teamList.value = ApiResult.Loading
            try {
                val response = footballApi.getTeamsByName(name)
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    if(data != null){
                        _teamList.value = ApiResult.Success(data)
                        println(response.body())
                    }else{
                        _teamList.value = ApiResult.Error("Data is null")
                        Log.e("MatchViewModel", "Data is null")
                    }
                } else {
                    _teamList.value = ApiResult.Error("Error fetching match list: ${response.message()}")
                    Log.e("MatchViewModel", "Error fetching match list: ${response.message()}")
                }
            } catch (e: Exception) {
                _teamList.value = ApiResult.Error("Exception fetching match list: ${e.message}")
                Log.e("MatchViewModel", "Exception fetching match list: ${e.message}")
            }
        }
    }

}