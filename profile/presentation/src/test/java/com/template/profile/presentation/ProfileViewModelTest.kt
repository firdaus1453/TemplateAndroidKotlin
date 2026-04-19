package com.template.profile.presentation

import app.cash.turbine.test
import com.template.core.domain.util.DataError
import com.template.profile.domain.AppLanguage
import com.template.profile.domain.ThemeMode
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
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    private lateinit var viewModel: ProfileViewModel
    private lateinit var fakeProfileRepository: FakeProfileRepository
    private lateinit var fakeSessionStorage: FakeSessionStorage
    private lateinit var fakeAppPreferences: FakeAppPreferences
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeProfileRepository = FakeProfileRepository()
        fakeSessionStorage = FakeSessionStorage()
        fakeAppPreferences = FakeAppPreferences()
        viewModel = ProfileViewModel(
            profileRepository = fakeProfileRepository,
            sessionStorage = fakeSessionStorage,
            appPreferences = fakeAppPreferences
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has default values`() {
        val state = viewModel.state
        assertNull(state.userProfile)
        assertEquals(ThemeMode.SYSTEM, state.themeMode)
        assertEquals(AppLanguage.ENGLISH, state.language)
        assertFalse(state.showThemeDialog)
        assertFalse(state.showLanguageDialog)
    }

    @Test
    fun `init loads profile successfully`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state
        assertNotNull(state.userProfile)
        assertEquals("emilys", state.userProfile!!.username)
        assertEquals("Emily Johnson", state.userProfile!!.fullName)
        assertFalse(state.isLoading)
    }

    @Test
    fun `failed profile load emits error event`() = runTest {
        fakeProfileRepository.shouldReturnError = true
        fakeProfileRepository.errorToReturn = DataError.Network.UNAUTHORIZED

        viewModel = ProfileViewModel(fakeProfileRepository, fakeSessionStorage, fakeAppPreferences)

        viewModel.events.test {
            testDispatcher.scheduler.advanceUntilIdle()
            val event = awaitItem()
            assertIs<ProfileEvent.Error>(event)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onRefresh reloads profile`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()
        assertNotNull(viewModel.state.userProfile)

        viewModel.onAction(ProfileAction.OnRefresh)
        testDispatcher.scheduler.advanceUntilIdle()

        assertNotNull(viewModel.state.userProfile)
    }

    @Test
    fun `theme mode change updates state`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onAction(ProfileAction.OnThemeModeChange(ThemeMode.DARK))
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(ThemeMode.DARK, viewModel.state.themeMode)
        assertFalse(viewModel.state.showThemeDialog)
    }

    @Test
    fun `language change updates state`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onAction(ProfileAction.OnLanguageChange(AppLanguage.JAPANESE))
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(AppLanguage.JAPANESE, viewModel.state.language)
        assertFalse(viewModel.state.showLanguageDialog)
    }

    @Test
    fun `show theme dialog updates state`() {
        viewModel.onAction(ProfileAction.OnShowThemeDialog)
        assertTrue(viewModel.state.showThemeDialog)
    }

    @Test
    fun `dismiss theme dialog updates state`() {
        viewModel.onAction(ProfileAction.OnShowThemeDialog)
        viewModel.onAction(ProfileAction.OnDismissThemeDialog)
        assertFalse(viewModel.state.showThemeDialog)
    }

    @Test
    fun `show language dialog updates state`() {
        viewModel.onAction(ProfileAction.OnShowLanguageDialog)
        assertTrue(viewModel.state.showLanguageDialog)
    }

    @Test
    fun `dismiss language dialog updates state`() {
        viewModel.onAction(ProfileAction.OnShowLanguageDialog)
        viewModel.onAction(ProfileAction.OnDismissLanguageDialog)
        assertFalse(viewModel.state.showLanguageDialog)
    }

    @Test
    fun `logout clears session and emits LogoutSuccess`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.events.test {
            viewModel.onAction(ProfileAction.OnLogoutClick)
            testDispatcher.scheduler.advanceUntilIdle()

            val event = awaitItem()
            assertIs<ProfileEvent.LogoutSuccess>(event)
            assertNull(fakeSessionStorage.authInfo)
            assertEquals(1, fakeSessionStorage.setCallCount)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `theme mode cycles through all options`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onAction(ProfileAction.OnThemeModeChange(ThemeMode.LIGHT))
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(ThemeMode.LIGHT, viewModel.state.themeMode)

        viewModel.onAction(ProfileAction.OnThemeModeChange(ThemeMode.DARK))
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(ThemeMode.DARK, viewModel.state.themeMode)

        viewModel.onAction(ProfileAction.OnThemeModeChange(ThemeMode.SYSTEM))
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(ThemeMode.SYSTEM, viewModel.state.themeMode)
    }

    @Test
    fun `language cycles through all options`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onAction(ProfileAction.OnLanguageChange(AppLanguage.INDONESIAN))
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(AppLanguage.INDONESIAN, viewModel.state.language)

        viewModel.onAction(ProfileAction.OnLanguageChange(AppLanguage.JAPANESE))
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(AppLanguage.JAPANESE, viewModel.state.language)

        viewModel.onAction(ProfileAction.OnLanguageChange(AppLanguage.ENGLISH))
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(AppLanguage.ENGLISH, viewModel.state.language)
    }
}
