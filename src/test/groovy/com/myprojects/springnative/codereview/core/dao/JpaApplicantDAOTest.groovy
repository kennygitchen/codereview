package com.myprojects.springnative.codereview.core.dao

import com.myprojects.springnative.codereview.TestContextConfiguration
import com.myprojects.springnative.codereview.core.domain.Applicant
import com.myprojects.springnative.codereview.core.domain.Position
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import

import java.util.stream.Collectors

@DataJpaTest
@Import(TestContextConfiguration)
class JpaApplicantDAOTest extends BaseJpaDAOTest {

    @Autowired
    private JpaHelper jpaHelper

    @Autowired
    private JpaPositionDAO jpaPositionDAO

    @Autowired
    private JpaApplicantDAO jpaApplicantDAO

    def 'save() should add a new applicant'() {
        given:
        Long expected = jpaApplicantDAO.count() + 1
        Applicant applicant = new Applicant(
                null,
                "test_applicant",
                "test_applicant@gmail.com",
                "0433123123",
                jpaPositionDAO.getById("Graduate"),
                []
        )

        when:
        Applicant saved = jpaApplicantDAO.saveAndFlush(applicant)
        applicant.id = saved.id

        then:
        jpaHelper.clearContext()

        saved.id != null
        applicant == jpaApplicantDAO.getById(saved.id)
        expected == jpaApplicantDAO.count()
    }

    def 'fetchById() should return a applicant'() {
        given:
        Long id = 1L
        Applicant expected = new Applicant(
                id,
                "Applicant 1",
                "applicant1@gmail.com",
                "0433333333",
                new Position("Senior"),
                []
        )

        when:
        // fetch and detach the entity from persistence context
        Applicant applicant = jpaHelper.detach(jpaApplicantDAO.fetchById(id).get())

        then:
        applicant == expected
        applicant.reviews.size() == 1
    }

    def 'findAllByNameContaining() should return applicants with name contains the search text'() {
        given:
        def searchText = 'APP'

        when:
        def result = jpaApplicantDAO.findAllByNameContainsIgnoreCaseOrderByName(searchText)

        then:
        result.size() != 0
        result.stream().filter {
            it.name.containsIgnoreCase(searchText)
        }.collect(Collectors.toList()) == result
    }
}


