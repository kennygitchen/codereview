package com.myprojects.springnative.codereview.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.myprojects.springnative.codereview.api.request.ReviewRequest
import com.myprojects.springnative.codereview.api.request.ReviewSearchRequest
import com.myprojects.springnative.codereview.api.response.ReviewResponse
import com.myprojects.springnative.codereview.core.service.ReviewService
import com.myprojects.springnative.codereview.util.api.ReviewSearchRequestEditor
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("reviews")
@Tag(name = "Review", description = "The Review API")
class ReviewApi @Autowired constructor(
    private val objectMapper: ObjectMapper,
    private val reviewService: ReviewService
) {
    @InitBinder
    fun initBinder(binder: WebDataBinder): Unit =
        binder.registerCustomEditor(ReviewSearchRequest::class.java, ReviewSearchRequestEditor(objectMapper))

    @Operation(summary = "Get details of a review by Id")
    @GetMapping(path = ["/{id}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun get(@PathVariable id: Long): ReviewResponse {
        LOGGER.info("Find Request received for Review, Id=$id")
        return ReviewResponse.from(reviewService.get(id))
    }

    @Operation(summary = "Add a new review")
    @PostMapping(path = [""])
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody reviewRequest: ReviewRequest) {
        LOGGER.info("Create Request received for Review")
        reviewService.create(reviewRequest.applicantId, reviewRequest.reviewerId, reviewRequest.toReview())
    }

    @Operation(summary = "Update details of a review for applicant by reviewer")
    @PutMapping(path = ["{id}"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(
        @PathVariable id: Long,
        @RequestBody reviewRequest: ReviewRequest
    ) {
        LOGGER.info("Update Request received for Review, Id=$id")
        reviewService.update(reviewRequest.applicantId, reviewRequest.reviewerId, reviewRequest.toReview(id))
    }

    @Operation(summary = "Search for reviews")
    @GetMapping(path = [""])
    fun search(
        @RequestParam
        @Parameter(
            content = [
                Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = ReviewSearchRequest::class)
                )
            ]
        )
        searchRequest: ReviewSearchRequest
    ): List<ReviewResponse> {
        LOGGER.info("Search Request received for Reviews")
        return ReviewResponse.from(reviewService.findByReviewer(searchRequest.reviewerId!!))
    }

    companion object {
        val LOGGER: Logger = LogManager.getLogger()
    }
}