package com.template.auth.presentation.login

sealed interface LoginAction {
    data class OnUsernameChange(val username: String) : LoginAction
    data class OnPasswordChange(val password: String) : LoginAction
    data object OnTogglePasswordVisibility : LoginAction
    data object OnLoginClick : LoginAction
}
