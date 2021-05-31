package com.myprojects.springnative.codereview.core.service

import com.myprojects.springnative.codereview.DataBuilder
import com.myprojects.springnative.codereview.core.dao.JpaReviewDAO
import com.myprojects.springnative.codereview.core.exception.EntityNotFoundException
import spock.lang.Specification

class ReviewServiceTest extends Specification {

    private ReviewService reviewService
    private JpaReviewDAO jpaReviewDAO
    private ApplicantService applicantService
    private ReviewerService reviewerService

    def setup() {
        jpaReviewDAO = Mock()
        applicantService = Mock()
        reviewerService = Mock()
        reviewService = new ReviewService(jpaReviewDAO, applicantService, reviewerService)
    }

    def "get() should return a review"() {
        given:
        def reviewer = DataBuilder.reviewer(1L)
        def applicant = DataBuilder.applicant(1L)
        def expected = DataBuilder.review(1L, applicant, reviewer)

        when:
        def review = reviewService.get(expected.id)

        then:
        review == expected

        1 * jpaReviewDAO.fetchById(expected.id) >> Optional.of(expected)
    }

    def "get() should throw Exception when reviewer does not exist"() {
        given:
        def id = 1000000000L

        when:
        reviewService.get(id)

        then:
        def error = thrown(EntityNotFoundException)
        error.message == """Review does not exist, id=$id"""

        1 * jpaReviewDAO.fetchById(id) >> Optional.empty()
    }

    def "create() should save a new review"() {
        given:
        def id = 1L
        def applicant = DataBuilder.applicant(1L)
        def reviewer = DataBuilder.reviewer(1L)
        def newReview = DataBuilder.review(null, null, null)
        def expected = DataBuilder.review(id, applicant, reviewer)

        when:
        def saved = reviewService.create(applicant.id, reviewer.id, newReview)

        then:
        saved.id == id
        saved == expected

        1 * applicantService.get(applicant.id) >> applicant
        1 * reviewerService.get(reviewer.id) >> reviewer
        1 * jpaReviewDAO.save(_) >> {
            assert arguments[0].id == null
            assert arguments[0].applicant == applicant
            assert arguments[0].assessedBy == reviewer
            return expected
        }
        1 * jpaReviewDAO.saveAndFlush(_) >> {
            assert arguments[0].id == id
            assert arguments[0].applicant == applicant
            assert arguments[0].assessedBy == reviewer
            return expected
        }
    }

    def "create() should error"() {
        given:
        def id = 1L
        def applicant = DataBuilder.applicant(1L)
        def reviewer = DataBuilder.reviewer(1L)
        def newReview = DataBuilder.review(id, null, null)

        when:
        reviewService.create(applicant.id, reviewer.id, newReview)

        then:
        thrown(IllegalStateException)

        0 * applicantService.get(_)
        0 * reviewerService.get(_)
        0 * jpaReviewDAO.save(_)
        0 * jpaReviewDAO.saveAndFlush(_)
    }

    def "update() should save a new review"() {
        given:
        def id = 1L
        def applicant = DataBuilder.applicant(1L)
        def reviewer = DataBuilder.reviewer(1L)
        def review = DataBuilder.review(id, null, null)
        def expected = DataBuilder.review(id, applicant, reviewer)

        when:
        def saved = reviewService.update(applicant.id, reviewer.id, review)

        then:
        saved.id == id
        saved == expected

        1 * jpaReviewDAO.getById(id) >> expected
        1 * applicantService.get(applicant.id) >> applicant
        1 * reviewerService.get(reviewer.id) >> reviewer
        1 * jpaReviewDAO.saveAndFlush(expected) >> expected
    }

    def "update() should error"() {
        given:
        def id = 5L
        def applicant = DataBuilder.applicant(1L)
        def reviewer = DataBuilder.reviewer(1L)
        def review = DataBuilder.review(id, null, null)

        when:
        reviewService.update(applicant.id, reviewer.id, review)

        then:
        thrown(EntityNotFoundException)

        1 * jpaReviewDAO.getById(id) >> { throw new javax.persistence.EntityNotFoundException() }
        0 * applicantService.get(_)
        0 * reviewerService.get(_)
        0 * jpaReviewDAO.saveAndFlush(_)
    }

    def 'findByReviewer() should return reviews for a reviewer'() {
        given:
        def id = 1L
        def applicant = DataBuilder.applicant(1L)
        def reviewer = DataBuilder.reviewer(1L)
        def expected = DataBuilder.review(id, applicant, reviewer)

        when:
        def reviews = reviewService.findByReviewer(reviewer.id)

        then:
        reviews == [expected]

        1 * reviewerService.get(reviewer.id) >> reviewer
        1 * jpaReviewDAO.findAllByAssessedByOrderByDateSubmitted(reviewer) >> [expected]
    }
}
