package com.template.profile.data.di

import com.template.profile.data.KtorProfileRepository
import com.template.profile.data.SharedPrefsAppPreferences
import com.template.profile.domain.AppPreferences
import com.template.profile.domain.ProfileRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val profileDataModule = module {
    singleOf(::KtorProfileRepository).bind<ProfileRepository>()
    singleOf(::SharedPrefsAppPreferences).bind<AppPreferences>()
}
