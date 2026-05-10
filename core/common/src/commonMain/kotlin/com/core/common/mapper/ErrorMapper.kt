package com.core.common.mapper

import com.core.common.error.DomainError
import core.commonresources.StringProvider
import stocksviewercmp.core.commonresources.generated.resources.Res
import stocksviewercmp.core.commonresources.generated.resources.no_network_connection
import stocksviewercmp.core.commonresources.generated.resources.some_server_problem
import stocksviewercmp.core.commonresources.generated.resources.something_went_wrong

interface ErrorMapper {
    suspend fun mapToStringError(error: DomainError): String
}

class ErrorMapperImpl(private val stringProvider: StringProvider) : ErrorMapper {

    override suspend fun mapToStringError(error: DomainError): String = when (error) {
        is DomainError.HttpError -> error.message
            ?: stringProvider.getString(Res.string.some_server_problem)

        is DomainError.MissingNetworkConnection -> stringProvider.getString(Res.string.no_network_connection)
        is DomainError.GeneralError -> stringProvider.getString(Res.string.something_went_wrong)
    }
}
