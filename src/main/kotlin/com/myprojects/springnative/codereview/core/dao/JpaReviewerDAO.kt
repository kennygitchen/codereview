package com.myprojects.springnative.codereview.core.dao

import com.myprojects.springnative.codereview.core.domain.Reviewer
import org.springframework.data.jpa.repository.JpaRepository

interface JpaReviewerDAO : JpaRepository<Reviewer, Long> {
    fun findAllByNameContainsIgnoreCaseOrderByName(searchText:String): List<Reviewer>
}