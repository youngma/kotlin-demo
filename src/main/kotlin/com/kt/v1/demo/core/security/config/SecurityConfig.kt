package com.kt.v1.demo.core.security.config

import com.kt.v1.demo.core.exception.UnauthorizedException
import com.kt.v1.demo.core.security.filter.FilterSkipMatcher
import com.kt.v1.demo.core.security.filter.JWTAuthenticationFilter
import com.kt.v1.demo.core.security.filter.JWTAuthorizationFilter
import com.kt.v1.demo.core.security.service.CustomUserDetailsService
import com.kt.v1.demo.core.security.vo.SecurityProperties
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    val securityProperties: SecurityProperties,
    var userDetailsService: CustomUserDetailsService,
): WebSecurityConfigurerAdapter() {

    // 정적 자원에 대해서는 Security 설정을 적용하지 않음.
    override fun configure(web: WebSecurity) {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations())
    }

    override fun configure(http: HttpSecurity) {


        val jWTAuthenticationFilter =JWTAuthenticationFilter(authenticationManager(), securityProperties)
        jWTAuthenticationFilter.setFilterProcessesUrl("/login2")

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

        http
            .cors().and()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // no sessions
            .and()
            .requestMatcher(matcher)
            .addFilter(jWTAuthenticationFilter)
            .addFilter(JWTAuthorizationFilter(authenticationManager(), securityProperties))
            .authorizeRequests()
            .anyRequest().authenticated()
            .and()

    }

    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder(securityProperties.strength)

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(bCryptPasswordEncoder())
    }

    @Bean
    fun authProvider(): DaoAuthenticationProvider = DaoAuthenticationProvider().apply {
        setUserDetailsService(userDetailsService)
        setPasswordEncoder(bCryptPasswordEncoder())
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource = UrlBasedCorsConfigurationSource().also { cors ->
        CorsConfiguration().apply {
            allowedOrigins = listOf("*")
            allowedMethods = listOf("POST", "PUT", "DELETE", "GET", "OPTIONS", "HEAD")
            allowedHeaders = listOf(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "Accept",
                "Origin",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers"
            )
            exposedHeaders = listOf(
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials",
                "Authorization",
                "Content-Disposition"
            )
            maxAge = 3600
            cors.registerCorsConfiguration("/**", this)
        }
    }
}

