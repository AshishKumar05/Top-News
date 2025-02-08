package com.thetopheadlines.topnews.data.repository

import android.util.Log
import com.thetopheadlines.topnews.data.utils.Utils
import com.thetopheadlines.topnews.data.remote.NewsApiService
import com.thetopheadlines.topnews.domain.model.NewsItem
import com.thetopheadlines.topnews.domain.repository.NewsRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import com.thetopheadlines.topnews.domain.model.Resource
import javax.inject.Inject

class NewsRepoImpl @Inject constructor(private val newsApiService: NewsApiService) : NewsRepo {
    override suspend fun getToNewsHeadlines(): Flow<Resource<List<NewsItem>>> = flow {
        emit(Resource.Loading())

        val response = newsApiService.getTopHeadlinesNews("us", Utils.API_KEY)
        Log.d("NewsRepoImpl", "response: $response")

        if (response.isSuccessful) {
            val newsList = response.body()?.articles ?: emptyList()
            emit(Resource.Error(response.message().orEmpty()))
            //emit(Resource.Success(newsList))
        } else {
            emit(Resource.Error(response.message().orEmpty()))
        }
    }.catch { e ->
        emit(Resource.Error("Exception: ${e.localizedMessage}"))
    }
}
