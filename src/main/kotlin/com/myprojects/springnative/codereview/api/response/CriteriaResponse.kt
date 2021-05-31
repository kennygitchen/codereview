package com.myprojects.springnative.codereview.api.response

import com.myprojects.springnative.codereview.core.domain.ScoreId

data class CriteriaResponse(
    val id: Long,
    val name: String,
    val description: String
) {
    companion object {
        fun from(criteria: ScoreId.CriteriaId) =
            CriteriaResponse(
                id = criteria.id,
                name = criteria.name!!,
                description = criteria.description!!
            )
    }
}
