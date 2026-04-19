package com.template.app

import android.app.Application
import com.template.app.di.appModule
import com.template.auth.data.di.authDataModule
import com.template.auth.presentation.di.authPresentationModule
import com.template.core.data.di.coreDataModule
import com.template.core.database.di.coreDatabaseModule
import com.template.home.data.di.homeDataModule
import com.template.home.presentation.di.homePresentationModule
import com.template.profile.data.di.profileDataModule
import com.template.profile.presentation.di.profilePresentationModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class TemplateApp : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@TemplateApp)
            modules(
                // App
                appModule,

                // Core
                coreDataModule,
                coreDatabaseModule,

                // Auth
                authDataModule,
                authPresentationModule,

                // Home
                homeDataModule,
                homePresentationModule,

                // Profile
                profileDataModule,
                profilePresentationModule
            )
        }
    }
}
