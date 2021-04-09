package com.kt.v1.demo.core.security.token

import com.kt.v1.demo.core.security.dto.AccountDTO
import org.springframework.security.core.userdetails.UserDetails

import org.springframework.security.core.authority.SimpleGrantedAuthority

import java.util.HashSet

import org.springframework.security.core.GrantedAuthority

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken


class PostAuthorizationToken  // 1.
private constructor(
    principal: Any,
    credentials: Any,
    authorities: Collection<GrantedAuthority>
) : UsernamePasswordAuthenticationToken(principal, credentials, authorities) {
    val userDetails: UserDetails
        get() = super.getPrincipal() as UserDetails

    companion object {
        fun getTokenFormUserDetails(userDetails: UserDetails): PostAuthorizationToken {
            return PostAuthorizationToken(
                userDetails,
                userDetails.password,
                userDetails.authorities
            )
        }

        fun getTokenFormUserDetails(accountDTO: AccountDTO): PostAuthorizationToken {
            val grantedAuthorities: MutableSet<GrantedAuthority> = HashSet()
            grantedAuthorities.add(
                SimpleGrantedAuthority(accountDTO.role)
            )
            return PostAuthorizationToken(
                accountDTO,
                "null password",
                grantedAuthorities
            )
        }
    }
}
