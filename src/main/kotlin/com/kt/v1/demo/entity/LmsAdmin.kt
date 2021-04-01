package com.kt.v1.demo.entity

import com.kt.v1.demo.common.utils.EnumConverter
import com.kt.v1.demo.common.utils.HasValue
import com.kt.v1.demo.common.utils.buildValueMap
import com.kt.v1.demo.common.utils.sha256
import com.kt.v1.demo.dto.LmsAdminDto
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "lms_admin")
class LmsAdmin(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid") var uid: Long = 0L,
    @Column(name = "user_id", nullable = false) var userId: String = "",
    @Column(name = "user_pass", nullable = false) var userPass: String = "",
    @Column(name = "name", nullable = false) var name: String = "",
    @Column(name = "tel", nullable = false) var tel: String = "",
    @Column(name = "mobile", nullable = false) var mobile: String = "",
    @Column(name = "email", nullable = false) var email: String = "",
    @Column(name = "user_level", nullable = false) var userLevel: Int = UserLevel.ADMIN.value,
    @Column(name = "del_yn", nullable = false) var delYn: String = isDeleted.No.value,
    @CreatedDate @Column(name = "regdate", nullable = false) var regdate: LocalDateTime = LocalDateTime.now()
) {

    fun add(lmsAdmin: LmsAdminDto) {
        this.userId = lmsAdmin.userId
        this.userPass = lmsAdmin.userPass
        this.name = lmsAdmin.name
        this.tel = lmsAdmin.tel
        this.mobile = lmsAdmin.mobile
        this.email = lmsAdmin.email
    }

    fun comparePassword(userPass: String): Boolean {
        return this.userPass == userPass.sha256()
    }
}
//(override val value: Int): HasValue<Int> {
//    SMALL(0), MEDIUM(1), LARGE(2);
//    companion object : EnumConverter<Int, Size>(buildValueMap())
//}

enum class UserLevel(override val value: Int): HasValue<Int> {
    ADMIN(1),
    MANAGER(2);
    companion object : EnumConverter<Int, UserLevel>(buildValueMap())
}

enum class isDeleted(override val value: String): HasValue<String> {
    No("N"),
    Yes("Y");
    companion object : EnumConverter<String, isDeleted>(buildValueMap())
}
