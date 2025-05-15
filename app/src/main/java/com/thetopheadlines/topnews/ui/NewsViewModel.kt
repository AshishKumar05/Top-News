package com.thetopheadlines.topnews.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thetopheadlines.topnews.domain.model.NewsResponse
import com.thetopheadlines.topnews.domain.model.Resource
import com.thetopheadlines.topnews.domain.repository.NewsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NewsViewModel @Inject constructor(private val newsRepo: NewsRepo) : ViewModel() {
    private val _newsResponse = MutableStateFlow<Resource<NewsResponse>>(Resource.Loading)
    val newsResponse: StateFlow<Resource<NewsResponse>> = _newsResponse.asStateFlow()

    init {
        loadNews()
    }

    fun loadNews() {
        viewModelScope.launch(Dispatchers.IO) {
            newsRepo.getToNewsHeadlines()
                .onStart { _newsResponse.value = Resource.Loading }
                .catch {
                    _newsResponse.value = Resource.Error(it.message ?: "Something went wrong")
                }
                .collect { news ->
                    _newsResponse.value = news
                }
        }
    }

}
