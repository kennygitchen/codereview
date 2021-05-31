package com.myprojects.springnative.codereview.core.service

import com.myprojects.springnative.codereview.core.dao.JpaReviewerDAO
import com.myprojects.springnative.codereview.core.domain.Reviewer
import com.myprojects.springnative.codereview.core.exception.EntityNotFoundException
import com.myprojects.springnative.codereview.core.validation.NameValidator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ReviewerService @Autowired constructor(
    private val jpaReviewerDAO: JpaReviewerDAO
) {
    fun get(id: Long): Reviewer =
        jpaReviewerDAO.findById(id).orElseThrow { throw EntityNotFoundException("Reviewer does not exist, id=$id") }

    fun createOrUpdate(reviewer: Reviewer): Reviewer {
        NameValidator.check(reviewer.name)
        return jpaReviewerDAO.saveAndFlush(reviewer)
    }

    fun list(): List<Reviewer> = jpaReviewerDAO.findAll()

    fun findByNameContains(searchText: String): List<Reviewer> =
        jpaReviewerDAO.findAllByNameContainsIgnoreCaseOrderByName(searchText)
}