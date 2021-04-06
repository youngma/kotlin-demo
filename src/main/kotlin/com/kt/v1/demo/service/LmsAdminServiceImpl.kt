package com.kt.v1.demo.service

import com.kt.v1.demo.core.exception.CustomException
import com.kt.v1.demo.core.exception.ServiceException
import com.kt.v1.demo.dto.LmsAdminDto
import com.kt.v1.demo.entity.LmsAdmin
import com.kt.v1.demo.mapper.LmsAdminMapper
import com.kt.v1.demo.repository.LmsAdminRepository
import com.kt.v1.demo.repository.QLmsAdminRepository
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class LmsAdminServiceImpl(
    private val lmsAdminMapper: LmsAdminMapper,
    private val qLmsAdminRepository: QLmsAdminRepository,
    private val lmsAdminRepository: LmsAdminRepository,
    private var kafkaTemplate: KafkaTemplate<String, Any>
    ) : LmsAdminService {

    override fun selectAllAdmins(): List<LmsAdminDto> {
        val admins: List<LmsAdmin> = qLmsAdminRepository.selectAllAdmins()
        return admins.map { lmsAdmin -> lmsAdminMapper.fromLmsAdmin(lmsAdmin) }
    }

    override fun addAdmin(lmsAdminDto: LmsAdminDto): LmsAdminDto {

        val searchAdmin = lmsAdminRepository.findAllByUserId(lmsAdminDto.userId)

        if (searchAdmin != null) {
            throw Exception("이미 등록된 사용자 입니다.")
        }

        lmsAdminDto.encryptionPassword();

        val newAdmin: LmsAdmin = lmsAdminMapper.addLmsAdmin(lmsAdminDto)
        lmsAdminRepository.save(newAdmin)
        return lmsAdminMapper.fromLmsAdmin(newAdmin)
    }

    override fun findAdmin(userId: String): LmsAdminDto {
        val admin = lmsAdminRepository.findAllByUserId(userId)
            ?: throw ServiceException(CustomException.LOGIN_INFORMATION_NOT_FOUND)

        val retAdmin = lmsAdminMapper.fromLmsAdmin(admin)

        kafkaTemplate.send("totic_lms_admin_status", retAdmin.userId, retAdmin)

        return retAdmin
    }
}
