package com.kt.v1.demo.core.controller

import com.kt.v1.demo.core.logger.Log
import lombok.extern.slf4j.Slf4j
import org.reflections.Reflections.log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.servlet.error.ErrorAttributes
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.WebRequest
import java.util.*
import javax.servlet.http.HttpServletRequest

@Slf4j
@RestController
class DefaultErrorController @Autowired constructor(
    var environment: Environment?,
    var serverProperties: ServerProperties,
    var errorAttributes: ErrorAttributes,
    var messageSource: MessageSource,
    ) : AbstractErrorController(errorAttributes) {

    companion object {
        const val ERROR_PATH = "\${error.path:/error}"
        const val ERROR_DEFAULT_MESSAGE = "No message available."
    }

    @RequestMapping(ERROR_PATH)
    fun error( request: WebRequest, locale: Locale): ResponseEntity<Map<String, Any>> {

        val body = errorAttributes.getErrorAttributes(request,  ErrorAttributeOptions.defaults())
        val status = when(body["status"]) {
            is Int -> HttpStatus.valueOf(body["status"] as Int)
            else -> HttpStatus.INTERNAL_SERVER_ERROR
        }

        body["message"] = when (isProduction() && status.value() >= 500) {
            true -> ERROR_DEFAULT_MESSAGE
            else -> body["message"]
        }

        return ResponseEntity.status(status).body(body)
    }

    private fun isProduction(): Boolean {
        return !environment?.activeProfiles?.toList()?.contains("production")!!
    }

    private fun getError(request: HttpServletRequest): Throwable {
        return request.getAttribute("javax.servlet.error.exception") as Throwable
    }
//
//    private fun getErrorMessage(request: WebRequest): String? {
//
//        this.getErrorAttributes(request, ErrorAttributeOptions.defaults())
//        val exc = request.getAttribute("javax.servlet.error.exception", WebRequest.SCOPE_SESSION)
//        return if (exc != null) exc.toString() else getErrorMessage(request, ERROR_DEFAULT_MESSAGE)
//    }
//
//    private fun getErrorMessage(request: WebRequest, defaultMessage: String): String {
//        val message = request.getAttribute("javax.servlet.error.message", WebRequest.SCOPE_SESSION)
//        return if (ObjectUtils.isEmpty(message)) defaultMessage else message as String
//    }

    override fun getErrorPath() = serverProperties.error.path
}

@Configuration
class ErrorPropertiesSupport {
    @Bean
    fun errorPropertiesPostProcessor(@Value(DefaultErrorController.ERROR_PATH) errorPath: String): BeanPostProcessor {
        return object : BeanPostProcessor {
            override fun postProcessBeforeInitialization(bean: Any, beanName: String) = bean
            override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {
                if (bean is ServerProperties) {
                    bean.error.path = errorPath
                }
                return bean
            }
        }
    }
}
