package com.kt.v1.demo.repository

import com.kt.v1.demo.entity.RedisLmsAdmin
import org.springframework.data.repository.CrudRepository

interface RLmsAdminRepository: CrudRepository<RedisLmsAdmin, Long> {
}
