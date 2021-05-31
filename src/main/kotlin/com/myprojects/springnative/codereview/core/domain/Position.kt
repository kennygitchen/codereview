package com.myprojects.springnative.codereview.core.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Position(
    @Id
    var name: String
)