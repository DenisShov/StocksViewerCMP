package com.core.network.ktor

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.core.network.model.errors.ApiError
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.bodyAsText
import kotlinx.io.IOException

@Suppress("TooGenericExceptionCaught")
suspend inline fun <reified T> safeApiCall(
    block: () -> T,
): Either<ApiError, T> = try {
    block().right()
} catch (e: ResponseException) {
    // Covers 4xx (ClientRequestException) and 5xx (ServerResponseException).
    // Reading the body can itself fail, so guard it while still keeping the status code.
    val errorBody = runCatching { e.response.bodyAsText() }.getOrNull()
    val errorMessage = getErrorMessage(errorBody) ?: e.message
    ApiError.HttpError(
        code = e.response.status.value,
        message = errorMessage,
        body = errorBody,
    ).left()
} catch (e: IOException) {
    ApiError.NetworkError(e).left()
} catch (e: Exception) {
    ApiError.UnknownError(e).left()
}

fun getErrorMessage(errorBody: String?): String? =
    errorBody?.let {
        """"error":"([^"]+)"""".toRegex().find(it)?.groupValues?.get(1)
    }
