package com.myprojects.springnative.codereview.core.domain

import javax.persistence.*

@Entity
data class Applicant(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var name: String,
    var email: String,
    var mobile: String,
    @ManyToOne
    @JoinColumn(name = "position_apply")
    var positionApply: Position,
    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], mappedBy = "applicant")
    var reviews: List<Review> = listOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Applicant

        if (id != other.id) return false
        if (name != other.name) return false
        if (email != other.email) return false
        if (mobile != other.mobile) return false
        if (positionApply != other.positionApply) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + name.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + mobile.hashCode()
        result = 31 * result + positionApply.hashCode()
        return result
    }

    override fun toString(): String {
        return "Applicant(id=$id, name='$name', email='$email', mobile='$mobile', positionApply=$positionApply)"
    }


}