package com.template.profile.presentation

import com.template.profile.domain.AppLanguage
import com.template.profile.domain.AppPreferences
import com.template.profile.domain.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeAppPreferences : AppPreferences {
    private val _themeMode = MutableStateFlow(ThemeMode.SYSTEM)
    private val _language = MutableStateFlow(AppLanguage.ENGLISH)

    override fun getThemeMode(): Flow<ThemeMode> = _themeMode
    override suspend fun setThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
    }

    override fun getLanguage(): Flow<AppLanguage> = _language
    override suspend fun setLanguage(language: AppLanguage) {
        _language.value = language
    }
}
