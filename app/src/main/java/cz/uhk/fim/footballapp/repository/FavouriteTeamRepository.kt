package cz.uhk.fim.footballapp.repository

import cz.uhk.fim.footballapp.data.FavouriteTeamEntity
import cz.uhk.fim.footballapp.data.FavouriteTeamEntity_
import cz.uhk.fim.footballapp.data.Team
import io.objectbox.Box
import io.objectbox.query.QueryBuilder

class FavouriteTeamRepository(private val favouriteTeamBox: Box<FavouriteTeamEntity>) {
    fun addFavouriteTeam(team: Team) {
        val existingEntity = favouriteTeamBox.query()
            .equal(FavouriteTeamEntity_.teamId, team.team.id.toString(), QueryBuilder.StringOrder.CASE_INSENSITIVE)
            .build().findFirst()

        if (existingEntity != null) {
            // Entita již existuje, aktualizujeme ji
            existingEntity.name = team.team.name
            existingEntity.logo = team.team.logo
            favouriteTeamBox.put(existingEntity)
        } else {
            // Entita neexistuje, vytvoříme novou
            val favouriteTeamEntity = FavouriteTeamEntity(teamId = team.team.id.toString(), name = team.team.name, logo = team.team.logo)
            favouriteTeamBox.put(favouriteTeamEntity)
        }

    }

    fun removeFavouriteTeam(teamId: String) {
        val query = favouriteTeamBox.query()
            .equal(FavouriteTeamEntity_.teamId, teamId, QueryBuilder.StringOrder.CASE_INSENSITIVE)
            .build()
        val result = query.findFirst()
        if (result != null) {
            favouriteTeamBox.remove(result)
        }
        query.close()
    }

    fun getAllFavouriteTeams(): List<FavouriteTeamEntity> {
        return favouriteTeamBox.all
    }
}