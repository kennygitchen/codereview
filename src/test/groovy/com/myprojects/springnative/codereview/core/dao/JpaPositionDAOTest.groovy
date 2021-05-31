package com.myprojects.springnative.codereview.core.dao


import com.myprojects.springnative.codereview.core.domain.Position
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class JpaPositionDAOTest extends BaseJpaDAOTest {
    @Autowired
    private JpaPositionDAO jpaPositionDAO

    def 'findById() should return an entity form database'() {
        given:
        String id = "Graduate"
        Position expected = new Position(id)

        when:
        Position position = jpaPositionDAO.findById(id).get()

        then:
        position == expected
    }
}