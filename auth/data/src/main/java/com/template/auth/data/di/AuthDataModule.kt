package com.template.auth.data.di

import com.template.auth.data.KtorAuthRepository
import com.template.auth.domain.AuthRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authDataModule = module {
    singleOf(::KtorAuthRepository).bind<AuthRepository>()
}
