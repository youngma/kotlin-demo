package com.kt.v1.demo.core.wapper

import org.springframework.http.HttpStatus


data class ResultResponse<T>(
    var httpStatus: HttpStatus? = HttpStatus.OK,
    var code: Int = 0,
    var message: String? = "",
    var result: T?,
) {
    private var status: Int? = null

    init {
        if (httpStatus == null) {
            this.status = HttpStatus.OK.value()
        } else {
            this.status = httpStatus!!.value()
        }
    }
}
