package com.core.network.platform

import timber.log.Timber

actual fun platformLog(tag: String, message: String) {
    Timber.tag(tag).d(message)
}
