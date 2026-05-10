package com.core.common.di

import com.core.common.mapper.ErrorMapper
import com.core.common.mapper.ErrorMapperImpl
import org.koin.dsl.module

val commonModule = module {
    factory<ErrorMapper> { ErrorMapperImpl(get()) }
}
