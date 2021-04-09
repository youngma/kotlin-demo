package com.kt.v1.demo.core.security.filter

import com.kt.v1.demo.core.exception.UnauthorizedException
import com.kt.v1.demo.core.security.common.FilterSkipMatcher
import org.springframework.security.core.context.SecurityContextHolder

import javax.servlet.http.HttpServletResponse

import javax.servlet.http.HttpServletRequest

import javax.servlet.ServletException

import javax.servlet.FilterChain

import com.kt.v1.demo.core.security.jwt.HeaderTokenExtractor
import com.kt.v1.demo.core.security.token.JwtPreProcessingToken
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException

import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import java.io.IOException


/**
 * Token 을 내려주는 Filter 가 아닌  client 에서 받아지는 Token 을 서버 사이드에서 검증하는 클레스 SecurityContextHolder 보관소에 해당
 * Token 값의 인증 상태를 보관 하고 필요할때 마다 인증 확인 후 권한 상태 확인 하는 기능
 */
class JwtAuthenticationFilter(
    private var requiresAuthenticationRequestMatcher: FilterSkipMatcher? = null,
    private var extractor: HeaderTokenExtractor
) : AbstractAuthenticationProcessingFilter(requiresAuthenticationRequestMatcher) {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    @Throws(AuthenticationException::class, Exception::class, UnauthorizedException::class)
    override fun attemptAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse
    ): Authentication {

        // JWT 값을 담아주는 변수 TokenPayload
        val tokenPayload = request.getHeader("Authorization") ?: throw UnauthorizedException()
        val preAuthorizationToken = JwtPreProcessingToken(
            extractor.extract(tokenPayload, request)
        )

        return super
            .getAuthenticationManager()
            .authenticate(preAuthorizationToken)
    }

    @Throws(IOException::class, ServletException::class)
    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        chain: FilterChain,
        authResult: Authentication?
    ) {
        /*
         *  SecurityContext 사용자 Token 저장소를 생성합니다.
         *  SecurityContext 에 사용자의 인증된 Token 값을 저장합니다.
         */
        val context = SecurityContextHolder.createEmptyContext()
        context.authentication = authResult
        SecurityContextHolder.setContext(context)

        // FilterChain chain 해당 필터가 실행 후 다른 필터도 실행할 수 있도록 연결실켜주는 메서드
        chain.doFilter(
            request,
            response
        )
    }

    override fun unsuccessfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        failed: AuthenticationException?
    ) {
        /*
         *	로그인을 한 상태에서 Token값을 주고받는 상황에서 잘못된 Token값이라면
         *	인증이 성공하지 못한 단계 이기 때문에 잘못된 Token값을 제거합니다.
         *	모든 인증받은 Context 값이 삭제 됩니다.
         */
        log.error("### unsuccessfulAuthentication {}", failed)
        SecurityContextHolder.clearContext()
        unsuccessfulAuthentication(
            request,
            response,
            failed
        )
    }
}
