package com.myprojects.springnative.codereview.api.response

import com.myprojects.springnative.codereview.core.domain.Reviewer

data class ReviewerResponse(
    var id: Long,
    var name: String,
    var position: String
) {
    companion object {
        fun from(reviewer: Reviewer) =
            ReviewerResponse(
                id = reviewer.id!!,
                name = reviewer.name,
                position = reviewer.position.name
            )

        fun from(reviewers: Collection<Reviewer>) = reviewers.map { from(it) }
    }
}