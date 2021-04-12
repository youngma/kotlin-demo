package com.kt.v1.demo.core.security.filter

import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher
import javax.servlet.http.HttpServletRequest

class FilterSkipMatcher(
    var pathToSkip: List<String> = emptyList(),
    var processingPath: String? = null
) : RequestMatcher {
    private val orRequestMatcher: OrRequestMatcher
    private val processingMatcher: RequestMatcher
    private fun httpPath(skipPath: String): AntPathRequestMatcher {
        val splitStr = skipPath.split(",").toTypedArray()

        /*
         * 배열 [1] httpMathod 방식 post get 인지 구분
         * 배열 [0] 제외하는 url
         * */return AntPathRequestMatcher(
            splitStr[1],
            splitStr[0]
        )
    }

    override fun matches(req: HttpServletRequest): Boolean {
        return !orRequestMatcher.matches(req) && processingMatcher.matches(req)
    }

    init {
        orRequestMatcher = OrRequestMatcher(
            pathToSkip
                .map { skipPath: String ->
                    httpPath(
                        skipPath
                    )
                }
        )
        processingMatcher = AntPathRequestMatcher(processingPath)
    }
}
