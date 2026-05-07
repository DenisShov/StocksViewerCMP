package com.core.network.di

import com.core.network.BuildConfig
import com.core.network.ktor.StocksApiService
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import timber.log.Timber

val networkModule = module {
    single<HttpClient> { createHttpClient() }
    single<StocksApiService> { StocksApiService(get()) }
}

private fun createHttpClient(): HttpClient = HttpClient(OkHttp) {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = false
        })
    }

    if (BuildConfig.DEBUG) {
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Timber.i(message)
                }
            }
            level = LogLevel.BODY
        }
    }

    defaultRequest {
        url {
            protocol = URLProtocol.HTTPS
            host = "api.polygon.io"
            parameters.append("apiKey", BuildConfig.API_KEY)
        }
    }
}
