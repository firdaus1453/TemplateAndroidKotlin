package com.template.home.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.core.domain.util.Result
import com.template.core.presentation.ui.toUiText
import com.template.home.domain.ProductRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    var state by mutableStateOf(HomeState())
        private set

    private val eventChannel = Channel<HomeEvent>()
    val events = eventChannel.receiveAsFlow()

    private var searchJob: Job? = null

    init {
        observeProducts()
        fetchProducts()
    }

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.OnSearchQueryChange -> {
                state = state.copy(searchQuery = action.query)
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500L) // debounce
                    observeProducts(action.query)
                }
            }
            HomeAction.OnRefresh -> fetchProducts()
            is HomeAction.OnProductClick -> {
                // Handle product detail navigation if needed
            }
        }
    }

    private fun observeProducts(query: String = "") {
        val flow = if (query.isBlank()) {
            productRepository.getProducts()
        } else {
            productRepository.searchProducts(query)
        }
        flow.onEach { products ->
            state = state.copy(
                products = products.map { it.toProductUi() },
                isLoading = false,
                isRefreshing = false
            )
        }.launchIn(viewModelScope)
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            state = state.copy(isRefreshing = true)
            val result = productRepository.fetchProducts()
            state = state.copy(isRefreshing = false)

            if (result is Result.Error) {
                eventChannel.send(HomeEvent.Error(result.error.toUiText()))
            }
        }
    }
}
