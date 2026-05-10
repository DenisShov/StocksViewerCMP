package com.core.network.platform

import io.ktor.client.engine.HttpClientEngine

expect fun createPlatformEngine(): HttpClientEngine
