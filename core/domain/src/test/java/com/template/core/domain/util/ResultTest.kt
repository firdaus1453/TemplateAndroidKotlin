package com.template.core.domain.util

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class ResultTest {

    @Test
    fun `map transforms Success data`() {
        val result: Result<Int, DataError.Network> = Result.Success(5)
        val mapped = result.map { it * 2 }
        assertIs<Result.Success<Int>>(mapped)
        assertEquals(10, mapped.data)
    }

    @Test
    fun `map passes through Error unchanged`() {
        val result: Result<Int, DataError.Network> = Result.Error(DataError.Network.NO_INTERNET)
        val mapped = result.map { it * 2 }
        assertIs<Result.Error<DataError.Network>>(mapped)
        assertEquals(DataError.Network.NO_INTERNET, mapped.error)
    }

    @Test
    fun `onSuccess invokes action for Success`() {
        var captured: Int? = null
        val result: Result<Int, DataError.Network> = Result.Success(42)
        result.onSuccess { captured = it }
        assertEquals(42, captured)
    }

    @Test
    fun `onSuccess does not invoke action for Error`() {
        var captured: Int? = null
        val result: Result<Int, DataError.Network> = Result.Error(DataError.Network.UNKNOWN)
        result.onSuccess { captured = it }
        assertNull(captured)
    }

    @Test
    fun `onFailure invokes action for Error`() {
        var captured: DataError.Network? = null
        val result: Result<Int, DataError.Network> = Result.Error(DataError.Network.SERVER_ERROR)
        result.onFailure { captured = it }
        assertEquals(DataError.Network.SERVER_ERROR, captured)
    }

    @Test
    fun `onFailure does not invoke action for Success`() {
        var captured: DataError.Network? = null
        val result: Result<Int, DataError.Network> = Result.Success(1)
        result.onFailure { captured = it }
        assertNull(captured)
    }

    @Test
    fun `asEmptyDataResult converts Success to Success Unit`() {
        val result: Result<Int, DataError.Network> = Result.Success(42)
        val empty = result.asEmptyDataResult()
        assertIs<Result.Success<Unit>>(empty)
    }

    @Test
    fun `asEmptyDataResult converts Error to Error`() {
        val result: Result<Int, DataError.Network> = Result.Error(DataError.Network.UNAUTHORIZED)
        val empty = result.asEmptyDataResult()
        assertIs<Result.Error<DataError.Network>>(empty)
        assertEquals(DataError.Network.UNAUTHORIZED, empty.error)
    }
}
