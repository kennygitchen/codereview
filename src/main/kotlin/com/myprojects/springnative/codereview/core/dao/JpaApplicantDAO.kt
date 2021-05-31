package com.myprojects.springnative.codereview.core.dao

import com.myprojects.springnative.codereview.core.domain.Applicant
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface JpaApplicantDAO : JpaRepository<Applicant, Long> {
    @Query("SELECT a FROM Applicant a LEFT JOIN FETCH a.reviews r WHERE a.id = :id")
    fun fetchById(id: Long): Optional<Applicant>
    fun findAllByNameContainsIgnoreCaseOrderByName(searchText: String): List<Applicant>
}