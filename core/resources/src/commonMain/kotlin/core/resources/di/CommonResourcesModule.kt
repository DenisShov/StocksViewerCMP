package core.commonresources.di

import com.core.common.mapper.ErrorMapper
import core.commonresources.StringProvider
import core.commonresources.mapper.ErrorMapperImpl
import org.koin.core.module.Module
import org.koin.dsl.module

val commonResourcesModule: Module = module {
    factory { StringProvider() }
    factory<ErrorMapper> { ErrorMapperImpl(get()) }
}
