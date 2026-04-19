package com.template.home.data.di

import com.template.home.data.KtorRemoteProductDataSource
import com.template.home.data.OfflineFirstProductRepository
import com.template.home.data.RoomLocalProductDataSource
import com.template.home.domain.LocalProductDataSource
import com.template.home.domain.ProductRepository
import com.template.home.domain.RemoteProductDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val homeDataModule = module {
    singleOf(::KtorRemoteProductDataSource).bind<RemoteProductDataSource>()
    singleOf(::RoomLocalProductDataSource).bind<LocalProductDataSource>()
    singleOf(::OfflineFirstProductRepository).bind<ProductRepository>()
}
