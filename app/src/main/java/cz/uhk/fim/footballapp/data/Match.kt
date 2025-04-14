package cz.uhk.fim.footballapp.data

import com.google.gson.annotations.SerializedName

data class Match(
    @SerializedName("fixture") val matchData: MatchData,
    @SerializedName("league") val league: League,
    @SerializedName("teams") val teams: Teams,
    @SerializedName("goals") val goals: Goals

//    @SerializedName("id") val id: String,
//    @SerializedName("utcDate") val utcDate: String,
//    @SerializedName("status") val status: String,
//    @SerializedName("minute") val minute: String?,
//    @SerializedName("injuryTime") val injuryTime: Int?,
//    @SerializedName("venue") val venue: String?,
//    @SerializedName("stage") val stage: String,
//    @SerializedName("homeTeam") val homeTeam: Team,
//    @SerializedName("awayTeam") val awayTeam: Team,
//    @SerializedName("score") val score: Score,
//    @SerializedName("goals") val goals: List<Goal>?,
)

data class MatchData(
    @SerializedName("id") val id: Int,
    @SerializedName("referee") val referee: String?,
    @SerializedName("date") val date: String,
    @SerializedName("venue") val venue: Venue,
    @SerializedName("status") val status: Status
)

data class League(
    @SerializedName("id") val id: Int,
    )

data class Venue(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
)

data class Status(
    @SerializedName("elapsed") val elapsed: Int?,
    @SerializedName("short") val short: String,
    @SerializedName("extra") val extra: String?
)

data class Teams(
    @SerializedName("home") val home: Team,
    @SerializedName("away") val away: Team
)

data class Team(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("logo") val logo: String,
    @SerializedName("winner") val winner: Boolean?
)

data class Goals(
    @SerializedName("home") val home: Int?,
    @SerializedName("away") val away: Int?
)

