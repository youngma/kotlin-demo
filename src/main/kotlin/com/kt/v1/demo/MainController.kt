package com.kt.v1.demo

import com.kt.v1.demo.entity.LmsAdmin
import com.kt.v1.demo.repository.LmsAdminRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/main")
class MainController( private val lmsAdminRepository: LmsAdminRepository) {

    @GetMapping("/first/{times}" )
    fun firstPage(@PathVariable times: Int? = 0): Model {
        return Model("First", times)
    }

    @GetMapping("/allAdmins")
    fun allAdmins(): List<LmsAdmin> {
        return lmsAdminRepository.findAll().map { LmsAdmin -> LmsAdmin }
    }
//
    @GetMapping("/admin/{userId}")
    fun getAdmin(@PathVariable userId: String): LmsAdmin {
        return lmsAdminRepository.findAllByUserId(userId)
    }
}

data class Model(var title: String, var times: Int? = 0)
