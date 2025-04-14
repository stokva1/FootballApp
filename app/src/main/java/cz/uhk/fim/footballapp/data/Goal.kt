package cz.uhk.fim.footballapp.data

import com.google.gson.annotations.SerializedName

data class Goal (
    @SerializedName("minute") val minute: Int,
    @SerializedName("scorer") val scorer: Scorer,
    @SerializedName("team") val team: GoalTeam,
)

data class Scorer(
    @SerializedName("name") val name: String
)

data class GoalTeam(
    @SerializedName("name") val name: String
)