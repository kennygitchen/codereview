package com.myprojects.springnative.codereview.core.domain

import java.sql.Timestamp
import javax.persistence.*

@Entity
data class Review(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id")
    var applicant: Applicant? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessed_by")
    var assessedBy: Reviewer? = null,
    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = "review_id", referencedColumnName = "id")
    var scores: MutableList<Score> = mutableListOf(),
    var feedback: String,
    var comments: String,
    @Column(name = "date_submitted")
    var dateSubmitted: Timestamp
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Review

        if (id != other.id) return false
        if (applicant != other.applicant) return false
        if (assessedBy != other.assessedBy) return false
        if (!(scores.toTypedArray() contentEquals other.scores.toTypedArray())) return false
        if (feedback != other.feedback) return false
        if (comments != other.comments) return false
        if (dateSubmitted != other.dateSubmitted) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + applicant.hashCode()
        result = 31 * result + assessedBy.hashCode()
        result = 31 * result + scores.hashCode()
        result = 31 * result + feedback.hashCode()
        result = 31 * result + comments.hashCode()
        result = 31 * result + dateSubmitted.hashCode()
        return result
    }
}
