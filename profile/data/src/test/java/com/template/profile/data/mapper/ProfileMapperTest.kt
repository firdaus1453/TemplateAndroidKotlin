package com.template.profile.data.mapper

import com.template.profile.data.dto.UserProfileDto
import kotlin.test.Test
import kotlin.test.assertEquals

class ProfileMapperTest {

    @Test
    fun `UserProfileDto toUserProfile maps all fields correctly`() {
        val dto = UserProfileDto(
            id = 1,
            username = "emilys",
            email = "emily.johnson@x.dummyjson.com",
            firstName = "Emily",
            lastName = "Johnson",
            gender = "female",
            image = "https://dummyjson.com/icon/emilys/128",
            phone = "+81 965-431-3024"
        )

        val profile = dto.toUserProfile()

        assertEquals(1, profile.id)
        assertEquals("emilys", profile.username)
        assertEquals("emily.johnson@x.dummyjson.com", profile.email)
        assertEquals("Emily", profile.firstName)
        assertEquals("Johnson", profile.lastName)
        assertEquals("female", profile.gender)
        assertEquals("https://dummyjson.com/icon/emilys/128", profile.image)
        assertEquals("+81 965-431-3024", profile.phone)
    }

    @Test
    fun `UserProfileDto with default values maps correctly`() {
        val dto = UserProfileDto(
            id = 2,
            username = "test",
            email = "test@test.com",
            firstName = "Test",
            lastName = "User"
        )

        val profile = dto.toUserProfile()

        assertEquals(2, profile.id)
        assertEquals("", profile.gender)
        assertEquals("", profile.image)
        assertEquals("", profile.phone)
    }

    @Test
    fun `mapped profile fullName returns correct value`() {
        val dto = UserProfileDto(
            id = 1,
            username = "emilys",
            email = "emily@test.com",
            firstName = "Emily",
            lastName = "Johnson"
        )

        val profile = dto.toUserProfile()
        assertEquals("Emily Johnson", profile.fullName)
    }
}
