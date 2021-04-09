package com.kt.v1.demo.core.security.vo

import org.springframework.stereotype.Component

@Component
class SecurityProperties {
    var secret = ""
    var expirationTime: Int = 31 // in days
    var tokenPrefix = "Bearer "
    var headerString = "Authorization"
    var strength = 10
}
