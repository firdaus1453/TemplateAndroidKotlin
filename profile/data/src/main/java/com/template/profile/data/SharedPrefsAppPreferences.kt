package com.template.profile.data

import android.content.Context
import android.content.SharedPreferences
import com.template.profile.domain.AppLanguage
import com.template.profile.domain.AppPreferences
import com.template.profile.domain.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SharedPrefsAppPreferences(
    context: Context
) : AppPreferences {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        "app_preferences",
        Context.MODE_PRIVATE
    )

    private val _themeMode = MutableStateFlow(loadThemeMode())
    private val _language = MutableStateFlow(loadLanguage())

    override fun getThemeMode(): Flow<ThemeMode> = _themeMode.asStateFlow()

    override suspend fun setThemeMode(mode: ThemeMode) {
        prefs.edit().putString(KEY_THEME, mode.name).apply()
        _themeMode.value = mode
    }

    override fun getLanguage(): Flow<AppLanguage> = _language.asStateFlow()

    override suspend fun setLanguage(language: AppLanguage) {
        prefs.edit()
            .putString(KEY_LANGUAGE, language.name)
            .putString(KEY_LANGUAGE_TAG, language.locale)
            .apply()
        _language.value = language
    }

    private fun loadThemeMode(): ThemeMode {
        val name = prefs.getString(KEY_THEME, ThemeMode.SYSTEM.name) ?: ThemeMode.SYSTEM.name
        return try {
            ThemeMode.valueOf(name)
        } catch (_: Exception) {
            ThemeMode.SYSTEM
        }
    }

    private fun loadLanguage(): AppLanguage {
        val name = prefs.getString(KEY_LANGUAGE, AppLanguage.ENGLISH.name) ?: AppLanguage.ENGLISH.name
        return try {
            AppLanguage.valueOf(name)
        } catch (_: Exception) {
            AppLanguage.ENGLISH
        }
    }

    companion object {
        private const val KEY_THEME = "theme_mode"
        private const val KEY_LANGUAGE = "app_language"
        private const val KEY_LANGUAGE_TAG = "app_language_tag"
    }
}
