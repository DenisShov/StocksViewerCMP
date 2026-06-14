package com.core.navigation.di

import com.core.navigation.Navigator
import org.koin.dsl.module

val navigationModule = module {
    single { Navigator() }
}
