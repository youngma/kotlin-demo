package com.kt.v1.demo.core.security.handler

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType


class CustomAuthenticationFailureHandler(
    var objectMapper: ObjectMapper
): AuthenticationFailureHandler {
    override fun onAuthenticationFailure(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        exception: AuthenticationException?
    ) {
        TODO("Not yet implemented")
    }
}
