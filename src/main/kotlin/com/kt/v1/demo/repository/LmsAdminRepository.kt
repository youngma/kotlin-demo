package com.kt.v1.demo.repository

import com.kt.v1.demo.entity.LmsAdmin
import org.springframework.data.repository.CrudRepository

interface LmsAdminRepository : CrudRepository<LmsAdmin, Long> {
    fun findAllByUserId(userId: String): LmsAdmin?
}