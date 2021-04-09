package com.kt.v1.demo.core.security.handler

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.AuthenticationException
import javax.servlet.http.HttpServletResponse

import javax.servlet.http.HttpServletRequest

import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Component


@Component
class FormLoginAuthenticationFailureHandler : AuthenticationFailureHandler {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)
    override fun onAuthenticationFailure(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        exception: AuthenticationException
    ) {
        log.error(exception.message)
    }
}
