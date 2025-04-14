package cz.uhk.fim.footballapp.consts

object Routes {
    const val MatchList = "matchList"
    const val MatchDetail = "matchDetail/{matchId}"
    const val FavoriteTeams = "favoriteTeams"
    const val TeamDetail = "teamDetail/{teamId}"
    const val Settings = "settings"

    fun matchDetail(matchId: String): String {
        return "matchDetail/$matchId"
    }

    fun teamDetail(teamId: String): String {
        return "teamDetail/teamId"
    }
}
