package com.core.network.di

import com.core.common.constants.CommonConstants
import com.core.network.ktor.StocksApiService
import com.core.network.ktor.StocksApiServiceImpl
import com.core.network.platform.createPlatformEngine
import com.core.network.platform.getApiKey
import com.core.network.platform.platformLog
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import org.koin.mp.KoinPlatform.getKoin

private const val LOG_TAG = "StocksViewerCMP"

val networkModule = module {
    single<HttpClient> {
        createHttpClient()
    }
    single<StocksApiService> { StocksApiServiceImpl(get()) }
}

private fun createHttpClient(): HttpClient = HttpClient(createPlatformEngine()) {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = false
        })
    }

    val isDebugBuild = getKoin().getProperty(CommonConstants.PROPERTY_IS_DEBUG_BUILD) ?: false
    if (isDebugBuild) {
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    platformLog(LOG_TAG, message)
                }
            }
            level = LogLevel.ALL
        }
    }

    defaultRequest {
        url {
            protocol = URLProtocol.HTTPS
            host = "api.polygon.io"
            parameters.append("apiKey", getApiKey())
        }
    }
}
