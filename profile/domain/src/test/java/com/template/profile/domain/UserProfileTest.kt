package com.template.profile.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class UserProfileTest {

    @Test
    fun `fullName concatenates first and last name`() {
        val profile = UserProfile(
            id = 1,
            username = "emilys",
            email = "emily@test.com",
            firstName = "Emily",
            lastName = "Johnson",
            gender = "female",
            image = "",
            phone = ""
        )
        assertEquals("Emily Johnson", profile.fullName)
    }

    @Test
    fun `fullName with empty last name`() {
        val profile = UserProfile(
            id = 1,
            username = "john",
            email = "john@test.com",
            firstName = "John",
            lastName = "",
            gender = "male",
            image = "",
            phone = ""
        )
        assertEquals("John ", profile.fullName)
    }
}
