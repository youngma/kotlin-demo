package com.kt.v1.demo.core.redis

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import io.lettuce.core.ReadFrom.REPLICA_PREFERRED
import org.springframework.data.redis.connection.RedisStaticMasterReplicaConfiguration


@Configuration
@EnableRedisRepositories
class RedisConfig {

    @Bean
    fun redisConnectionFactory(
        @Value("\${spring.redis.primary.host}") masterHostName: String,
        @Value("\${spring.redis.reader.host}") replicaHostName: String,
        @Value("\${spring.redis.primary:6379}") masterPort: Int,
        @Value("\${spring.redis.reader:6379}") replicaPort: Int,
        @Value("\${spring.redis.database:10}") database: Int,
    ): LettuceConnectionFactory {

        val elasticCache = RedisStaticMasterReplicaConfiguration(masterHostName, masterPort)
        elasticCache.addNode(replicaHostName, replicaPort)

        val clientConfig = LettuceClientConfiguration.builder()
            .readFrom(REPLICA_PREFERRED)
            .build()

        return LettuceConnectionFactory(elasticCache, clientConfig)
    }

    @Bean
    fun redisTemplate(
        redisConnectionFactory: RedisConnectionFactory
    ): RedisTemplate<Any, Any>? {
        val redisTemplate = RedisTemplate<Any, Any>()

        val redisSerializer = Jackson2JsonRedisSerializer(Any::class.java)
            .apply {
                setObjectMapper(
                    jacksonObjectMapper()
                        .registerModule(JavaTimeModule())
                )
            }

        redisTemplate.keySerializer = StringRedisSerializer()
        redisTemplate.valueSerializer = redisSerializer
        redisTemplate.hashKeySerializer = StringRedisSerializer()
        redisTemplate.hashValueSerializer = redisSerializer
        redisTemplate.setConnectionFactory(redisConnectionFactory)
        return redisTemplate
    }
}
