package cz.uhk.fim.footballapp.data

import com.google.gson.annotations.SerializedName

data class Team(
    @SerializedName("team") val team: TeamDetail,
    @SerializedName("venue") val venue: Venue
)

data class TeamDetail(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("code") val code: String?,
    @SerializedName("country") val country: String,
    @SerializedName("founded") val founded: Int,
    @SerializedName("national") val national: Boolean,
    @SerializedName("logo") val logo: String
)

data class Venue(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("address") val address: String?,
    @SerializedName("city") val city: String,
    @SerializedName("capacity") val capacity: Int,
    @SerializedName("surface") val surface: String,
    @SerializedName("image") val image: String
)

