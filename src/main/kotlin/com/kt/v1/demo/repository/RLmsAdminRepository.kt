package com.kt.v1.demo.repository

import com.kt.v1.demo.entity.RedisLmsAdmin

interface RLmsAdminRepository {
    fun save(user: RedisLmsAdmin)
    fun findAll(): Map<Long, RedisLmsAdmin?>?
    fun findById(id: Long): RedisLmsAdmin?
    fun update(user: RedisLmsAdmin)
    fun delete(id: String)
}
