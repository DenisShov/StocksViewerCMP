package core.resources.mapper

import com.core.common.error.DomainError
import com.core.common.mapper.ErrorMapper
import core.resources.StringProvider
import stocksviewercmp.core.resources.generated.resources.Res
import stocksviewercmp.core.resources.generated.resources.no_network_connection
import stocksviewercmp.core.resources.generated.resources.some_server_problem
import stocksviewercmp.core.resources.generated.resources.something_went_wrong

class ErrorMapperImpl(private val stringProvider: StringProvider) : ErrorMapper {

    override suspend fun mapToStringError(error: DomainError): String = when (error) {
        is DomainError.HttpError -> error.message
            ?: stringProvider.getString(Res.string.some_server_problem)

        is DomainError.MissingNetworkConnection -> stringProvider.getString(Res.string.no_network_connection)
        is DomainError.GeneralError -> stringProvider.getString(Res.string.something_went_wrong)
    }
}
