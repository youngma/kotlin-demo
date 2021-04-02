package com.kt.v1.demo.repository

import com.kt.v1.demo.entity.RedisLmsAdmin
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.RedisTemplate

class RLmsAdminRepositoryImpl(
    var redisTemplate: RedisTemplate<String, RedisLmsAdmin>,
    var hashOperations: HashOperations<String, Long, RedisLmsAdmin> = redisTemplate.opsForHash()
): RLmsAdminRepository {

    override fun save(user: RedisLmsAdmin) {
        hashOperations.put("LmsUser", user.uid, user);
    }

    override fun findAll(): Map<Long, RedisLmsAdmin?>? {
        return hashOperations.entries("LmsUser")
    }

    override fun findById(id: Long): RedisLmsAdmin? {
        return hashOperations.get("LmsUser", id)
    }

    override fun update(user: RedisLmsAdmin) {
        save(user)
    }

    override fun delete(id: String) {
        hashOperations.delete("LmsUser", id)
    }
}

