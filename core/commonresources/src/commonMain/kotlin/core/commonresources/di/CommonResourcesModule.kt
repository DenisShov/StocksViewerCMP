package core.commonresources.di

import core.commonresources.StringProvider
import org.koin.core.module.Module
import org.koin.dsl.module

val commonResourcesModule: Module = module {
    factory { StringProvider() }
}
