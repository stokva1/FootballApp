package cz.uhk.fim.footballapp.data

import com.google.gson.annotations.SerializedName

data class MatchEvents(
    @SerializedName("time") val time: GoalTime,
    @SerializedName("team") val team: GoalTeam,
    @SerializedName("player") val player: Player,
    @SerializedName("assist") val assist: Assist?,
    @SerializedName("type") val type: String,
    @SerializedName("detail") val detail: String,
    @SerializedName("comments") val comments: String?
)

data class GoalTime(
    @SerializedName("elapsed") val elapsed: Int,
    @SerializedName("extra") val extra: Int?
)

data class GoalTeam(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("logo") val logo: String
)

data class Player(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)

data class Assist(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)
