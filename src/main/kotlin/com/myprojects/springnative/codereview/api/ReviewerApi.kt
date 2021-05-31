package com.myprojects.springnative.codereview.api

import com.myprojects.springnative.codereview.api.request.ReviewerRequest
import com.myprojects.springnative.codereview.api.response.ReviewerResponse
import com.myprojects.springnative.codereview.core.service.ReviewerService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("reviewers")
@Tag(name = "Reviewer", description = "The Reviewer API")
class ReviewerApi @Autowired constructor(
    private val reviewerService: ReviewerService
) {
    @Operation(summary = "Get details of a reviewer by Id")
    @GetMapping(path = ["/{id}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun get(@PathVariable id: Long): ReviewerResponse {
        LOGGER.info("Find Request received for Reviewer, Id=$id")
        return ReviewerResponse.from(reviewerService.get(id))
    }

    @Operation(summary = "Add a new reviewer")
    @PostMapping(path = [])
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody reviewerRequest: ReviewerRequest) {
        LOGGER.info("Create Request received for Reviewer")
        reviewerService.createOrUpdate(reviewerRequest.toReviewer())
    }

    @Operation(summary = "Update details of a reviewer by Id")
    @PutMapping(path = ["/{id}"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(@PathVariable id: Long, @RequestBody reviewerRequest: ReviewerRequest) {
        LOGGER.info("Update Request received for Reviewer, Id=$id")
        reviewerService.createOrUpdate(reviewerRequest.toReviewer(id))
    }

    @Operation(summary = "Get a list of reviewers")
    @GetMapping(path = [])
    fun findBy(@RequestParam name: String?): List<ReviewerResponse> {
        LOGGER.info("FindBy Request received for Reviewers")
        return when {
            name != null && name.isNotEmpty() -> ReviewerResponse.from(reviewerService.findByNameContains(name))
            else -> ReviewerResponse.from(reviewerService.list())
        }
    }

    companion object {
        val LOGGER: Logger = LogManager.getLogger()
    }
}