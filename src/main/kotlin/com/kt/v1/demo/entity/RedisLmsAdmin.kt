package com.kt.v1.demo.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@RedisHash(value = "LmsAdmin", timeToLive = 60)
data class RedisLmsAdmin(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid") var uid: Long = 0L,
    @Indexed @Column(name = "user_id", nullable = false) var userId: String = "",
    @Column(name = "user_pass", nullable = false) var userPass: String = "",
    @Column(name = "name", nullable = false) var name: String = "",
    @Column(name = "tel", nullable = false) var tel: String = "",
    @Column(name = "mobile", nullable = false) var mobile: String = "",
    @Column(name = "email", nullable = false) var email: String = "",
    @Column(name = "user_level", nullable = false) var userLevel: UserLevel = UserLevel.ADMIN,
    @Column(name = "del_yn", nullable = false) var delYn: isDeleted = isDeleted.No,
    @CreatedDate @Column(name = "regdate", nullable = false) var regdate: LocalDateTime = LocalDateTime.now(),
)
