package com.myprojects.springnative.codereview.core.dao

import com.myprojects.springnative.codereview.core.domain.Criteria
import org.springframework.data.jpa.repository.JpaRepository

interface JpaCriteriaDAO : JpaRepository<Criteria, Long>