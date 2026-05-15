package com.core.common.mapper

import com.core.common.error.DomainError

interface ErrorMapper {
    suspend fun mapToStringError(error: DomainError): String
}
