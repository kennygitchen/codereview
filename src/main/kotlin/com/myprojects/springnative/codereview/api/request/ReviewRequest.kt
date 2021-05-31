package com.myprojects.springnative.codereview.api.request

import com.myprojects.springnative.codereview.core.domain.Review
import com.myprojects.springnative.codereview.core.domain.Score
import com.myprojects.springnative.codereview.core.domain.ScoreId
import java.sql.Timestamp
import java.time.LocalDateTime

data class ReviewRequest(
    var applicantId: Long,
    var reviewerId: Long,
    var scores: List<ScoreRequest>,
    var feedback: String,
    var comments: String,
    var dateSubmitted: LocalDateTime? = null
) {
    data class ScoreRequest(
        val criteriaId: Long,
        var comments: String,
        var score: Double
    ) {
        fun toScore(reviewId: Long?) =
            Score(
                id = ScoreId(ScoreId.ReviewId(reviewId), ScoreId.CriteriaId(criteriaId)),
                comments = comments,
                score = score
            )
    }

    fun toReview(id: Long? = null): Review {
        return Review(
            id = id,
            scores = scores.map { it.toScore(id) }.toMutableList(),
            feedback = feedback,
            comments = comments,
            dateSubmitted = dateSubmitted?.let { Timestamp.valueOf(it) } ?: Timestamp.valueOf(LocalDateTime.now())
        )
    }
}
