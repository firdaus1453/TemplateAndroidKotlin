package com.template.profile.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserProfileDto(
    val id: Int,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val gender: String = "",
    val image: String = "",
    val phone: String = ""
)
