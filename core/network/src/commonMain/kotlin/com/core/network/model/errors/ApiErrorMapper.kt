package com.core.network.model.errors

import arrow.core.Either
import com.core.common.error.DomainError

fun <T> Either<ApiError, T>.mapLeftToDomainError(): Either<DomainError, T> = mapLeft {
    it.toDomainError()
}

private fun ApiError.toDomainError(): DomainError = when (this) {
    is ApiError.HttpError -> DomainError.HttpError(code, message)
    is ApiError.NetworkError -> DomainError.MissingNetworkConnection
    is ApiError.UnknownError -> DomainError.GeneralError(throwable)
}
