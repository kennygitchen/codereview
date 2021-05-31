package com.myprojects.springnative.codereview.core.dao

import com.myprojects.springnative.codereview.TestContextConfiguration
import com.myprojects.springnative.codereview.core.domain.Review
import com.myprojects.springnative.codereview.core.domain.Score
import com.myprojects.springnative.codereview.core.domain.ScoreId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import

import java.sql.Timestamp
import java.time.LocalDateTime

@DataJpaTest
@Import(TestContextConfiguration)
class JpaReviewDAOTest extends BaseJpaDAOTest {

    @Autowired
    private JpaHelper jpaHelper

    @Autowired
    private JpaApplicantDAO jpaApplicantDAO

    @Autowired
    private JpaReviewerDAO jpaReviewerDAO

    @Autowired
    private JpaReviewDAO jpaReviewDAO

    @Autowired
    private JpaCriteriaDAO jpaCriteriaDAO

    def 'save() should add a new review'() {
        given:
        Long expectedCount = jpaReviewDAO.count() + 1
        Review review = new Review(
                null,
                jpaApplicantDAO.findById(1L).get(),
                jpaReviewerDAO.findById(1L).get(),
                [],
                "new feedback",
                "new comments",
                Timestamp.valueOf(LocalDateTime.now())
        )

        when:
        // step 1, save the review
        Review saved = jpaReviewDAO.saveAndFlush(review)
        review.id = saved.id


        then:
        jpaHelper.clearContext()

        saved.id != null
        jpaReviewDAO.findById(saved.id).get() == review
        jpaReviewDAO.count() == expectedCount

        when:
        // step 2, save the scores
        List<Score> scores = [
                new Score(new ScoreId(new ScoreId.ReviewId(saved.id), ScoreId.CriteriaId.@Companion.from(jpaCriteriaDAO.getById(1L))), "n/a", 2.0),
                new Score(new ScoreId(new ScoreId.ReviewId(saved.id), ScoreId.CriteriaId.@Companion.from(jpaCriteriaDAO.getById(2L))), "n/a", 3.0),
                new Score(new ScoreId(new ScoreId.ReviewId(saved.id), ScoreId.CriteriaId.@Companion.from(jpaCriteriaDAO.getById(3L))), "n/a", 3.5),
                new Score(new ScoreId(new ScoreId.ReviewId(saved.id), ScoreId.CriteriaId.@Companion.from(jpaCriteriaDAO.getById(4L))), "no tests", 0.0),
                new Score(new ScoreId(new ScoreId.ReviewId(saved.id), ScoreId.CriteriaId.@Companion.from(jpaCriteriaDAO.getById(5L))), "no documentation", 0.0),
        ]
        review.scores.addAll(scores)
        jpaReviewDAO.saveAndFlush(review)

        then:
        jpaHelper.clearContext()

        jpaReviewDAO.findById(saved.id).get() == review
        jpaReviewDAO.count() == expectedCount
    }

    def 'fetchById() should return a review'() {
        given:
        Long reviewId = 1L
        List<Score> scores = [
                new Score(new ScoreId(new ScoreId.ReviewId(reviewId), ScoreId.CriteriaId.@Companion.from(jpaCriteriaDAO.getById(1L))), "Most fo the requirements are met", 4.0),
                new Score(new ScoreId(new ScoreId.ReviewId(reviewId), ScoreId.CriteriaId.@Companion.from(jpaCriteriaDAO.getById(2L))), "n/a", 3.0),
                new Score(new ScoreId(new ScoreId.ReviewId(reviewId), ScoreId.CriteriaId.@Companion.from(jpaCriteriaDAO.getById(3L))), "Simple and easy to read code", 3.5),
                new Score(new ScoreId(new ScoreId.ReviewId(reviewId), ScoreId.CriteriaId.@Companion.from(jpaCriteriaDAO.getById(4L))), "Need more test coverage", 3.0),
                new Score(new ScoreId(new ScoreId.ReviewId(reviewId), ScoreId.CriteriaId.@Companion.from(jpaCriteriaDAO.getById(5L))), "Has problem/solution statements, but no instruction to run", 3.0),
        ]
        Review expected = new Review(
                reviewId,
                jpaApplicantDAO.findById(1L).get(),
                jpaReviewerDAO.findById(1L).get(),
                scores,
                "test feedback",
                "test comments",
                Timestamp.valueOf("2020-01-01 10:10:10")
        )

        when:
        Review review = jpaHelper.detach(jpaReviewDAO.fetchById(reviewId).get())

        then:
        review == expected
        review.scores.size() == 5
    }

    def 'findAllByAssessedByOrderByDateSubmitted() should return all reviews from a reviewer'() {
        given:
        def reviewer = jpaReviewerDAO.findById(1L).get()

        when:
        def submitted = jpaReviewDAO.findAllByAssessedByOrderByDateSubmitted(reviewer)

        then:
        submitted.size() > 0
        submitted.forEach(review -> review.assessedBy == reviewer)
    }
}


