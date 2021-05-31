package com.myprojects.springnative.codereview.core.service

import com.myprojects.springnative.codereview.core.dao.JpaApplicantDAO
import com.myprojects.springnative.codereview.core.domain.Applicant
import com.myprojects.springnative.codereview.core.domain.Position
import com.myprojects.springnative.codereview.core.exception.EntityNotFoundException
import spock.lang.Specification

class ApplicantServiceTest extends Specification {

    private ApplicantService applicantService
    private JpaApplicantDAO jpaApplicantDAO

    def setup() {
        jpaApplicantDAO = Mock()
        applicantService = new ApplicantService(jpaApplicantDAO)
    }

    def "get() should return a applicant"() {
        given:
        def id = 1L
        def expected = new Applicant(id, "applicant", "applicant@gmail.com", "0433211211", new Position("Senior"), [])

        when:
        def reviewer = applicantService.get(id)

        then:
        reviewer == expected

        1 * jpaApplicantDAO.fetchById(id) >> Optional.of(expected)
    }

    def "get() should throw Exception when applicant does not exist"() {
        given:
        def id = 1000000000L

        when:
        applicantService.get(id)

        then:
        def error = thrown(EntityNotFoundException)
        error.message == """Applicant does not exist, id=$id"""

        1 * jpaApplicantDAO.fetchById(id) >> Optional.empty()
    }

    def "createOrUpdate() should save a new applicant"() {
        given:
        def id = 1L
        def reviewer = new Applicant(null, "applicant", "applicant@gmail.com", "0433211211", new Position("Senior"), [])
        def expected = new Applicant(id, "applicant", "applicant@gmail.com", "0433211211", new Position("Senior"), [])

        when:
        def saved = applicantService.createOrUpdate(reviewer)

        then:
        saved.id == id
        saved == expected

        1 * jpaApplicantDAO.saveAndFlush(reviewer) >> expected
    }

    def "list() should return a list of all applicants"() {
        given:
        def applicant1 = new Applicant(1L, "applicant1", "applicant1@gmail.com", "0433211211", new Position("Senior"), [])
        def applicant2 = new Applicant(2L, "applicant2", "applicant2@gmail.com", "0433211212", new Position("Graduate"), [])

        when:
        def result = applicantService.list()

        then:
        result == [applicant1, applicant2]

        1 * jpaApplicantDAO.findAll() >> [applicant1, applicant2]

    }

    def "findByNameContains() should return top 10 partial name matched applicants"() {
        given:
        def searchText = 'eng'
        def applicant1 = new Applicant(1L, "applicant1", "applicant1@gmail.com", "0433211211", new Position("Senior"), [])
        def applicant2 = new Applicant(2L, "applicant2", "applicant2@gmail.com", "0433211212", new Position("Graduate"), [])

        when:
        def result = applicantService.findByNameContains(searchText)

        then:
        result == [applicant1, applicant2]

        1 * jpaApplicantDAO.findAllByNameContainsIgnoreCaseOrderByName(searchText) >> [applicant1, applicant2]
    }
}
