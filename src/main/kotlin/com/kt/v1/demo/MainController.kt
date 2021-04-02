package com.kt.v1.demo

import com.kt.v1.demo.core.exception.CustomException
import com.kt.v1.demo.core.exception.ServiceException
import com.kt.v1.demo.core.wapper.ResultResponse
import com.kt.v1.demo.dto.LmsAdminDto
import com.kt.v1.demo.entity.LmsAdmin
import com.kt.v1.demo.mapper.LmsAdminMapper
import com.kt.v1.demo.repository.LmsAdminRepository
import com.kt.v1.demo.service.LmsAdminService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/main")
class MainController(
    private val lmsAdminRepository: LmsAdminRepository,
    private val lmsAdminMapper: LmsAdminMapper,
    private val lmsAdminService: LmsAdminService
    ) {

    @GetMapping("/first/{times}" )
    fun firstPage(@PathVariable times: Int? = 0): Model {
        return Model("First", times)
    }

    @GetMapping("/allAdmins")
    fun allAdmins(): ResultResponse<List<LmsAdminDto>> {
        return ResultResponse(lmsAdminRepository.findAll().map { LmsAdmin -> lmsAdminMapper.fromLmsAdmin(LmsAdmin) })
    }

    @GetMapping("/admin/{userId}")
    fun getAdmin(@PathVariable userId: String): ResultResponse<LmsAdminDto> {
        val user = lmsAdminService.findAdmin(userId)
        return ResultResponse(user)
    }
}

data class Model(var title: String, var times: Int? = 0)
