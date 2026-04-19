package com.template.auth.presentation.login

import com.template.auth.domain.AuthRepository
import com.template.core.domain.util.DataError
import com.template.core.domain.util.EmptyResult
import com.template.core.domain.util.Result

class FakeAuthRepository : AuthRepository {
    var shouldReturnError = false
    var errorToReturn: DataError.Network = DataError.Network.SERVER_ERROR
    var loginCallCount = 0
        private set
    var lastUsername: String? = null
        private set
    var lastPassword: String? = null
        private set

    override suspend fun login(username: String, password: String): EmptyResult<DataError.Network> {
        loginCallCount++
        lastUsername = username
        lastPassword = password
        return if (shouldReturnError) {
            Result.Error(errorToReturn)
        } else {
            Result.Success(Unit)
        }
    }
}
