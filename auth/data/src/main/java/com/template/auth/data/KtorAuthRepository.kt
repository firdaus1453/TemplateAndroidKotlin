package com.template.auth.data

import com.template.auth.domain.AuthRepository
import com.template.core.data.networking.post
import com.template.core.domain.AuthInfo
import com.template.core.domain.SessionStorage
import com.template.core.domain.util.DataError
import com.template.core.domain.util.EmptyResult
import com.template.core.domain.util.Result
import com.template.core.domain.util.asEmptyDataResult
import io.ktor.client.HttpClient

class KtorAuthRepository(
    private val httpClient: HttpClient,
    private val sessionStorage: SessionStorage
) : AuthRepository {

    override suspend fun login(
        username: String,
        password: String
    ): EmptyResult<DataError.Network> {
        val result = httpClient.post<LoginRequest, LoginResponse>(
            route = "/auth/login",
            body = LoginRequest(
                username = username,
                password = password
            )
        )
        if (result is Result.Success) {
            sessionStorage.set(
                AuthInfo(
                    accessToken = result.data.accessToken,
                    refreshToken = result.data.refreshToken,
                    userId = result.data.id
                )
            )
        }
        return result.asEmptyDataResult()
    }
}
