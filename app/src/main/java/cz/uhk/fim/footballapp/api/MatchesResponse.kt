package cz.uhk.fim.footballapp.api

import com.google.gson.annotations.SerializedName

data class FootballResponse<T>(
    @SerializedName("response") val data: T
)