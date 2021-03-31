package com.kt.v1.demo.service

import com.kt.v1.demo.dto.LmsAdminDto
import com.kt.v1.demo.entity.LmsAdmin
import com.kt.v1.demo.mapper.LmsAdminMapper
import com.kt.v1.demo.repository.QLmsAdminRepository

class LmsAdminServiceImpl(
    private val lmsAdminMapper: LmsAdminMapper,
    private val qLmsAdminRepository: QLmsAdminRepository) : LmsAdminService {

    override fun selectAllAdmins(): List<LmsAdminDto> {
        val admins: List<LmsAdmin> = qLmsAdminRepository.selectAllAdmins()
        return admins.map { lmsAdmin -> lmsAdminMapper.fromLmsAdmin(lmsAdmin) }
    }
}
