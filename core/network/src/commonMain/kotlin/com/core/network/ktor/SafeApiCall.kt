package com.core.network.ktor

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.core.network.model.errors.ApiError
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.bodyAsText
import kotlinx.io.IOException

@Suppress("TooGenericExceptionCaught")
suspend inline fun <reified T> safeApiCall(
    block: () -> T,
): Either<ApiError, T> = try {
    block().right()
} catch (e: ClientRequestException) {
    val errorBody = e.response.bodyAsText()
    val errorMessage = getErrorMessage(errorBody) ?: e.message
    ApiError.HttpError(
        code = e.response.status.value,
        message = errorMessage,
        body = errorBody,
    ).left()
} catch (e: ServerResponseException) {
    val errorBody = e.response.bodyAsText()
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
