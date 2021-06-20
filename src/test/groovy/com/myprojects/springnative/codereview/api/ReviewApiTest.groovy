package com.myprojects.springnative.codereview.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.myprojects.springnative.codereview.DataBuilder
import com.myprojects.springnative.codereview.api.request.ReviewSearchRequest
import com.myprojects.springnative.codereview.api.response.ReviewResponse
import com.myprojects.springnative.codereview.core.service.ReviewService
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import spock.lang.Specification

class ReviewApiTest extends Specification {

    private ReviewService reviewService
    private ObjectMapper objectMapper
    private ReviewApi reviewApi
    private Continuation continuation

    def setup() {
        reviewService = Mock()
        objectMapper = Mock()
        reviewApi = new ReviewApi(objectMapper, reviewService)
        continuation = Mock(Continuation) {
            getContext() >> Mock(CoroutineContext)
        }
    }

    def 'get() should return review by id'() {
        given:
        def id = 1L
        def expected = DataBuilder.review(id, DataBuilder.applicant(1L), DataBuilder.reviewer(1L))

        when:
        def review = reviewApi.get(id, continuation)

        then:
        review == ReviewResponse.@Companion.from(expected, false)

        1 * reviewService.get(id, _) >> expected
    }

    def 'create() should create a new review'() {
        given:
        def applicantId = 1L
        def reviewerId = 1L
        def request = DataBuilder.reviewRequest(applicantId, reviewerId, [DataBuilder.scoreRequest(1L), DataBuilder.scoreRequest(2L)])

        when:
        reviewApi.create(request, continuation)

        then:
        1 * reviewService.create(applicantId, reviewerId, request.toReview(null), _)
    }

    def 'update() should update details for review by id'() {
        given:
        def id = 1L
        def applicantId = 1L
        def reviewerId = 1L
        def request = DataBuilder.reviewRequest(applicantId, reviewerId, [DataBuilder.scoreRequest(1L), DataBuilder.scoreRequest(2L)])

        when:
        reviewApi.update(id, request, continuation)

        then:
        1 * reviewService.update(applicantId, reviewerId, request.toReview(id), _)
    }

    def 'search() should return reviews by searchRequest'() {
        given:
        def request = new ReviewSearchRequest(1L)
        def expected = [DataBuilder.review(1L, DataBuilder.applicant(1L), DataBuilder.reviewer(1L))]

        when:
        def result = reviewApi.search(request, continuation)

        then:
        result == ReviewResponse.@Companion.from(expected, false)
        1 * reviewService.findByReviewer(request.reviewerId, _) >> expected
    }
}
