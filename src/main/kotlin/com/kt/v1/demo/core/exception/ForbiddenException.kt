package com.kt.v1.demo.core.exception

import org.springframework.http.HttpStatus

class ForbiddenException (
    httpStatus: HttpStatus = HttpStatus.FORBIDDEN,
    code: Int = 40300,
    message: String? = HttpStatus.FORBIDDEN.reasonPhrase
) : ServiceException(httpStatus, code, message)
