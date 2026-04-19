package com.template.home.presentation

sealed interface HomeAction {
    data class OnSearchQueryChange(val query: String) : HomeAction
    data object OnRefresh : HomeAction
    data class OnProductClick(val productId: Int) : HomeAction
}
