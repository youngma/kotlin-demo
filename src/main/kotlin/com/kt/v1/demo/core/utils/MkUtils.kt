package com.kt.v1.demo.core.utils

import java.security.MessageDigest

enum class HashAlgorithm(override val value: String) : HasValue<String> {
    MD5("MD5"), SHA256("SHA-256");
    companion object : EnumConverter<String, HashAlgorithm>(buildValueMap())
}

fun String.md5(): String {
    return hashString(this, HashAlgorithm.MD5)
}

fun String.sha256(): String {
    return hashString(this, HashAlgorithm.SHA256)
}

private fun hashString(input: String, algorithm: HashAlgorithm): String {
    return MessageDigest
        .getInstance(algorithm.value)
        .digest(input.toByteArray())
        .fold("", { str, it -> str + "%02x".format(it) })
}

class MkUtils