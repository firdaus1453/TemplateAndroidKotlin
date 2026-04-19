package com.template.core.domain

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * Tests for the [SessionStorage] interface contract.
 *
 * SessionStorage is the single source of session state for the entire app:
 * - HttpClientFactory reads it for Bearer tokens
 * - LoginViewModel writes to it on successful login
 * - ProfileViewModel sets it to null on logout
 *
 * We use an in-memory fake to verify the contract:
 * 1. Default state should be null (no session)
 * 2. Storing auth info should make it retrievable
 * 3. Setting null should clear the session (logout)
 * 4. Overwriting tokens should reflect the latest state (token refresh)
 */
class SessionStorageContractTest {

    /**
     * In-memory implementation of [SessionStorage] for contract testing.
     * This is distinct from FakeSessionStorage in presentation tests because
     * it starts with no session (null) to test the clean-slate scenario.
     */
    private class InMemorySessionStorage : SessionStorage {
        private var stored: AuthInfo? = null

        override suspend fun get(): AuthInfo? = stored
        override suspend fun set(info: AuthInfo?) {
            stored = info
        }
    }

    private val storage: SessionStorage = InMemorySessionStorage()

    @Test
    fun `initially returns null when no session stored`() = runTest {
        assertNull(storage.get())
    }

    @Test
    fun `stores and retrieves AuthInfo`() = runTest {
        val authInfo = AuthInfo(
            accessToken = "access-token-abc",
            refreshToken = "refresh-token-xyz",
            userId = 42
        )
        storage.set(authInfo)
        assertEquals(authInfo, storage.get())
    }

    @Test
    fun `setting null clears the session (logout)`() = runTest {
        val authInfo = AuthInfo(
            accessToken = "access",
            refreshToken = "refresh",
            userId = 1
        )
        storage.set(authInfo)
        assertEquals(authInfo, storage.get())

        storage.set(null)
        assertNull(storage.get())
    }

    @Test
    fun `overwriting session updates to latest auth info (token refresh)`() = runTest {
        val original = AuthInfo(
            accessToken = "old-access",
            refreshToken = "refresh-token",
            userId = 1
        )
        storage.set(original)

        val refreshed = original.copy(accessToken = "new-access-after-refresh")
        storage.set(refreshed)

        val retrieved = storage.get()
        assertEquals("new-access-after-refresh", retrieved?.accessToken)
        // Refresh token and userId remain unchanged
        assertEquals("refresh-token", retrieved?.refreshToken)
        assertEquals(1, retrieved?.userId)
    }

    @Test
    fun `multiple set-get cycles maintain consistency`() = runTest {
        // Login
        val loginInfo = AuthInfo("tok1", "ref1", 10)
        storage.set(loginInfo)
        assertEquals(loginInfo, storage.get())

        // Token refresh
        val refreshedInfo = loginInfo.copy(accessToken = "tok2")
        storage.set(refreshedInfo)
        assertEquals(refreshedInfo, storage.get())

        // Logout
        storage.set(null)
        assertNull(storage.get())

        // Re-login
        val reLoginInfo = AuthInfo("tok3", "ref3", 20)
        storage.set(reLoginInfo)
        assertEquals(reLoginInfo, storage.get())
    }
}
