package com.kt.v1.demo.mapper

import com.kt.v1.demo.dto.LmsAdminDto
import com.kt.v1.demo.entity.LmsAdmin
import org.mapstruct.Mapper

@Mapper
interface LmsAdminMapper {
    fun toLmsAdmin(req: LmsAdminDto): LmsAdmin
    fun fromLmsAdmin(req: LmsAdmin): LmsAdminDto
}
