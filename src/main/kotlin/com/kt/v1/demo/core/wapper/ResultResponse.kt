package com.kt.v1.demo.core.wapper

import org.springframework.http.HttpStatus


class ResultResponse<T> {

//    var status: Int? = 0
//    var httpStatus: HttpStatus? = HttpStatus.OK
    var code: Int = 0
    var message: String? = ""
    var result: Any? = null

//    init {
//        if (httpStatus == null) {
//            this.status = HttpStatus.OK.value()
//        } else {
//            this.status = httpStatus!!.value()
//        }
//    }

    constructor(result: Any?) {
        this.result = result
    }

    constructor( code: Int, message: String? = null) {
//        this.status = httpStatus.value()
        this.code = code
        this.message = message
    }

    constructor(code: Int, message: String? = null, result: Any?) {
//        this.status = httpStatus.value()
        this.code = code
        this.message = message
        this.result = result
    }
}
