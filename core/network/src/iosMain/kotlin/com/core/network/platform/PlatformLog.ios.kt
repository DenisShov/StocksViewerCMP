package com.core.network.platform

actual fun platformLog(tag: String, message: String) {
    println("$tag: $message")
}
