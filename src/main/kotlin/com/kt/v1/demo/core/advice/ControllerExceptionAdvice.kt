package com.kt.v1.demo.core.advice

import com.kt.v1.demo.core.exception.ServiceException
import com.kt.v1.demo.core.exception.UnauthorizedException
import com.kt.v1.demo.core.logger.Log
import com.kt.v1.demo.core.wapper.ResultResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.beans.TypeMismatchException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.xml.bind.ValidationException

@RestControllerAdvice
class ControllerExceptionAdvice( ) {
    companion object: Log
    @ExceptionHandler(ServiceException::class)
    fun handleException(e: ServiceException): ResponseEntity<Any?> {
        val resultResponse = ResultResponse<Any?>( e.code, e.message)
        logger.debug("#### {}", resultResponse)
        return ResponseEntity(resultResponse, e.httpStatus)
    }

    @ExceptionHandler(ValidationException::class, TypeMismatchException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidateException(e: Exception): ResultResponse<Any?> {
        val resultResponse = ResultResponse<Any?>(40099, e.message)
        logger.warn("#### {}", e.stackTrace as Any)
        return resultResponse
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResultResponse<Any?> {
        val resultResponse = ResultResponse<Any?>(40099, e.message)
        val errorMessages = e.bindingResult.fieldErrors
            .map { fe: FieldError ->
                String.format(
                    "%s : %s",
                    fe.field,
                    fe.defaultMessage
                )
            }
        resultResponse.message = (errorMessages.joinToString { ", " })
        logger.warn("#### {}", e.stackTrace as Any)
        return resultResponse
    }

    //
    @ExceptionHandler(
        AccessDeniedException::class,
        UnauthorizedException::class
    )
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleException(e: AccessDeniedException): ResultResponse<Any?> {
        return ResultResponse(40100, e.message)
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleException(e: Exception): ResultResponse<Any?> {
        logger.warn("#### {}", e.toString())
        for (stack in e.stackTrace) {
            logger.warn("#### {}", stack)
        }
        return ResultResponse(99999, e.message)
    }
}
