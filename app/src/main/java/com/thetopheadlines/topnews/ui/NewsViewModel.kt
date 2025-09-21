package com.thetopheadlines.topnews.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thetopheadlines.topnews.domain.model.NewsResponse
import com.thetopheadlines.topnews.domain.model.NewsUIState
import com.thetopheadlines.topnews.domain.model.Resource
import com.thetopheadlines.topnews.domain.repository.NewsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NewsViewModel @Inject constructor(private val newsRepo: NewsRepo) : ViewModel() {
    private val _newsUIState = MutableStateFlow<NewsUIState>(NewsUIState())
    val newsUIState: StateFlow<NewsUIState> = _newsUIState.asStateFlow()

    init {
       // loadNews()
        loadNewsAsync()
    }

    fun loadNews() {
        viewModelScope.launch(Dispatchers.IO) {
            newsRepo.getToNewsHeadlines()
                .onStart {  _newsUIState.update { it.copy(news = Resource.Loading) } }
                .catch {
                    _newsUIState.update { it.copy(news = Resource.Error("Something went wrong")) }
                }
                .collect { news ->
                    _newsUIState.update { it.copy(news = news) }
                }
        }
    }

    private fun loadNewsAsync() {
        viewModelScope.launch(Dispatchers.IO) {
            _newsUIState.update { it.copy(news = Resource.Loading) }

            val deferred1 = async { newsRepo.getToNewsHeadlinesAsync() }
            val deferred2 = async { newsRepo.getToNewsHeadlinesAsync() }

            try {
                val response1 = deferred1.await()
                val response2 = deferred2.await()
                if (response1 is Resource.Success && response2 is Resource.Success) {
                    _newsUIState.update { it.copy(news = Resource.Success(getCombinedNewsResponse(response1.data, response2.data))) }

                } else if (response1 is Resource.Error && response2 is Resource.Success) {
                    _newsUIState.update { it.copy(news = Resource.Success(response2.data)) }
                } else if (response1 is Resource.Success && response2 is Resource.Error) {
                    _newsUIState.update { it.copy(news = Resource.Success(response1.data)) }
                } else {
                    _newsUIState.update { it.copy(news = Resource.Error("Something went wrong")) }
                }
            } catch (e: Exception) {
                _newsUIState.update { it.copy(news = Resource.Error("Something went wrong")) }
            }
        }
    }


    private fun getCombinedNewsResponse(data1: NewsResponse, data2: NewsResponse): NewsResponse {
        val combinedArticles = data1.articles + data2.articles
        return NewsResponse(data1.status, data1.totalResults + data2.totalResults, combinedArticles)
    }


}
