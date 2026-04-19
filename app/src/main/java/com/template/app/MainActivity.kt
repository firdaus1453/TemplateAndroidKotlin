package com.template.app

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.template.core.presentation.designsystem.TemplateTheme
import com.template.profile.domain.ThemeMode
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class MainActivity : ComponentActivity() {

    private val viewModel by viewModel<MainViewModel>()

    override fun attachBaseContext(newBase: Context) {
        // Apply persisted locale for pre-API 33 devices.
        // On API 33+ the system handles per-app language via LocaleManager.
        val prefs = newBase.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val langTag = prefs.getString("app_language_tag", null)

        if (langTag != null && android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.TIRAMISU) {
            val locale = Locale.forLanguageTag(langTag)
            val config = Configuration(newBase.resources.configuration)
            config.setLocale(locale)
            super.attachBaseContext(newBase.createConfigurationContext(config))
        } else {
            super.attachBaseContext(newBase)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            viewModel.state.isCheckingAuth
        }

        enableEdgeToEdge()

        setContent {
            val state = viewModel.state

            val darkTheme = when (state.themeMode) {
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
            }

            TemplateTheme(darkTheme = darkTheme) {
                if (!state.isCheckingAuth) {
                    NavigationRoot(isLoggedIn = state.isLoggedIn)
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}
