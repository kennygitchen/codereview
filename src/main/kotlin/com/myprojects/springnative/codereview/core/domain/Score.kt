package com.myprojects.springnative.codereview.core.domain

import javax.persistence.EmbeddedId
import javax.persistence.Entity

@Entity
data class Score(
    @EmbeddedId
    var id: ScoreId,
    var comments: String,
    var score: Double
)
