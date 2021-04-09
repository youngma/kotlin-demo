package com.kt.v1.demo.core.security.config

import com.kt.v1.demo.core.security.filter.JWTAuthenticationFilter
import com.kt.v1.demo.core.security.filter.JWTAuthorizationFilter
import com.kt.v1.demo.core.security.service.CustomUserDetailsService
import com.kt.v1.demo.core.security.vo.SecurityProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.CachingUserDetailsService
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    val bCryptPasswordEncoder: BCryptPasswordEncoder,
    val securityProperties: SecurityProperties,
    var userDetailsService: CustomUserDetailsService,
): WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(bCryptPasswordEncoder)
    }

    override fun configure(web: WebSecurity) {
        super.configure(web)
    }

    override fun configure(http: HttpSecurity) {
        http
            .cors().and()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // no sessions
            .and()
            .authorizeRequests()
            .antMatchers("/api/**").permitAll()
            .antMatchers("/error/**").permitAll()
            .antMatchers(HttpMethod.POST, "/login").permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilter(JWTAuthenticationFilter(authenticationManager(), securityProperties))
            .addFilter(JWTAuthorizationFilter(authenticationManager(), securityProperties))
    }

    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder(securityProperties.strength)

    @Bean
    fun authProvider(): DaoAuthenticationProvider = DaoAuthenticationProvider().apply {
        setUserDetailsService(userDetailsService)
        setPasswordEncoder(bCryptPasswordEncoder)
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
