package com.myprojects.springnative.codereview.core.service

import com.myprojects.springnative.codereview.DataBuilder
import com.myprojects.springnative.codereview.TestUtil
import com.myprojects.springnative.codereview.core.dao.JpaReviewDAO
import com.myprojects.springnative.codereview.core.exception.EntityNotFoundException
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.BuildersKt
import spock.lang.Specification

import static com.myprojects.springnative.codereview.TestUtil.testRunBlocking

class ReviewServiceTest extends Specification {

    private ReviewService reviewService
    private JpaReviewDAO jpaReviewDAO
    private ApplicantService applicantService
    private ReviewerService reviewerService
    private Continuation continuation

    def setup() {
        jpaReviewDAO = Mock()
        applicantService = Mock()
        reviewerService = Mock()
        reviewService = new ReviewService(jpaReviewDAO, applicantService, reviewerService)
        continuation = Mock(Continuation) {
            getContext() >> Mock(CoroutineContext)
        }
    }

    def "get() should return a review"() {
        given:
        def reviewer = DataBuilder.reviewer(1L)
        def applicant = DataBuilder.applicant(1L)
        def expected = DataBuilder.review(1L, applicant, reviewer)

        when:
        def review = reviewService.get(expected.id, continuation)

        then:
        review == expected

        1 * jpaReviewDAO.fetchById(expected.id) >> Optional.of(expected)
    }

    def "get() should throw Exception when reviewer does not exist"() {
        given:
        def id = 1000000000L

        when:
        reviewService.get(id, continuation)

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
        def saved = testRunBlocking {
            scope, continuation -> reviewService.create(applicant.id, reviewer.id, newReview, continuation)
        }


        //def saved = reviewService.create(applicant.id, reviewer.id, newReview, continuation)

        then:
        saved.id == id
        saved == expected

        1 * applicantService.get(applicant.id, _) >> applicant
        1 * reviewerService.get(reviewer.id, _) >> reviewer
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
        testRunBlocking {
            scope, continuation -> reviewService.create(applicant.id, reviewer.id, newReview, continuation)
        }

        then:
        thrown(IllegalStateException)

        0 * applicantService.get(_, _)
        0 * reviewerService.get(_, _)
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
        BuildersKt.runBlocking(EmptyCoroutineContext.INSTANCE) {
            scope, continuation ->
        }
        def saved = testRunBlocking {
            scope, continuation -> reviewService.update(applicant.id, reviewer.id, review, continuation)
        }
        then:
        saved.id == id
        saved == expected

        1 * jpaReviewDAO.getById(id) >> expected
        1 * applicantService.get(applicant.id, _) >> applicant
        1 * reviewerService.get(reviewer.id, _) >> reviewer
        1 * jpaReviewDAO.saveAndFlush(expected) >> expected
    }

    def "update() should error"() {
        given:
        def id = 5L
        def applicant = DataBuilder.applicant(1L)
        def reviewer = DataBuilder.reviewer(1L)
        def review = DataBuilder.review(id, null, null)

        when:
        reviewService.update(applicant.id, reviewer.id, review, continuation)

        then:
        thrown(EntityNotFoundException)

        1 * jpaReviewDAO.getById(id) >> { throw new javax.persistence.EntityNotFoundException() }
        0 * applicantService.get(_, _)
        0 * reviewerService.get(_, _)
        0 * jpaReviewDAO.saveAndFlush(_)
    }

    def 'findByReviewer() should return reviews for a reviewer'() {
        given:
        def id = 1L
        def applicant = DataBuilder.applicant(1L)
        def reviewer = DataBuilder.reviewer(1L)
        def expected = DataBuilder.review(id, applicant, reviewer)

        when:
        def reviews = reviewService.findByReviewer(reviewer.id, continuation)

        then:
        reviews == [expected]

        1 * reviewerService.get(reviewer.id, _) >> reviewer
        1 * jpaReviewDAO.findAllByAssessedByOrderByDateSubmitted(reviewer) >> [expected]
    }
}
