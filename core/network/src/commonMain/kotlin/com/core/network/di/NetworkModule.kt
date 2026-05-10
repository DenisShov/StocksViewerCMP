package com.core.network.di

import com.core.network.ktor.StocksApiService
import com.core.network.ktor.StocksApiServiceImpl
import com.core.network.platform.createPlatformEngine
import com.core.network.platform.getApiKey
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {
    single<HttpClient> { createHttpClient() }
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

    install(Logging) {
        level = LogLevel.BODY
    }

    defaultRequest {
        url {
            protocol = URLProtocol.HTTPS
            host = "api.polygon.io"
            parameters.append("apiKey", getApiKey())
        }
    }
}
