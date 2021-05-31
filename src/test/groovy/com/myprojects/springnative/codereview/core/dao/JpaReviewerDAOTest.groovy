package com.myprojects.springnative.codereview.core.dao

import com.myprojects.springnative.codereview.TestContextConfiguration
import com.myprojects.springnative.codereview.core.domain.Position
import com.myprojects.springnative.codereview.core.domain.Reviewer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import

import java.util.stream.Collectors

@DataJpaTest
@Import(TestContextConfiguration)
class JpaReviewerDAOTest extends BaseJpaDAOTest {

    @Autowired
    private JpaHelper jpaHelper

    @Autowired
    private JpaPositionDAO jpaPositionDAO

    @Autowired
    private JpaReviewerDAO jpaReviewerDAO

    def 'save() should add a new reviewer'() {
        given:
        Long expected = jpaReviewerDAO.count() + 1
        def reviewer = new Reviewer(null, "Tester", jpaPositionDAO.getById("Graduate"))


        when:
        Reviewer saved = jpaReviewerDAO.saveAndFlush(reviewer)
        reviewer.id = saved.id

        then:
        // ensure the Context is clean
        jpaHelper.clearContext()

        saved.id != null
        reviewer == jpaReviewerDAO.findById(saved.id).get()
        expected == jpaReviewerDAO.count()
    }

    def 'getById() should return a reviewer'() {
        given:
        Long id = 1L
        Reviewer expected = new Reviewer(id, "reviewer 1", new Position("Graduate"))

        when:
        // fetch and detach the entity from persistence context
        Reviewer reviewer = jpaHelper.detach(jpaReviewerDAO.getById(id))

        then:
        reviewer == expected
    }

    def 'findAllByNameContaining() should return reviewers with name contains the search text'() {
        given:
        def searchText = 'WER'

        when:
        def result = jpaReviewerDAO.findAllByNameContainsIgnoreCaseOrderByName(searchText)

        then:
        result.size() != 0
        result.stream().filter {
            it.name.containsIgnoreCase(searchText)
        }.collect(Collectors.toList()) == result
    }
}


