package com.core.network.platform

/**
 * Writes a network log line to the platform-specific logging facility
 * (Timber on Android, the system console on iOS).
 */
expect fun platformLog(tag: String, message: String)
