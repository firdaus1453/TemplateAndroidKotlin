package com.template.profile.data.mapper

import com.template.profile.data.dto.UserProfileDto
import com.template.profile.domain.UserProfile

fun UserProfileDto.toUserProfile() = UserProfile(
    id = id,
    username = username,
    email = email,
    firstName = firstName,
    lastName = lastName,
    gender = gender,
    image = image,
    phone = phone
)
