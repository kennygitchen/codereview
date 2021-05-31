package com.myprojects.springnative.codereview.core.domain

import javax.persistence.*

@Entity
data class Reviewer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var name: String,
    @ManyToOne
    @JoinColumn(name = "position")
    var position: Position
)
