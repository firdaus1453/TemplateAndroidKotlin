package com.template.profile.presentation

import com.template.core.presentation.ui.UiText

sealed interface ProfileEvent {
    data class Error(val error: UiText) : ProfileEvent
    data object LogoutSuccess : ProfileEvent
    data class LanguageChanged(val localeTag: String) : ProfileEvent
}
