package com.myprojects.springnative.codereview.core.domain

import java.io.Serializable
import javax.persistence.*

@Embeddable
data class ScoreId(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    var review: ReviewId,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criteria_id")
    var criteria: CriteriaId,
) : Serializable {

    @Entity
    @Table(name = "review")
    data class ReviewId(@Id var id: Long?)

    @Entity
    @Table(name = "criteria")
    data class CriteriaId(
        @Id
        var id: Long,
        @Column(updatable = false, insertable = false)
        var name: String? = null,
        @Column(updatable = false, insertable = false)
        var description: String? = null
    ) {
        companion object {
            fun from(criteria: Criteria): CriteriaId =
                CriteriaId(
                    id = criteria.id,
                    name = criteria.name,
                    description = criteria.description
                )
        }
    }
}

