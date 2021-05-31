package com.myprojects.springnative.codereview.core.dao

import com.myprojects.springnative.codereview.core.domain.Criteria
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class JpaCriteriaDAOTest extends BaseJpaDAOTest {
    @Autowired
    private JpaCriteriaDAO jpaCriteriaDAO

    def 'findById() should return an entity from database'() {

        given:
        Long id = 1L
        Criteria expected = new Criteria(id, "Requirements", "Are all requirements been met?")

        when:
        Criteria criteria = jpaCriteriaDAO.findById(id).get()

        then:
        criteria == expected
    }
}