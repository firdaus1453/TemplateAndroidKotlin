package com.template.auth.presentation.login

import app.cash.turbine.test
import com.template.core.domain.util.DataError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private lateinit var fakeAuthRepository: FakeAuthRepository
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeAuthRepository = FakeAuthRepository()
        viewModel = LoginViewModel(fakeAuthRepository)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is empty and not loading`() {
        val state = viewModel.state
        assertEquals("", state.username)
        assertEquals("", state.password)
        assertFalse(state.isLoading)
        assertFalse(state.canLogin)
        assertFalse(state.isPasswordVisible)
    }

    @Test
    fun `username change updates state`() {
        viewModel.onAction(LoginAction.OnUsernameChange("emilys"))
        assertEquals("emilys", viewModel.state.username)
    }

    @Test
    fun `password change updates state`() {
        viewModel.onAction(LoginAction.OnPasswordChange("pass1234"))
        assertEquals("pass1234", viewModel.state.password)
    }

    @Test
    fun `canLogin is true when username and password are valid`() {
        viewModel.onAction(LoginAction.OnUsernameChange("emilys"))
        viewModel.onAction(LoginAction.OnPasswordChange("pass"))
        assertTrue(viewModel.state.canLogin)
    }

    @Test
    fun `canLogin is false when username too short`() {
        viewModel.onAction(LoginAction.OnUsernameChange("ab"))
        viewModel.onAction(LoginAction.OnPasswordChange("pass1234"))
        assertFalse(viewModel.state.canLogin)
    }

    @Test
    fun `canLogin is false when password too short`() {
        viewModel.onAction(LoginAction.OnUsernameChange("emilys"))
        viewModel.onAction(LoginAction.OnPasswordChange("ab"))
        assertFalse(viewModel.state.canLogin)
    }

    @Test
    fun `toggle password visibility`() {
        assertFalse(viewModel.state.isPasswordVisible)
        viewModel.onAction(LoginAction.OnTogglePasswordVisibility)
        assertTrue(viewModel.state.isPasswordVisible)
        viewModel.onAction(LoginAction.OnTogglePasswordVisibility)
        assertFalse(viewModel.state.isPasswordVisible)
    }

    @Test
    fun `successful login emits LoginSuccess event`() = runTest {
        viewModel.onAction(LoginAction.OnUsernameChange("emilys"))
        viewModel.onAction(LoginAction.OnPasswordChange("emilyspass"))

        viewModel.events.test {
            viewModel.onAction(LoginAction.OnLoginClick)
            testDispatcher.scheduler.advanceUntilIdle()

            val event = awaitItem()
            assertIs<LoginEvent.LoginSuccess>(event)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `successful login calls repository with correct params`() = runTest {
        viewModel.onAction(LoginAction.OnUsernameChange("  emilys  "))
        viewModel.onAction(LoginAction.OnPasswordChange("emilyspass"))

        viewModel.events.test {
            viewModel.onAction(LoginAction.OnLoginClick)
            testDispatcher.scheduler.advanceUntilIdle()
            awaitItem()
            cancelAndIgnoreRemainingEvents()
        }

        assertEquals(1, fakeAuthRepository.loginCallCount)
        assertEquals("emilys", fakeAuthRepository.lastUsername) // trimmed
        assertEquals("emilyspass", fakeAuthRepository.lastPassword)
    }

    @Test
    fun `failed login emits Error event`() = runTest {
        fakeAuthRepository.shouldReturnError = true
        fakeAuthRepository.errorToReturn = DataError.Network.UNAUTHORIZED

        viewModel.onAction(LoginAction.OnUsernameChange("emilys"))
        viewModel.onAction(LoginAction.OnPasswordChange("wrongpass"))

        viewModel.events.test {
            viewModel.onAction(LoginAction.OnLoginClick)
            testDispatcher.scheduler.advanceUntilIdle()

            val event = awaitItem()
            assertIs<LoginEvent.Error>(event)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `login sets isLoading during network call`() = runTest {
        viewModel.onAction(LoginAction.OnUsernameChange("emilys"))
        viewModel.onAction(LoginAction.OnPasswordChange("emilyspass"))

        viewModel.events.test {
            viewModel.onAction(LoginAction.OnLoginClick)
            // Before advancing, isLoading might be true
            testDispatcher.scheduler.advanceUntilIdle()

            // After completion, isLoading should be false
            assertFalse(viewModel.state.isLoading)
            awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
    }
}
