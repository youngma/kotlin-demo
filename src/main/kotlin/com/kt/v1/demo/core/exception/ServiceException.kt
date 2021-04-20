package com.kt.v1.demo.core.exception

import org.springframework.http.HttpStatus

open class ServiceException(
    val httpStatus: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
    val code: Int = 0,
    override val message: String?
) : RuntimeException() {

    constructor(customException: CustomException) : this(
        code = customException.errorCode,
        message = customException.errorMessage
    )
}