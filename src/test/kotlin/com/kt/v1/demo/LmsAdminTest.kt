package com.kt.v1.demo

import com.kt.v1.demo.dto.LmsAdminDto
import com.kt.v1.demo.service.LmsAdminService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import javax.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired

@Transactional
@SpringBootTest
class LmsAdminTest {

    @Autowired
    private lateinit var lmsAdminService: LmsAdminService

    @Test
    @DisplayName("LMS ADMIN 등록 #1")
    fun testAddLmsAdmin(){
        val lmsAdmin = LmsAdminDto()
        lmsAdmin.userId = "test_symm2212"
        lmsAdmin.userPass = "password"

        val newAdmin = lmsAdminService.addAdmin(lmsAdmin)

        assertThat(newAdmin.userId).isEqualTo(lmsAdmin.userId)
//        assertThat(newAdmin.userPass).isEqualTo(lmsAdmin.userPass)
    }

    @Test
    @DisplayName("LMS ADMIN 전체조회 #1")
    fun testLmsAdminAllList(){
        val list: List<LmsAdminDto> = lmsAdminService.selectAllAdmins()

//        list.map{ admin -> System.out.println(admin)}
        assertThat(list.size).isGreaterThan(0)
    }
}
