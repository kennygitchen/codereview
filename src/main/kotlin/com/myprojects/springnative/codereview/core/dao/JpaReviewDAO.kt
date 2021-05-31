package com.myprojects.springnative.codereview.core.dao

import com.myprojects.springnative.codereview.core.domain.Review
import com.myprojects.springnative.codereview.core.domain.Reviewer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface JpaReviewDAO : JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r JOIN FETCH r.scores s WHERE r.id = :id")
    fun fetchById(id: Long): Optional<Review>
    fun findAllByAssessedByOrderByDateSubmitted(assessedBy: Reviewer): List<Review>
}