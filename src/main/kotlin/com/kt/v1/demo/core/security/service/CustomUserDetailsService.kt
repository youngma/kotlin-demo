package com.kt.v1.demo.core.security.service

import com.kt.v1.demo.repository.LmsAdminRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
class CustomUserDetailsService(
    var userRepository: LmsAdminRepository
): UserDetailsService {

    override fun loadUserByUsername(userId: String): UserDetails {

        val user = userRepository.findAllByUserId(userId) ?: throw UsernameNotFoundException("The username $userId doesn't exist")
        val authorities = ArrayList<GrantedAuthority>()
        authorities.add(SimpleGrantedAuthority("USER"))

//        user.roles!!.forEach { authorities.add(SimpleGrantedAuthority(it.roleName)) }

        return User(
            user.userId,
            user.userPass,
            authorities
        )
    }
}
