package com.core.database.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.scope.Scope

internal actual fun Scope.platformContext(): Any? = androidContext()
