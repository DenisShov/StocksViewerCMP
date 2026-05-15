package core.resources.di

import com.core.common.mapper.ErrorMapper
import core.resources.StringProvider
import core.resources.mapper.ErrorMapperImpl
import org.koin.core.module.Module
import org.koin.dsl.module

val resourcesModule: Module = module {
    factory { StringProvider() }
    factory<ErrorMapper> { ErrorMapperImpl(get()) }
}
