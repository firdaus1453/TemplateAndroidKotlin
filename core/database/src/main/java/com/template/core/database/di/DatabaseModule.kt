package com.template.core.database.di

import androidx.room.Room
import com.template.core.database.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreDatabaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "template_app.db"
        ).build()
    }
    single { get<AppDatabase>().productDao }
}
