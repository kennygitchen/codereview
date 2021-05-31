package com.myprojects.springnative.codereview.core.service

import com.myprojects.springnative.codereview.core.dao.JpaApplicantDAO
import com.myprojects.springnative.codereview.core.domain.Applicant
import com.myprojects.springnative.codereview.core.exception.EntityNotFoundException
import com.myprojects.springnative.codereview.core.validation.EmailValidator
import com.myprojects.springnative.codereview.core.validation.MobileValidator
import com.myprojects.springnative.codereview.core.validation.NameValidator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ApplicantService @Autowired constructor(
    private val jpaApplicantDAO: JpaApplicantDAO
) {
    fun get(id: Long): Applicant =
        jpaApplicantDAO.fetchById(id).orElseThrow { throw EntityNotFoundException("Applicant does not exist, id=$id") }

    fun createOrUpdate(applicant: Applicant): Applicant {
        NameValidator.check(applicant.name)
        MobileValidator.check(applicant.mobile)
        EmailValidator.check(applicant.email)

        return jpaApplicantDAO.saveAndFlush(applicant)
    }

    fun list(): List<Applicant> = jpaApplicantDAO.findAll()

    fun findByNameContains(searchText: String): List<Applicant> =
        jpaApplicantDAO.findAllByNameContainsIgnoreCaseOrderByName(searchText)
}