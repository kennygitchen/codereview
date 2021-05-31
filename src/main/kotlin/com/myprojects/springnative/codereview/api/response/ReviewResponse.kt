package com.myprojects.springnative.codereview.api.response

import com.myprojects.springnative.codereview.core.domain.Review
import java.time.LocalDateTime

data class ReviewResponse(
    val id: Long,
    var applicant: ApplicantResponse,
    var assessedBy: ReviewerResponse,
    var scores: List<ScoreResponse>,
    var feedback: String,
    var comments: String,
    var dateSubmitted: LocalDateTime
) {
    companion object {
        fun from(review: Review, forApplicant: Boolean = false): ReviewResponse =
            ReviewResponse(
                id = review.id!!,
                applicant = ApplicantResponse.from(review.applicant!!),
                assessedBy = ReviewerResponse.from(review.assessedBy!!),
                scores = if (forApplicant) listOf() else ScoreResponse.from(review.scores),
                feedback = if (forApplicant) "" else review.feedback,
                comments = review.comments,
                dateSubmitted = review.dateSubmitted.toLocalDateTime()
            )

        fun from(reviews: List<Review>, forApplicant: Boolean = false): List<ReviewResponse> =
            reviews.map { from(it, forApplicant) }
    }
}
