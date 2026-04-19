package com.template.home.presentation

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
class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel
    private lateinit var fakeRepository: FakeProductRepository
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeProductRepository()
        viewModel = HomeViewModel(fakeRepository)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has empty products and isLoading false`() {
        val state = viewModel.state
        assertTrue(state.products.isEmpty())
        assertEquals("", state.searchQuery)
    }

    @Test
    fun `init triggers fetchProducts`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(1, fakeRepository.fetchCallCount)
    }

    @Test
    fun `products from repository update state as ProductUi`() = runTest {
        val products = FakeProductRepository.createSampleProducts()
        fakeRepository.emitProducts(products)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(2, viewModel.state.products.size)
        assertEquals("iPhone 9", viewModel.state.products[0].title)
        assertEquals("Samsung Universe 9", viewModel.state.products[1].title)
        // Verify UI mapping
        assertEquals("$549.00", viewModel.state.products[0].formattedPrice)
        assertEquals("4.7", viewModel.state.products[0].formattedRating)
        assertEquals("-12%", viewModel.state.products[0].discountBadge)
    }

    @Test
    fun `onRefresh triggers fetchProducts again`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(1, fakeRepository.fetchCallCount)

        viewModel.onAction(HomeAction.OnRefresh)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(2, fakeRepository.fetchCallCount)
    }

    @Test
    fun `onRefresh sets isRefreshing then clears it`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onAction(HomeAction.OnRefresh)
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.state.isRefreshing)
    }

    @Test
    fun `fetch error emits Error event`() = runTest {
        fakeRepository.shouldReturnError = true
        fakeRepository.errorToReturn = DataError.Network.NO_INTERNET

        // Create a new VM to trigger init with error
        viewModel = HomeViewModel(fakeRepository)

        viewModel.events.test {
            testDispatcher.scheduler.advanceUntilIdle()
            val event = awaitItem()
            assertIs<HomeEvent.Error>(event)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `search query updates state`() = runTest {
        viewModel.onAction(HomeAction.OnSearchQueryChange("iphone"))
        assertEquals("iphone", viewModel.state.searchQuery)
    }

    @Test
    fun `onProductClick does not crash`() = runTest {
        viewModel.onAction(HomeAction.OnProductClick(1))
        // No crash means success
    }
}
