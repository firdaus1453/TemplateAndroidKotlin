package com.template.profile.presentation

import com.template.profile.domain.AppLanguage
import com.template.profile.domain.ThemeMode

sealed interface ProfileAction {
    data object OnRefresh : ProfileAction
    data object OnLogoutClick : ProfileAction
    data class OnThemeModeChange(val mode: ThemeMode) : ProfileAction
    data class OnLanguageChange(val language: AppLanguage) : ProfileAction
    data object OnShowThemeDialog : ProfileAction
    data object OnDismissThemeDialog : ProfileAction
    data object OnShowLanguageDialog : ProfileAction
    data object OnDismissLanguageDialog : ProfileAction
}
