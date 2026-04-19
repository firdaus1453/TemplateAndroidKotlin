package com.template.auth.presentation.login

data class LoginState(
    val username: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val canLogin: Boolean = false
)
