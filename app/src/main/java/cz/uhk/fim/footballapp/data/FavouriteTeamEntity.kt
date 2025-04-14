package cz.uhk.fim.footballapp.data

import io.objectbox.annotation.Id
import io.objectbox.annotation.Entity

@Entity
data class FavouriteTeamEntity (
    @Id var id: Long = 0,
    var teamId: String,
    var name: String,
    var crest: String,
)