package com.kt.v1.demo.repository

import com.kt.v1.demo.dto.LmsAdminDto
import com.kt.v1.demo.entity.LmsAdmin
import com.kt.v1.demo.entity.QLmsAdmin
import com.kt.v1.demo.entity.isDeleted
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager

@Repository
class QLmsAdminRepository(
    val entityManager: EntityManager,
    val jpaQueryFactory: JPAQueryFactory
) {
    fun selectAllAdmins(): List<LmsAdmin> {

        val adminList: List<LmsAdmin> = jpaQueryFactory.selectFrom(QLmsAdmin.lmsAdmin)
            .fetch()

        return adminList
    }

    fun selectAdmin(lmsAdminDto: LmsAdminDto): LmsAdmin? {
        return jpaQueryFactory.selectFrom(QLmsAdmin.lmsAdmin).where(
            QLmsAdmin.lmsAdmin.userId.eq(lmsAdminDto.userId),
            QLmsAdmin.lmsAdmin.delYn.eq(isDeleted.No.value)
        ).fetchOne()
    }
}