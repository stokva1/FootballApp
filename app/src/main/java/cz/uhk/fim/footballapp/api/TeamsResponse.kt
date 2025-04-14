package cz.uhk.fim.footballapp.api

import com.google.gson.annotations.SerializedName

data class TeamsResponse<T>(
    @SerializedName("teams") val data: T

)
