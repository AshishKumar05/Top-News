package com.thetopheadlines.topnews.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thetopheadlines.topnews.domain.model.NewsItem
import com.thetopheadlines.topnews.domain.model.Resource
import com.thetopheadlines.topnews.domain.repository.NewsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NewsViewModel @Inject constructor(private val newsRepo: NewsRepo) : ViewModel() {
    private val _newsList = MutableStateFlow<Resource<List<NewsItem>>>(Resource.Loading())
    val newsList: StateFlow<Resource<List<NewsItem>>> = _newsList.asStateFlow()

    init {
        loadNews()
    }

    fun loadNews() {
        viewModelScope.launch {
            newsRepo.getToNewsHeadlines()
                .onStart { _newsList.value = Resource.Loading() }
                .catch {
                    _newsList.value = Resource.Error(it.message ?: "Something went wrong")
                }
                .collect { news ->
                    _newsList.value = news
                }
        }
    }

}
