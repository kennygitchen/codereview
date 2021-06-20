package com.myprojects.springnative.codereview.core.service

import com.myprojects.springnative.codereview.core.dao.JpaReviewDAO
import com.myprojects.springnative.codereview.core.domain.Review
import com.myprojects.springnative.codereview.core.exception.EntityNotFoundException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.List.copyOf
import javax.transaction.Transactional
import javax.persistence.EntityNotFoundException as JpaEntityNotFoundException

@Service
class ReviewService @Autowired constructor(
        private val jpaReviewDAO: JpaReviewDAO,
        private val applicantService: ApplicantService,
        private val reviewerService: ReviewerService
) {
    suspend fun get(id: Long): Review =
            jpaReviewDAO.fetchById(id).orElseThrow { throw EntityNotFoundException("Review does not exist, id=$id") }

    @Transactional
    suspend fun create(applicantId: Long, reviewerId: Long, review: Review): Review = coroutineScope {

        if (review.id != null) throw IllegalStateException("Review already exist")

        val applicantAsync = async { applicantService.get(applicantId) }
        val reviewerAsync = async { reviewerService.get(reviewerId) }

        val scores = copyOf(review.scores)
        review.scores.clear()
        review.applicant = applicantAsync.await()
        review.assessedBy = reviewerAsync.await()

        // save the review
        val saved = jpaReviewDAO.save(review)

        // update scores
        scores.forEach { it.id.review.id = saved.id }
        saved.scores.addAll(scores)

        // save the scores
        jpaReviewDAO.saveAndFlush(saved)
    }

    suspend fun update(applicantId: Long, reviewerId: Long, review: Review): Review = coroutineScope {
        try {
            val reviewRef = jpaReviewDAO.getById(review.id!!)
            val applicantAsync = async { applicantService.get(applicantId) }
            val reviewerAsync = async { reviewerService.get(reviewerId) }

            reviewRef.applicant = applicantAsync.await()
            reviewRef.assessedBy = reviewerAsync.await()
            reviewRef.scores.clear()
            reviewRef.scores.addAll(review.scores)
            reviewRef.comments = review.comments
            reviewRef.feedback = review.feedback

            jpaReviewDAO.saveAndFlush(reviewRef)
        } catch (error: JpaEntityNotFoundException) {
            throw EntityNotFoundException("Review not found for update", error)
        }
    }


    suspend fun findByReviewer(reviewerId: Long): List<Review> =
            jpaReviewDAO.findAllByAssessedByOrderByDateSubmitted(reviewerService.get(reviewerId))
}