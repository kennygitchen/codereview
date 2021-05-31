package com.myprojects.springnative.codereview.core.dao

import org.springframework.test.context.jdbc.Sql
import spock.lang.Specification

@Sql("classpath:data.sql")
abstract class BaseJpaDAOTest extends Specification{
}
