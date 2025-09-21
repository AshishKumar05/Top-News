package com.thetopheadlines.topnews.domain.model

data class NewsUIState (
    val isLoading: Boolean = false,
    val news: Resource<NewsResponse> = Resource.Loading,
    val error: String = ""
)