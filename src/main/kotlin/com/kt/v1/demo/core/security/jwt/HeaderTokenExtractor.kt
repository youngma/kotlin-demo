package com.kt.v1.demo.core.security.jwt

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.NoSuchElementException

import javax.servlet.http.HttpServletRequest


@Component
class HeaderTokenExtractor {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    /*
     * HEADER_PREFIX
     * header Authorization token 값의 표준이되는 변수
     */
    val HEADER_PREFIX = "Bearer "

    fun extract(header: String = "", request: HttpServletRequest): String {

        val isBearer = header.startsWith(HEADER_PREFIX, false) ?: false

        /*
         * - Token 값이 올바르지 않은경우 -
         * header 값이 비어있거나 또는 HEADER_PREFIX 값보다 짧은 경우
         * 이셉션을(예외)를 던져주어야 합니다.
         */
        if (!isBearer) {
            log.info("error request : {}" , request.requestURI)
            throw NoSuchElementException("올바른 Auth 정보가 아닙니다.")
        } else {
            /*
            * - Token 값이 존재하는 경우 -
            * (bearer ) 부분만 제거 후 token 값 반환
            */
            return header.substring(HEADER_PREFIX.length)
        }
    }
}
