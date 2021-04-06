package com.kt.v1.demo.consumer

import com.kt.v1.demo.dto.LmsAdminDto
import com.kt.v1.demo.entity.LmsAdmin
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class LmsAdminConsumer (
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @KafkaListener(topics = ["totic_lms_admin_status"], groupId = "admin-service")
    fun listen(user: LmsAdminDto) {
        logger.info("User created 1-> {}", user)
    }
}
