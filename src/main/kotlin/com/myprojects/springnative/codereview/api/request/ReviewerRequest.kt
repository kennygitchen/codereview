package com.myprojects.springnative.codereview.api.request

import com.myprojects.springnative.codereview.core.domain.Position
import com.myprojects.springnative.codereview.core.domain.Reviewer

data class ReviewerRequest(
    var name: String,
    var position: String
) {
    fun toReviewer(id: Long? = null): Reviewer =
        Reviewer(
            id = id,
            name = name,
            position = Position(position)
        )
}