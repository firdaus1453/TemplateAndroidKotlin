package com.template.core.data.auth

import com.template.core.domain.AuthInfo

fun AuthInfo.toSerializable() = AuthInfoSerializable(
    accessToken = accessToken,
    refreshToken = refreshToken,
    userId = userId
)

fun AuthInfoSerializable.toAuthInfo() = AuthInfo(
    accessToken = accessToken,
    refreshToken = refreshToken,
    userId = userId
)
