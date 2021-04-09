package com.kt.v1.demo.core.security.handler

import org.springframework.http.HttpStatus

import javax.servlet.http.HttpServletResponse

import org.springframework.security.core.userdetails.UserDetails

import javax.servlet.http.HttpServletRequest

import com.fasterxml.jackson.databind.ObjectMapper
import com.kt.v1.demo.core.security.dto.TokenDTO

import com.kt.v1.demo.core.security.jwt.JwtFactory
import com.kt.v1.demo.core.security.token.PostAuthorizationToken
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication

import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import java.io.IOException


@Component
class FormLoginAuthenticationSuccessHandler(
    val factory: JwtFactory
) : AuthenticationSuccessHandler {

    private val objectMapper: ObjectMapper? = null

    // 1.
    @Throws(IOException::class)
    override fun onAuthenticationSuccess(
        req: HttpServletRequest?,
        res: HttpServletResponse,
        auth: Authentication
    ) {
        val token: PostAuthorizationToken = auth as PostAuthorizationToken
        val userDetails: UserDetails = token.userDetails

        // 2.
        val tokenString: String = factory.createJwt(userDetails)

        // 3.
        val tokenDTO = TokenDTO(tokenString, userDetails.username)
        processResponse(res, tokenDTO)
    }

    @Throws(IOException::class)
    private fun processResponse(
        res: HttpServletResponse,
        dto: TokenDTO
    ) {
        res.contentType = MediaType.APPLICATION_JSON_VALUE
        res.status = HttpStatus.OK.value()
        res.writer.write(objectMapper!!.writeValueAsString(dto))
    }
}
