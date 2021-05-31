package com.myprojects.springnative.codereview.core.service

import com.myprojects.springnative.codereview.core.dao.JpaReviewDAO
import com.myprojects.springnative.codereview.core.domain.Review
import com.myprojects.springnative.codereview.core.exception.EntityNotFoundException
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
    fun get(id: Long): Review =
        jpaReviewDAO.fetchById(id).orElseThrow { throw EntityNotFoundException("Review does not exist, id=$id") }

    @Transactional
    fun create(applicantId: Long, reviewerId: Long, review: Review): Review {

        if (review.id != null) throw IllegalStateException("Review already exist")

        val scores = copyOf(review.scores)
        review.scores.clear()
        review.applicant = applicantService.get(applicantId)
        review.assessedBy = reviewerService.get(reviewerId)

        // save the review
        val saved = jpaReviewDAO.save(review)

        // update scores
        scores.forEach { it.id.review.id = saved.id }
        saved.scores.addAll(scores)

        // save the scores
        return jpaReviewDAO.saveAndFlush(saved)
    }

    fun update(applicantId: Long, reviewerId: Long, review: Review): Review {
        try {
            val reviewRef = jpaReviewDAO.getById(review.id!!)

            reviewRef.applicant = applicantService.get(applicantId)
            reviewRef.assessedBy = reviewerService.get(reviewerId)
            reviewRef.scores.clear()
            reviewRef.scores.addAll(review.scores)
            reviewRef.comments = review.comments
            reviewRef.feedback = review.feedback

            return jpaReviewDAO.saveAndFlush(reviewRef)
        } catch (error: JpaEntityNotFoundException) {
            throw EntityNotFoundException("Review not found for update", error)
        }
    }


    fun findByReviewer(reviewerId: Long): List<Review> =
        jpaReviewDAO.findAllByAssessedByOrderByDateSubmitted(reviewerService.get(reviewerId))
}