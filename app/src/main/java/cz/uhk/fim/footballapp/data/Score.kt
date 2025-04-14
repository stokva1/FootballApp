package cz.uhk.fim.footballapp.data

import com.google.gson.annotations.SerializedName

data class Score(
    @SerializedName("winner") val winner: String?,
    @SerializedName("duration") val duration: String,
    @SerializedName("fullTime") val fullTime: MatchResult
) {
    val homeGoals: Int
        get() = fullTime.home

    val awayGoals: Int
        get() = fullTime.away
}