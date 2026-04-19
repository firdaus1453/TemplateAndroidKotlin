package com.template.auth.domain

data class UserDataValidator(
    val isUsernameValid: Boolean = false,
    val isPasswordValid: Boolean = false
) {
    val canLogin: Boolean
        get() = isUsernameValid && isPasswordValid

    companion object {
        fun validateUsername(username: String): Boolean {
            return username.length >= 3
        }

        fun validatePassword(password: String): Boolean {
            return password.length >= 4
        }
    }
}
