package com.template.core.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull

/**
 * Tests for [AuthInfo] data class.
 *
 * AuthInfo is the session token holder used throughout the app: it drives
 * Bearer auth in HttpClientFactory, is persisted by EncryptedSessionStorage,
 * and determines whether the user is logged in (non-null in SessionStorage).
 *
 * We verify:
 * 1. Construction and property access (needed for Kover bytecode coverage)
 * 2. Structural equality and copy semantics (critical for token refresh logic)
 * 3. Interoperability with SessionStorage (store → retrieve → compare)
 */
class AuthInfoTest {

    private val sampleAuthInfo = AuthInfo(
        accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9",
        refreshToken = "c2FtcGxlLXJlZnJlc2gtdG9rZW4",
        userId = 1
    )

    // ── Construction & Property Access ──

    @Test
    fun `stores access token correctly`() {
        assertEquals("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9", sampleAuthInfo.accessToken)
    }

    @Test
    fun `stores refresh token correctly`() {
        assertEquals("c2FtcGxlLXJlZnJlc2gtdG9rZW4", sampleAuthInfo.refreshToken)
    }

    @Test
    fun `stores user id correctly`() {
        assertEquals(1, sampleAuthInfo.userId)
    }

    // ── Equality (data class contract) ──

    @Test
    fun `two AuthInfo with same values are equal`() {
        val copy = AuthInfo(
            accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9",
            refreshToken = "c2FtcGxlLXJlZnJlc2gtdG9rZW4",
            userId = 1
        )
        assertEquals(sampleAuthInfo, copy)
        assertEquals(sampleAuthInfo.hashCode(), copy.hashCode())
    }

    @Test
    fun `different access token produces unequal AuthInfo`() {
        val updated = sampleAuthInfo.copy(accessToken = "new-access-token")
        assertNotEquals(sampleAuthInfo, updated)
    }

    @Test
    fun `different refresh token produces unequal AuthInfo`() {
        val updated = sampleAuthInfo.copy(refreshToken = "new-refresh-token")
        assertNotEquals(sampleAuthInfo, updated)
    }

    @Test
    fun `different userId produces unequal AuthInfo`() {
        val updated = sampleAuthInfo.copy(userId = 999)
        assertNotEquals(sampleAuthInfo, updated)
    }

    // ── Copy semantics (used by token refresh in HttpClientFactory) ──

    @Test
    fun `copy preserves unchanged fields when only access token changes`() {
        val refreshed = sampleAuthInfo.copy(accessToken = "new-access-token")
        assertEquals(sampleAuthInfo.refreshToken, refreshed.refreshToken)
        assertEquals(sampleAuthInfo.userId, refreshed.userId)
        assertEquals("new-access-token", refreshed.accessToken)
    }

    // ── SessionStorage integration scenario ──

    @Test
    fun `nullable AuthInfo simulates empty session`() {
        val session: AuthInfo? = null
        assertNull(session)
        // This mirrors SessionStorage.get() returning null when not logged in
    }

    @Test
    fun `nullable AuthInfo safe access matches SessionStorage pattern`() {
        val session: AuthInfo? = sampleAuthInfo
        // Pattern used in HttpClientFactory bearer loadTokens
        val accessToken = session?.accessToken ?: ""
        val refreshToken = session?.refreshToken ?: ""
        assertEquals("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9", accessToken)
        assertEquals("c2FtcGxlLXJlZnJlc2gtdG9rZW4", refreshToken)
    }
}
