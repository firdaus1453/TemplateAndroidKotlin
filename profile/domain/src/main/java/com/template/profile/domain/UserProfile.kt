package com.template.profile.domain

data class UserProfile(
    val id: Int,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val gender: String,
    val image: String,
    val phone: String
) {
    val fullName: String
        get() = "$firstName $lastName"
}
