package com.myprojects.springnative.codereview.api.request

import com.myprojects.springnative.codereview.core.domain.Applicant
import com.myprojects.springnative.codereview.core.domain.Position

data class ApplicantRequest(
    var name: String,
    var email: String,
    var mobile: String,
    var positionApply: String
) {
    fun toApplicant(id: Long? = null): Applicant =
        Applicant(
            id = id,
            name = name,
            email = email,
            mobile = mobile,
            positionApply = Position(positionApply)
        )
}