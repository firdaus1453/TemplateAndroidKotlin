package com.template.app

import com.template.profile.domain.ThemeMode

data class MainState(
    val isLoggedIn: Boolean = false,
    val isCheckingAuth: Boolean = true,
    val themeMode: ThemeMode = ThemeMode.SYSTEM
)
