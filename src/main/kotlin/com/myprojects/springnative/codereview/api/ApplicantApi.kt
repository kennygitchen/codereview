package com.myprojects.springnative.codereview.api

import com.myprojects.springnative.codereview.api.request.ApplicantRequest
import com.myprojects.springnative.codereview.api.response.ApplicantResponse
import com.myprojects.springnative.codereview.core.service.ApplicantService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("applicants")
@Tag(name = "Applicant", description = "The Applicant API")
class ApplicantApi @Autowired constructor(
    private val applicantService: ApplicantService
) {
    @Operation(summary = "Get details of a applicant by Id")
    @GetMapping(path = ["/{id}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun get(@PathVariable id: Long): ApplicantResponse {
        LOGGER.info("Find Request received for Applicant, Id=$id")
        return ApplicantResponse.from(applicantService.get(id), true)
    }

    @Operation(summary = "Add a new applicant")
    @PostMapping(path = [])
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody applicantRequest: ApplicantRequest) {
        LOGGER.info("Create Request received for Applicant")
        applicantService.createOrUpdate(applicantRequest.toApplicant())
    }

    @Operation(summary = "Update details of a applicant by Id")
    @PutMapping(path = ["/{id}"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(@PathVariable id: Long, @RequestBody applicantRequest: ApplicantRequest) {
        LOGGER.info("Update Request received for Applicant, Id=$id")
        applicantService.createOrUpdate(applicantRequest.toApplicant(id))
    }

    @Operation(summary = "Get a list of applicants")
    @GetMapping(path = [])
    fun findBy(@RequestParam name: String?): List<ApplicantResponse> {
        LOGGER.info("FindBy Request received for Applicants")
        return when {
            name != null && name.isNotEmpty() -> ApplicantResponse.from(applicantService.findByNameContains(name))
            else -> ApplicantResponse.from(applicantService.list())
        }
    }

    companion object {
        val LOGGER: Logger = LogManager.getLogger()
    }
}