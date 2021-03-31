package com.kt.v1.demo.entity

import javax.persistence.*

@Entity(name = "lms_admin")
class LmsAdmin(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid") var uid: Long = 0L,
    @Column(name = "user_id", nullable = false) var userId: String = ""
) {

}
