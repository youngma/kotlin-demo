package com.kt.v1.demo.core.security.domain

class User (
    val id: Int,
    var username: String? = null,
    var password: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var roles: Set<Role>? = null
)
