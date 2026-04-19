package com.template.core.data.networking

import kotlinx.serialization.Serializable

@Serializable
data class AccessTokenRequest(
    val refreshToken: String,
    val expiresInMins: Int = 60
)

@Serializable
data class AccessTokenResponse(
    val accessToken: String,
    val refreshToken: String
)
