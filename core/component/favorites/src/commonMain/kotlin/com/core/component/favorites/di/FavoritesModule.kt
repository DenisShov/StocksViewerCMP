package com.core.component.favorites.di

import com.core.component.favorites.data.repository.FavoritesRepositoryImpl
import com.core.component.favorites.domain.repository.FavoritesRepository
import org.koin.dsl.module

val favoritesModule = module {
    single<FavoritesRepository> { FavoritesRepositoryImpl(get()) }
}
