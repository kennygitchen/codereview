package com.myprojects.springnative.codereview.api

import com.myprojects.springnative.codereview.api.request.ReviewerRequest
import com.myprojects.springnative.codereview.api.response.ReviewerResponse
import com.myprojects.springnative.codereview.core.domain.Position
import com.myprojects.springnative.codereview.core.domain.Reviewer
import com.myprojects.springnative.codereview.core.service.ReviewerService
import spock.lang.Specification

class ReviewerApiTest extends Specification {

    private ReviewerService reviewerService
    private ReviewerApi reviewerApi

    def setup() {
        reviewerService = Mock()
        reviewerApi = new ReviewerApi(reviewerService)
    }

    def 'get() should return reviewer by id'() {
        given:
        def id = 1L
        def expected = new Reviewer(id, 'Test', new Position('Senior'))

        when:
        def reviewer = reviewerApi.get(id)

        then:
        reviewer == ReviewerResponse.@Companion.from(expected)

        1 * reviewerService.get(id) >> expected
    }

    def 'findBy(null) should return all reviewers'() {
        given:
        def reviewer1 = new Reviewer(1L, 'Test', new Position('Senior'))
        def reviewer2 = new Reviewer(2L, 'Test', new Position('Senior'))
        def expected = [reviewer1, reviewer2]

        when:
        def reviewers = reviewerApi.findBy(null)

        then:
        reviewers == ReviewerResponse.@Companion.from(expected)

        1 * reviewerService.list() >> expected
    }

    def 'findBy("test") should return matched reviewers'() {
        given:
        def toFind = "test"
        def reviewer1 = new Reviewer(1L, 'Test', new Position('Senior'))
        def reviewer2 = new Reviewer(2L, 'Test', new Position('Senior'))
        def expected = [reviewer1, reviewer2]

        when:
        def reviewers = reviewerApi.findBy(toFind)

        then:
        reviewers == ReviewerResponse.@Companion.from(expected)

        1 * reviewerService.findByNameContains(toFind) >> expected
    }

    def 'create() should create a new reviewer'() {
        given:
        def request = new ReviewerRequest('Test', 'Senior')

        when:
        reviewerApi.create(request)

        then:
        1 * reviewerService.createOrUpdate(request.toReviewer())
    }

    def 'update() should update details for reviewer by id'() {
        given:
        def id = 1L
        def request = new ReviewerRequest('Test', 'Senior')
        def expected = request.toReviewer()
        expected.id = id

        when:
        reviewerApi.update(id, request)

        then:
        1 * reviewerService.createOrUpdate(expected)
    }
}
