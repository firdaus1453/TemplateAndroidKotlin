package com.template.home.presentation

import com.template.core.presentation.ui.UiText

sealed interface HomeEvent {
    data class Error(val error: UiText) : HomeEvent
}
