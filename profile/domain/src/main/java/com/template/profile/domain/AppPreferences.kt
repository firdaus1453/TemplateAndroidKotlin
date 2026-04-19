package com.template.profile.domain

import kotlinx.coroutines.flow.Flow

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}

enum class AppLanguage(val locale: String) {
    ENGLISH("en"),
    INDONESIAN("in"),
    JAPANESE("ja")
}

interface AppPreferences {
    fun getThemeMode(): Flow<ThemeMode>
    suspend fun setThemeMode(mode: ThemeMode)
    fun getLanguage(): Flow<AppLanguage>
    suspend fun setLanguage(language: AppLanguage)
}
