package com.kt.v1.demo.core.security.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.kt.v1.demo.core.security.domain.User
import com.kt.v1.demo.core.security.vo.SecurityProperties
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import java.io.IOException
import java.time.Duration
import java.time.Instant
import java.util.*
import java.util.Date.from
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.collections.ArrayList


class JWTAuthenticationFilter(
    private val authManager: AuthenticationManager,
    private val securityProperties: SecurityProperties,
) : UsernamePasswordAuthenticationFilter() {

    private var log: Logger = LoggerFactory.getLogger(this::class.java)

    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(
        req: HttpServletRequest,
        res: HttpServletResponse?
    ): Authentication {
        return try {

            log.info("### JWTAuthenticationFilter")

            val user: User = ObjectMapper().readValue(req.reader, User::class.java)
            authManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    user.username,
                    user.password,
                    ArrayList<GrantedAuthority>()
                )
            )
        } catch (e: IOException) {
            throw AuthenticationServiceException(e.message)
        }
    }

    @Throws(IOException::class, ServletException::class)
    override fun successfulAuthentication(
        req: HttpServletRequest,
        res: HttpServletResponse,
        chain: FilterChain?,
        auth: Authentication
    ) {
        val claims: MutableList<String> = mutableListOf()
        if (auth.authorities.isNotEmpty())
            auth.authorities.forEach { a -> claims.add(a.toString()) }

        val token = Jwts.builder()
            .setSubject((auth.principal as User).username)
            .claim("auth", claims)
            .setExpiration(from(Instant.now().plus(Duration.ofMinutes(securityProperties.expirationTime.toLong()))))
            .signWith(Keys.hmacShaKeyFor(securityProperties.secret.toByteArray()), SignatureAlgorithm.HS512)
            .compact()
        res.addHeader(securityProperties.headerString, securityProperties.tokenPrefix + token)
    }
}
