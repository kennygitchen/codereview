package com.myprojects.springnative.codereview.core.service


import com.myprojects.springnative.codereview.core.dao.JpaReviewerDAO
import com.myprojects.springnative.codereview.core.domain.Position
import com.myprojects.springnative.codereview.core.domain.Reviewer
import com.myprojects.springnative.codereview.core.exception.EntityNotFoundException
import spock.lang.Specification

class ReviewerServiceTest extends Specification {

    private ReviewerService reviewerService
    private JpaReviewerDAO jpaReviewerDAO

    def setup() {
        jpaReviewerDAO = Mock()
        reviewerService = new ReviewerService(jpaReviewerDAO)
    }

    def "get() should return a reviewer"() {
        given:
        def id = 1L
        def expected = new Reviewer(id, "Engineer", new Position("Senior"))

        when:
        def reviewer = reviewerService.get(id)

        then:
        reviewer == expected

        1 * jpaReviewerDAO.findById(id) >> Optional.of(expected)
    }

    def "get() should throw Exception when reviewer does not exist"() {
        given:
        def id = 1000000000L

        when:
        reviewerService.get(id)

        then:
        def error = thrown(EntityNotFoundException)
        error.message == """Reviewer does not exist, id=$id"""

        1 * jpaReviewerDAO.findById(id) >> Optional.empty()
    }

    def "createOrUpdate() should save a new reviewer"() {
        given:
        def id = 1L
        def reviewer = new Reviewer(null, "Engineer", new Position("Senior"))
        def expected = new Reviewer(id, "Engineer", new Position("Senior"))

        when:
        def saved = reviewerService.createOrUpdate(reviewer)

        then:
        saved.id == id
        saved == expected

        1 * jpaReviewerDAO.saveAndFlush(reviewer) >> expected
    }

    def "list() should return a list of all reviewers"() {
        given:
        def reviewer1 = new Reviewer(1L, "Engineer1", new Position("Senior"))
        def reviewer2 = new Reviewer(2L, "Engineer2", new Position("Tech Lead"))

        when:
        def result = reviewerService.list()

        then:
        result == [reviewer1, reviewer2]

        1 * jpaReviewerDAO.findAll() >> [reviewer1, reviewer2]

    }

    def "findByNameContains() should return top 10 partial name matched reviewers"() {
        given:
        def toFind = 'eng'
        def reviewer1 = new Reviewer(1L, "Engineer1", new Position("Senior"))
        def reviewer2 = new Reviewer(2L, "Engineer2", new Position("Tech Lead"))

        when:
        def result = reviewerService.findByNameContains(toFind)

        then:
        result == [reviewer1, reviewer2]

        1 * jpaReviewerDAO.findAllByNameContainsIgnoreCaseOrderByName(toFind) >> [reviewer1, reviewer2]

    }
}
