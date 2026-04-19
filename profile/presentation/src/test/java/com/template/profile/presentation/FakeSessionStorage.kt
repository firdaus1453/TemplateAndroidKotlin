package com.template.profile.presentation

import com.template.core.domain.AuthInfo
import com.template.core.domain.SessionStorage

class FakeSessionStorage : SessionStorage {
    var authInfo: AuthInfo? = AuthInfo(
        accessToken = "test-token",
        refreshToken = "test-refresh",
        userId = 1
    )
    var setCallCount = 0
        private set

    override suspend fun get(): AuthInfo? = authInfo

    override suspend fun set(info: AuthInfo?) {
        setCallCount++
        authInfo = info
    }
}
