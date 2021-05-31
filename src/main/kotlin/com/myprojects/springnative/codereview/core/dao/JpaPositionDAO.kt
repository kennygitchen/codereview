package com.myprojects.springnative.codereview.core.dao

import com.myprojects.springnative.codereview.core.domain.Position
import org.springframework.data.jpa.repository.JpaRepository

interface JpaPositionDAO : JpaRepository<Position, String>