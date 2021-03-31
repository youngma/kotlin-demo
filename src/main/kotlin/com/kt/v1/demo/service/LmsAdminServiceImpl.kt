package com.kt.v1.demo.service

import com.kt.v1.demo.dto.LmsAdminDto
import com.kt.v1.demo.entity.LmsAdmin
import com.kt.v1.demo.mapper.LmsAdminMapper
import com.kt.v1.demo.mapper.LmsAdminMapperImpl
import com.kt.v1.demo.repository.LmsAdminRepository
import com.kt.v1.demo.repository.QLmsAdminRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class LmsAdminServiceImpl(
    private val lmsAdminMapper: LmsAdminMapper,
    private val qLmsAdminRepository: QLmsAdminRepository,
    private val lmsAdminRepository: LmsAdminRepository
    ) : LmsAdminService {

    override fun selectAllAdmins(): List<LmsAdminDto> {
        val admins: List<LmsAdmin> = qLmsAdminRepository.selectAllAdmins()
        return admins.map { lmsAdmin -> lmsAdminMapper.fromLmsAdmin(lmsAdmin) }
    }

    override fun addAdmin(lmsAdminDto: LmsAdminDto): LmsAdminDto {
        val newAdmin: LmsAdmin = lmsAdminMapper.addLmsAdmin(lmsAdminDto)
        lmsAdminRepository.save(newAdmin)
        return lmsAdminMapper.fromLmsAdmin(newAdmin)
    }
}
