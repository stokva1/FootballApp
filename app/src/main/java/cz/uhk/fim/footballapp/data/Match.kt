package cz.uhk.fim.footballapp.data

import com.google.gson.annotations.SerializedName

data class Match(
    @SerializedName("fixture") val matchData: MatchData,
    @SerializedName("league") val league: League,
    @SerializedName("teams") val teams: Teams,
    @SerializedName("goals") val goals: Goals,
    @SerializedName("events") val events: List<MatchEvents>,
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

data class Status(
    @SerializedName("elapsed") val elapsed: Int?,
    @SerializedName("short") val short: String,
    @SerializedName("extra") val extra: String?
)

data class Teams(
    @SerializedName("home") val home: TeamsDetail,
    @SerializedName("away") val away: TeamsDetail
)

data class TeamsDetail(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("logo") val logo: String,
    @SerializedName("winner") val winner: Boolean?
)

data class Goals(
    @SerializedName("home") val home: Int?,
    @SerializedName("away") val away: Int?
)

