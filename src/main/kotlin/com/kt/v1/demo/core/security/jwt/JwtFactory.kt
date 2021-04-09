package com.kt.v1.demo.core.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.security.KeyPair
import java.time.Duration
import java.time.Instant
import java.util.*
import java.util.Date.*

@Component
class JwtFactory {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    val keyPair: KeyPair = Keys.keyPairFor(SignatureAlgorithm.RS256)

    fun createJwt(userDetails: UserDetails): String {

        val roles = userDetails.authorities.map { role -> role.authority }
        val role = roles[0]

        return Jwts.builder()
            .signWith(keyPair.private, SignatureAlgorithm.RS256)
            .setSubject(userDetails.username)
            .setIssuer("identity")
            .claim("userId", userDetails.username)
            .claim("role", role)
            .setExpiration(from(Instant.now().plus(Duration.ofMinutes(15))))
            .setIssuedAt(from(Instant.now()))
            .compact()
    }

    /**
     * Validate the JWT where it will throw an exception if it isn't valid.
     */
    fun validateJwt(jwt: String): Jws<Claims> {
        return Jwts.parserBuilder()
            .setSigningKey(keyPair.public)
            .build()
            .parseClaimsJws(jwt)
    }
}
