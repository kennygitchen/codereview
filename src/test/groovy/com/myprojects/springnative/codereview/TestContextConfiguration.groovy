package com.myprojects.springnative.codereview

import com.myprojects.springnative.codereview.core.dao.JpaHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

import javax.persistence.EntityManager

@TestConfiguration
class TestContextConfiguration {

    @Autowired
    private EntityManager entityManager

    @Bean
    JpaHelper jpaHelper() {
        return new JpaHelper(entityManager)
    }
}