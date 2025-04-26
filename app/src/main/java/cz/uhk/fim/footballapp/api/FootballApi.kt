package cz.uhk.fim.footballapp.api

import cz.uhk.fim.footballapp.data.Match
import cz.uhk.fim.footballapp.data.MatchEvents
import cz.uhk.fim.footballapp.data.Team
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Date

interface FootballApi {

    @GET("/fixtures")
    suspend fun getMatches(@Query("date") date: String): Response<FootballResponse<List<Match>>>

    @GET("/fixtures")
    suspend fun getMatchDetail(
        @Query("id") id: Int
    ): Response<FootballResponse<List<Match>>>

    @GET("/fixtures/events")
    suspend fun getMatchEvents(
        @Query("fixture") fixtureId: Int
    ): Response<FootballResponse<List<MatchEvents>>>

    @GET("/teams")
    suspend fun getTeamDetails(
        @Query("id") teamId: Int
    ): Response<FootballResponse<List<Team>>>

    @GET("/teams")
    suspend fun getTeamsByName(
        @Query("name") name: String
    ) : Response<FootballResponse<List<Team>>>
}