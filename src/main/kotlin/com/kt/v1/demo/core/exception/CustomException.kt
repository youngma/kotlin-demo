package com.kt.v1.demo.core.exception

interface BaseExceptionType {
    val errorCode: Int
    val errorMessage: String?
}

enum class CustomException(
    override val errorCode: Int,
    override val errorMessage: String
) : BaseExceptionType {
    NOT_FOUND_USER(
        1001,
        "해당하는 사용자가 존재하지 않습니다."
    ),
    DUPLICATED_USER(
        1002,
        "이미 존재하는 사용자 아이디입니다."
    ),
    LOGIN_INFORMATION_NOT_FOUND(
        1004,
        "로그인 정보를 찾을 수 없습니다. (세션 만료)"
    );
}