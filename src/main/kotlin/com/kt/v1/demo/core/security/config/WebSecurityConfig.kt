package com.kt.v1.demo.core.security.config

import com.kt.v1.demo.core.exception.UnauthorizedException
import com.kt.v1.demo.core.security.common.FilterSkipMatcher
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import java.lang.Exception
import org.springframework.security.crypto.factory.PasswordEncoderFactories

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.http.HttpMethod

import org.springframework.security.config.http.SessionCreationPolicy
import com.kt.v1.demo.core.security.filter.JwtAuthenticationFilter
import com.kt.v1.demo.core.security.jwt.HeaderTokenExtractor

import java.util.ArrayList
import com.kt.v1.demo.core.security.filter.FormLoginFilter
import com.kt.v1.demo.core.security.handler.FormLoginAuthenticationFailureHandler
import com.kt.v1.demo.core.security.handler.FormLoginAuthenticationSuccessHandler


@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    var headerTokenExtractor: HeaderTokenExtractor,
    var formLoginAuthenticationSuccessHandler: FormLoginAuthenticationSuccessHandler,
    var formLoginAuthenticationFailureHandler: FormLoginAuthenticationFailureHandler
): WebSecurityConfigurerAdapter(
) {

    @Bean
    @Throws(Exception::class)
    fun getAuthenticationManager(): AuthenticationManager? {
        return super.authenticationManager()
    }

    // 정적 자원에 대해서는 Security 설정을 적용하지 않음.
    override fun configure(web: WebSecurity) {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations())
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
            .csrf()
            .disable()

        http
            .headers()
            .frameOptions()
            .disable()


        // 서버에서 인증은 JWT로 인증하기 때문에 Session의 생성을 막습니다.
        http
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        /*
         * 1.
         * UsernamePasswordAuthenticationFilter 이전에 FormLoginFilter, JwtFilter 를 등록합니다.
         * FormLoginFilter : 로그인 인증을 실시합니다.
         * JwtFilter       : 서버에 접근시 JWT 확인 후 인증을 실시합니다.
         */
        http
            .addFilterBefore(
                formLoginFilter(),
                UsernamePasswordAuthenticationFilter::class.java
            )
        /*
         * 1.
         * UsernamePasswordAuthenticationFilter 이전에 FormLoginFilter, JwtFilter 를 등록합니다.
         * FormLoginFilter : 로그인 인증을 실시합니다.
         * JwtFilter       : 서버에 접근시 JWT 확인 후 인증을 실시합니다.
         */http
            .addFilterBefore(
                jwtFilter(),
                UsernamePasswordAuthenticationFilter::class.java
            )

        // 권한(USER)이 필요한 접근 설정

        // 권한(USER)이 필요한 접근 설정
        http
            .authorizeRequests()
            .mvcMatchers(
                HttpMethod.GET,
                "/api/account"
            )
            .hasRole("USER")
    }

    @Throws(Exception::class)
    protected fun formLoginFilter(): FormLoginFilter? {
        val filter = FormLoginFilter(
            "/api/account/login",
            formLoginAuthenticationSuccessHandler,
            formLoginAuthenticationFailureHandler
        )
        filter.setAuthenticationManager(super.authenticationManager())
        return filter
    }

    @Throws(UnauthorizedException::class)
    private fun jwtFilter(): JwtAuthenticationFilter {
        val skipPath: MutableList<String> = ArrayList()

        // Static 정보 접근 허용
        skipPath.add("GET,/error")
        skipPath.add("GET,/favicon.ico")
        skipPath.add("GET,/static")
        skipPath.add("GET,/static/**")
        skipPath.add("POST,/api/account")
        skipPath.add("POST,/api/account/login")
        val matcher = FilterSkipMatcher(
            skipPath,
            "/**"
        )
        val filter = JwtAuthenticationFilter(
            matcher,
            headerTokenExtractor
        )
        filter.setAuthenticationManager(super.authenticationManager())
        return filter
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder? {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }
}
