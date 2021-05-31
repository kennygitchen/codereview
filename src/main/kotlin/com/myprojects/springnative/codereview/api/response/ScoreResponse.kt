package com.myprojects.springnative.codereview.api.response

import com.myprojects.springnative.codereview.core.domain.Score

data class ScoreResponse(
    var criteria: CriteriaResponse,
    var comments: String,
    var score: Double
) {
    companion object {
        fun from(score: Score) =
            ScoreResponse(
                criteria = CriteriaResponse.from(score.id.criteria),
                comments = score.comments,
                score = score.score
            )

        fun from(scores: List<Score>) = scores.map { from(it) }
    }
}