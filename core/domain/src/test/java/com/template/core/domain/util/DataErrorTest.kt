package com.template.core.domain.util

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * Tests for [DataError] sealed interface and its enum sub-types.
 *
 * DataError is the core error taxonomy used across the entire application:
 * - [DataError.Network] is returned by all Ktor remote data sources
 * - [DataError.Local] is returned by Room local data sources
 * - ViewModels map DataError → UiText via toUiText() for user-facing messages
 * - WorkManager workers map DataError → WorkerResult to decide retry vs failure
 *
 * We verify that:
 * 1. Each enum has the expected entries (guards against accidental removal)
 * 2. Enum entries implement the DataError and Error interfaces correctly
 * 3. Enum entries can be used in exhaustive when-expressions (mapper compatibility)
 * 4. valueOf() round-trips correctly (used in persistence/serialization scenarios)
 */
class DataErrorTest {

    // ── DataError.Network ──

    @Test
    fun `Network enum has all 13 expected error types`() {
        val entries = DataError.Network.entries
        assertEquals(13, entries.size)
    }

    @Test
    fun `Network errors can be matched exhaustively in when-expression`() {
        // This simulates the responseToResult() mapping in HttpClientExt
        DataError.Network.entries.forEach { error ->
            val label = when (error) {
                DataError.Network.BAD_REQUEST -> "400"
                DataError.Network.REQUEST_TIMEOUT -> "408"
                DataError.Network.UNAUTHORIZED -> "401"
                DataError.Network.FORBIDDEN -> "403"
                DataError.Network.NOT_FOUND -> "404"
                DataError.Network.CONFLICT -> "409"
                DataError.Network.TOO_MANY_REQUESTS -> "429"
                DataError.Network.NO_INTERNET -> "no-internet"
                DataError.Network.PAYLOAD_TOO_LARGE -> "413"
                DataError.Network.SERVER_ERROR -> "500"
                DataError.Network.SERVICE_UNAVAILABLE -> "503"
                DataError.Network.SERIALIZATION -> "serialization"
                DataError.Network.UNKNOWN -> "unknown"
            }
            assertTrue(label.isNotEmpty(), "Every network error should map to a label")
        }
    }

    @Test
    fun `Network error implements Error interface`() {
        val error: Error = DataError.Network.SERVER_ERROR
        assertIs<Error>(error)
    }

    @Test
    fun `Network error implements DataError interface`() {
        val error: DataError = DataError.Network.NO_INTERNET
        assertIs<DataError>(error)
    }

    @Test
    fun `Network valueOf round-trips all entries`() {
        DataError.Network.entries.forEach { entry ->
            assertEquals(entry, DataError.Network.valueOf(entry.name))
        }
    }

    // ── DataError.Local ──

    @Test
    fun `Local enum has all 3 expected error types`() {
        val entries = DataError.Local.entries
        assertEquals(3, entries.size)
    }

    @Test
    fun `Local errors can be matched exhaustively in when-expression`() {
        // This simulates the toWorkerResult() mapping in data layer
        DataError.Local.entries.forEach { error ->
            val isRetryable = when (error) {
                DataError.Local.DISK_FULL -> false
                DataError.Local.NOT_FOUND -> false
                DataError.Local.UNKNOWN -> true
            }
            // DISK_FULL and NOT_FOUND are terminal, UNKNOWN can be retried
            if (error == DataError.Local.UNKNOWN) {
                assertTrue(isRetryable)
            }
        }
    }

    @Test
    fun `Local error implements Error interface`() {
        val error: Error = DataError.Local.DISK_FULL
        assertIs<Error>(error)
    }

    @Test
    fun `Local error implements DataError interface`() {
        val error: DataError = DataError.Local.NOT_FOUND
        assertIs<DataError>(error)
    }

    @Test
    fun `Local valueOf round-trips all entries`() {
        DataError.Local.entries.forEach { entry ->
            assertEquals(entry, DataError.Local.valueOf(entry.name))
        }
    }

    // ── Polymorphism (DataError as union type) ──

    @Test
    fun `both Network and Local can be stored as DataError`() {
        val errors: List<DataError> = listOf(
            DataError.Network.SERVER_ERROR,
            DataError.Local.DISK_FULL,
            DataError.Network.NO_INTERNET,
            DataError.Local.UNKNOWN
        )
        assertEquals(4, errors.size)
        // This pattern is used by Repository which returns DataError (union of both)
        assertIs<DataError.Network>(errors[0])
        assertIs<DataError.Local>(errors[1])
    }

    @Test
    fun `DataError can be used with Result type`() {
        val networkError: Result<String, DataError.Network> =
            Result.Error(DataError.Network.UNAUTHORIZED)
        assertIs<Result.Error<DataError.Network>>(networkError)
        assertEquals(DataError.Network.UNAUTHORIZED, networkError.error)

        val localError: Result<String, DataError.Local> =
            Result.Error(DataError.Local.DISK_FULL)
        assertIs<Result.Error<DataError.Local>>(localError)
        assertEquals(DataError.Local.DISK_FULL, localError.error)
    }
}
