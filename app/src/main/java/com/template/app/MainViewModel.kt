package com.template.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.core.domain.SessionStorage
import com.template.profile.domain.AppPreferences
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainViewModel(
    private val sessionStorage: SessionStorage,
    appPreferences: AppPreferences
) : ViewModel() {

    var state by mutableStateOf(MainState())
        private set

    init {
        appPreferences.getThemeMode()
            .onEach { mode -> state = state.copy(themeMode = mode) }
            .launchIn(viewModelScope)

        viewModelScope.launch {
            state = state.copy(isCheckingAuth = true)
            state = state.copy(
                isLoggedIn = sessionStorage.get() != null,
                isCheckingAuth = false
            )
        }
    }
}
