package com.kt.v1.demo.mapper

import com.kt.v1.demo.dto.LmsAdminDto
import com.kt.v1.demo.entity.LmsAdmin
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface LmsAdminMapper {
    fun toLmsAdmin(req: LmsAdminDto): LmsAdmin
    fun fromLmsAdmin(req: LmsAdmin): LmsAdminDto
    fun addLmsAdmin(req: LmsAdminDto): LmsAdmin {
        val admin = LmsAdmin()
        admin.add(req)
        return admin
    }
}
