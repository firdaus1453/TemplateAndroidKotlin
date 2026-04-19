package com.template.profile.presentation

import com.template.profile.domain.AppLanguage
import com.template.profile.domain.ThemeMode
import com.template.profile.domain.UserProfile

data class ProfileState(
    val userProfile: UserProfile? = null,
    val isLoading: Boolean = false,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val language: AppLanguage = AppLanguage.ENGLISH,
    val showThemeDialog: Boolean = false,
    val showLanguageDialog: Boolean = false
)
