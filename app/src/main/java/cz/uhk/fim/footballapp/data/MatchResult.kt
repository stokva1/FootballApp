package cz.uhk.fim.footballapp.data

import com.google.gson.annotations.SerializedName

data class MatchResult(
    @SerializedName("home") val home: Int,
    @SerializedName("away") val away: Int
)