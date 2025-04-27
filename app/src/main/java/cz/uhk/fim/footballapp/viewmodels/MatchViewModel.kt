package cz.uhk.fim.footballapp.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.uhk.fim.footballapp.api.ApiResult
import cz.uhk.fim.footballapp.api.FootballApi
import cz.uhk.fim.footballapp.data.Match
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MatchViewModel(private val footballApi: FootballApi):ViewModel() {
    private val _matchList = MutableStateFlow<ApiResult<List<Match>>>(ApiResult.Loading)
    val matchList: StateFlow<ApiResult<List<Match>>> = _matchList.asStateFlow()

    private val _matchDetail = MutableStateFlow<ApiResult< List<Match>>>(ApiResult.Loading)
    val matchDetail: StateFlow<ApiResult<List<Match>>> = _matchDetail.asStateFlow()

    init {
//        startAutoRefresh()
    }

//    private fun startAutoRefresh() {
//        viewModelScope.launch {
//            while (true) {
//                getMatchList()
//                kotlinx.coroutines.delay(60_000L)
//            }
//        }
//    }

    fun getMatchList(date: String) {
        viewModelScope.launch {
            _matchList.value = ApiResult.Loading
            try {
                val response = footballApi.getMatches(date)
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    if(data != null){
                        val filteredData = data.filter { it.league.id in listOf(169, 1, 2, 3, 39, 45, 61, 65, 78, 135, 140, 143, 528) }

                        _matchList.value = ApiResult.Success(filteredData)
//                        _matchList.value = ApiResult.Success(data)
                        println(response.body())
                        Log.d("MatchViewModel", "getMatchList: ${response.body()?.data}")
                    }else{
                        _matchList.value = ApiResult.Error("Data is null")
                        Log.e("MatchViewModel", "Data is null")
                    }
                } else {
                    _matchList.value = ApiResult.Error("Error fetching match list: ${response.message()}")
                    Log.e("MatchViewModel", "Error fetching match list: ${response.message()}")
                }
            } catch (e: Exception) {
                _matchList.value = ApiResult.Error("Exception fetching match list: ${e.message}")
                Log.e("MatchViewModel", "Exception fetching match list: ${e.message}")
            }
        }
    }

    fun getMatchDetail(id: Int) {
        viewModelScope.launch {
            _matchDetail.value = ApiResult.Loading
            try {
                val response = footballApi.getMatchDetail(id)
                if (response.isSuccessful) {
                    _matchDetail.value = ApiResult.Success(response.body()!!.data)
                } else {
                    _matchDetail.value = ApiResult.Error("Error fetching match detail: ${response.message()}")
                    Log.e("MatchViewModel", "Error fetching match detail: ${response.message()}")
                }
            } catch (e: Exception) {
                _matchDetail.value = ApiResult.Error("Exception fetching match detail: ${e.message}")
                Log.e("MatchViewModel", "Exception fetching match detail: ${e.message}")
            }
        }
    }
}