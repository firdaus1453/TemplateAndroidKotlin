package com.template.profile.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.core.domain.SessionStorage
import com.template.core.domain.util.Result
import com.template.core.presentation.ui.toUiText
import com.template.profile.domain.AppPreferences
import com.template.profile.domain.ProfileRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val sessionStorage: SessionStorage,
    private val appPreferences: AppPreferences
) : ViewModel() {

    var state by mutableStateOf(ProfileState())
        private set

    private val eventChannel = Channel<ProfileEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        observePreferences()
        loadProfile()
    }

    fun onAction(action: ProfileAction) {
        when (action) {
            ProfileAction.OnRefresh -> loadProfile()
            ProfileAction.OnLogoutClick -> logout()
            is ProfileAction.OnThemeModeChange -> {
                viewModelScope.launch {
                    appPreferences.setThemeMode(action.mode)
                    state = state.copy(showThemeDialog = false)
                }
            }
            is ProfileAction.OnLanguageChange -> {
                viewModelScope.launch {
                    appPreferences.setLanguage(action.language)
                    state = state.copy(showLanguageDialog = false)
                    eventChannel.send(ProfileEvent.LanguageChanged(action.language.locale))
                }
            }
            ProfileAction.OnShowThemeDialog -> {
                state = state.copy(showThemeDialog = true)
            }
            ProfileAction.OnDismissThemeDialog -> {
                state = state.copy(showThemeDialog = false)
            }
            ProfileAction.OnShowLanguageDialog -> {
                state = state.copy(showLanguageDialog = true)
            }
            ProfileAction.OnDismissLanguageDialog -> {
                state = state.copy(showLanguageDialog = false)
            }
        }
    }

    private fun observePreferences() {
        appPreferences.getThemeMode()
            .onEach { mode -> state = state.copy(themeMode = mode) }
            .launchIn(viewModelScope)

        appPreferences.getLanguage()
            .onEach { lang -> state = state.copy(language = lang) }
            .launchIn(viewModelScope)
    }

    private fun loadProfile() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = profileRepository.getCurrentUser()
            state = state.copy(isLoading = false)

            when (result) {
                is Result.Error -> {
                    eventChannel.send(ProfileEvent.Error(result.error.toUiText()))
                }
                is Result.Success -> {
                    state = state.copy(userProfile = result.data)
                }
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            sessionStorage.set(null)
            eventChannel.send(ProfileEvent.LogoutSuccess)
        }
    }
}
