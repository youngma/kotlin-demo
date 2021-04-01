package com.kt.v1.demo.dto

import com.kt.v1.demo.core.utils.sha256
import com.kt.v1.demo.entity.UserLevel
import com.kt.v1.demo.entity.isDeleted
import java.time.LocalDateTime

data class LmsAdminDto (
    var uid: Long = 0,
    var userId: String = "",
    var userPass: String = "",
    var name: String = "",
    var tel: String = "",
    var mobile: String = "",
    var email: String = "",
    var userLevel: Int = UserLevel.ADMIN.value,
    var delYn: String = isDeleted.No.value,
    var regdate: LocalDateTime = LocalDateTime.now()
) {
    fun encryptionPassword() {
        this.userPass = (this).userPass.sha256()
    }
}
