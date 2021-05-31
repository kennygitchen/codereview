package com.myprojects.springnative.codereview.api

import com.myprojects.springnative.codereview.api.request.ApplicantRequest
import com.myprojects.springnative.codereview.api.response.ApplicantResponse
import com.myprojects.springnative.codereview.core.domain.Applicant
import com.myprojects.springnative.codereview.core.domain.Position
import com.myprojects.springnative.codereview.core.service.ApplicantService
import spock.lang.Specification

class ApplicantApiTest extends Specification {

    private ApplicantService applicantService
    private ApplicantApi applicantApi

    def setup() {
        applicantService = Mock()
        applicantApi = new ApplicantApi(applicantService)
    }

    def 'get() should return applicant by id'() {
        given:
        def id = 1L
        def expected = new Applicant(id, 'Test', 'test@gmail.com', '0433123123', new Position('Senior'), [])

        when:
        def applicant = applicantApi.get(id)

        then:
        applicant == ApplicantResponse.@Companion.from(expected, true)

        1 * applicantService.get(id) >> expected
    }

    def 'findBy(null) should return all applicants'() {
        given:
        def applicant1 = new Applicant(1L, 'Test1', 'test1@gmail.com', '0433123123', new Position('Senior'), [])
        def applicant2 = new Applicant(2L, 'Test2', 'test2@gmail.com', '0433123124', new Position('Tech Lead'), [])
        def expected = [applicant1, applicant2]

        when:
        def applicants = applicantApi.findBy(null)

        then:
        applicants == ApplicantResponse.@Companion.from(expected, false)

        1 * applicantService.list() >> expected
    }

    def 'findBy("test") should return matched applicants'() {
        given:
        def toFind = "test"
        def applicant1 = new Applicant(1L, 'Test1', 'test1@gmail.com', '0433123123', new Position('Senior'), [])
        def applicant2 = new Applicant(2L, 'Test2', 'test2@gmail.com', '0433123124', new Position('Tech Lead'), [])
        def expected = [applicant1, applicant2]

        when:
        def applicants = applicantApi.findBy(toFind)

        then:
        applicants == ApplicantResponse.@Companion.from(expected, false)

        1 * applicantService.findByNameContains(toFind) >> expected
    }

    def 'create() should create a new applicant'() {
        given:
        def request = new ApplicantRequest('Test', 'test@gmail.com', '0433123123', 'Senior')

        when:
        applicantApi.create(request)

        then:
        1 * applicantService.createOrUpdate(request.toApplicant())
    }

    def 'update() should update details for applicant by id'() {
        given:
        def id = 1L
        def request = new ApplicantRequest('Test', 'test@gmail.com', '0433123123', 'Senior')
        def expected = request.toApplicant()
        expected.id = id

        when:
        applicantApi.update(id, request)

        then:
        1 * applicantService.createOrUpdate(expected)
    }
}
