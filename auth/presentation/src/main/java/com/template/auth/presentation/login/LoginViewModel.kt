package com.template.auth.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.auth.domain.AuthRepository
import com.template.auth.domain.UserDataValidator
import com.template.core.domain.util.Result
import com.template.core.presentation.ui.toUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    private val eventChannel = Channel<LoginEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnUsernameChange -> {
                state = state.copy(
                    username = action.username,
                    canLogin = UserDataValidator.validateUsername(action.username) &&
                            UserDataValidator.validatePassword(state.password)
                )
            }
            is LoginAction.OnPasswordChange -> {
                state = state.copy(
                    password = action.password,
                    canLogin = UserDataValidator.validateUsername(state.username) &&
                            UserDataValidator.validatePassword(action.password)
                )
            }
            LoginAction.OnTogglePasswordVisibility -> {
                state = state.copy(isPasswordVisible = !state.isPasswordVisible)
            }
            LoginAction.OnLoginClick -> login()
        }
    }

    private fun login() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            val result = authRepository.login(
                username = state.username.trim(),
                password = state.password
            )

            state = state.copy(isLoading = false)

            when (result) {
                is Result.Error -> {
                    eventChannel.send(LoginEvent.Error(result.error.toUiText()))
                }
                is Result.Success -> {
                    eventChannel.send(LoginEvent.LoginSuccess)
                }
            }
        }
    }
}
