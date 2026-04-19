package com.template.profile.domain

import com.template.core.domain.util.DataError
import com.template.core.domain.util.Result

interface ProfileRepository {
    suspend fun getCurrentUser(): Result<UserProfile, DataError.Network>
}
