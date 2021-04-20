package com.kt.v1.demo.core.exception

import org.springframework.http.HttpStatus

class UnauthorizedException(
    httpStatus: HttpStatus = HttpStatus.UNAUTHORIZED,
    code: Int = 40100,
    message: String? = HttpStatus.UNAUTHORIZED.reasonPhrase
) : ServiceException(httpStatus, code, message)