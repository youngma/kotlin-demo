package com.kt.v1.demo.core.security.filter

import javax.servlet.ServletException

import javax.servlet.http.HttpServletResponse

import javax.servlet.http.HttpServletRequest

import javax.servlet.FilterChain

import com.fasterxml.jackson.databind.ObjectMapper
import com.kt.v1.demo.core.security.dto.AccountDTO
import com.kt.v1.demo.core.security.token.PreAuthorizationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException

import org.springframework.security.web.authentication.AuthenticationFailureHandler

import org.springframework.security.web.authentication.AuthenticationSuccessHandler

import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import java.io.IOException


class FormLoginFilter(
    defaultUrl: String,
    successHandler: AuthenticationSuccessHandler,
    failureHandler: AuthenticationFailureHandler
) : AbstractAuthenticationProcessingFilter(defaultUrl) {

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        // JSON 으로 변환
        // JSON 으로 변환
        val dto: AccountDTO = ObjectMapper().readValue(
            request.reader,
            AccountDTO::class.java
        )

        // 사용자입력값이 존재하는지 비교
        val token = PreAuthorizationToken(dto.username, dto.username)

        // PreAuthorizationToken 해당 객체에 맞는 Provider를
        // getAuthenticationManager 해당 메서드가 자동으로 찾아서 연결해 준다.
        // 자동으로 찾아준다고 해도 Provider 에 직접 PreAuthorizationToken 지정해 줘야 찾아갑니다.
        return super
            .getAuthenticationManager()
            .authenticate(token)
    }
}
