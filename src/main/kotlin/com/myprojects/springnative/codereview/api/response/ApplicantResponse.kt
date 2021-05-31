package com.myprojects.springnative.codereview.api.response

import com.myprojects.springnative.codereview.core.domain.Applicant

data class ApplicantResponse(
    var id: Long,
    var name: String,
    var email: String,
    var mobile: String,
    var positionApply: String,
    var reviews: List<ReviewResponse> = listOf()
) {
    companion object {
        fun from(applicant: Applicant, includeReviews: Boolean = false): ApplicantResponse =
            ApplicantResponse(
                id = applicant.id!!,
                name = applicant.name,
                email = applicant.email,
                mobile = applicant.mobile,
                positionApply = applicant.positionApply.name,
                reviews = if (includeReviews) ReviewResponse.from(applicant.reviews, true) else listOf()
            )

        fun from(applicants: Collection<Applicant>, includeReviews: Boolean = false): List<ApplicantResponse> =
            applicants.map { from(it, includeReviews) }
    }
}