package com.kt.v1.demo.core.security.token

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

class PreAuthorizationToken( username: String,  password: String ): UsernamePasswordAuthenticationToken(username, password) {
}
