package com.template.app.di

import com.template.app.MainViewModel
import com.template.app.TemplateApp
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single<CoroutineScope> {
        (androidApplication() as TemplateApp).applicationScope
    }
    viewModelOf(::MainViewModel)
}
