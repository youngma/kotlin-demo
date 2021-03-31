package com.kt.v1.demo.service

import com.kt.v1.demo.dto.LmsAdminDto

interface LmsAdminService {
    fun selectAllAdmins(): List<LmsAdminDto>
    fun addAdmin(lmsAdminDto: LmsAdminDto): LmsAdminDto
}
