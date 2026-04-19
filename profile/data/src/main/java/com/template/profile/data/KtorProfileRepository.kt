package com.template.profile.data

import com.template.core.data.networking.get
import com.template.core.domain.util.DataError
import com.template.core.domain.util.Result
import com.template.core.domain.util.map
import com.template.profile.data.dto.UserProfileDto
import com.template.profile.data.mapper.toUserProfile
import com.template.profile.domain.ProfileRepository
import com.template.profile.domain.UserProfile
import io.ktor.client.HttpClient

class KtorProfileRepository(
    private val httpClient: HttpClient
) : ProfileRepository {

    override suspend fun getCurrentUser(): Result<UserProfile, DataError.Network> {
        return httpClient.get<UserProfileDto>(route = "/auth/me")
            .map { it.toUserProfile() }
    }
}
