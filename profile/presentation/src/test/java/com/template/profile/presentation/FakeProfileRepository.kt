package com.template.profile.presentation

import com.template.core.domain.util.DataError
import com.template.core.domain.util.Result
import com.template.profile.domain.ProfileRepository
import com.template.profile.domain.UserProfile

class FakeProfileRepository : ProfileRepository {
    var shouldReturnError = false
    var errorToReturn: DataError.Network = DataError.Network.SERVER_ERROR

    val sampleProfile = UserProfile(
        id = 1,
        username = "emilys",
        email = "emily.johnson@x.dummyjson.com",
        firstName = "Emily",
        lastName = "Johnson",
        gender = "female",
        image = "https://dummyjson.com/icon/emilys/128",
        phone = "+81 965-431-3024"
    )

    override suspend fun getCurrentUser(): Result<UserProfile, DataError.Network> {
        return if (shouldReturnError) {
            Result.Error(errorToReturn)
        } else {
            Result.Success(sampleProfile)
        }
    }
}
