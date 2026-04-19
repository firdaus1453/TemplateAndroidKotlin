package com.template.auth.domain

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UserDataValidatorTest {

    @Test
    fun `validateUsername returns false for empty string`() {
        assertFalse(UserDataValidator.validateUsername(""))
    }

    @Test
    fun `validateUsername returns false for 2 chars`() {
        assertFalse(UserDataValidator.validateUsername("ab"))
    }

    @Test
    fun `validateUsername returns true for 3 chars`() {
        assertTrue(UserDataValidator.validateUsername("abc"))
    }

    @Test
    fun `validateUsername returns true for long string`() {
        assertTrue(UserDataValidator.validateUsername("emilys"))
    }

    @Test
    fun `validatePassword returns false for empty`() {
        assertFalse(UserDataValidator.validatePassword(""))
    }

    @Test
    fun `validatePassword returns false for 3 chars`() {
        assertFalse(UserDataValidator.validatePassword("abc"))
    }

    @Test
    fun `validatePassword returns true for 4 chars`() {
        assertTrue(UserDataValidator.validatePassword("abcd"))
    }

    @Test
    fun `validatePassword returns true for long string`() {
        assertTrue(UserDataValidator.validatePassword("emilyspass"))
    }

    @Test
    fun `canLogin is true when both valid`() {
        val validator = UserDataValidator(
            isUsernameValid = true,
            isPasswordValid = true
        )
        assertTrue(validator.canLogin)
    }

    @Test
    fun `canLogin is false when username invalid`() {
        val validator = UserDataValidator(
            isUsernameValid = false,
            isPasswordValid = true
        )
        assertFalse(validator.canLogin)
    }

    @Test
    fun `canLogin is false when password invalid`() {
        val validator = UserDataValidator(
            isUsernameValid = true,
            isPasswordValid = false
        )
        assertFalse(validator.canLogin)
    }
}
