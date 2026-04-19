package com.template.core.data.di

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.template.core.data.auth.EncryptedSessionStorage
import com.template.core.data.networking.HttpClientFactory
import com.template.core.domain.SessionStorage
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    single<SharedPreferences> {
        EncryptedSharedPreferences.create(
            androidContext(),
            "auth_prefs",
            MasterKey.Builder(androidContext())
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
    singleOf(::EncryptedSessionStorage).bind<SessionStorage>()
    singleOf(::HttpClientFactory)
    single { get<HttpClientFactory>().build() }
}
