package com.template.auth.domain

import com.template.core.domain.util.DataError
import com.template.core.domain.util.EmptyResult

interface AuthRepository {
    suspend fun login(username: String, password: String): EmptyResult<DataError.Network>
}
