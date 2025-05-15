package com.thetopheadlines.topnews.data.repository

import com.thetopheadlines.topnews.data.utils.Utils
import com.thetopheadlines.topnews.data.remote.NewsApiService
import com.thetopheadlines.topnews.domain.model.NewsResponse
import com.thetopheadlines.topnews.domain.repository.NewsRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import com.thetopheadlines.topnews.domain.model.Resource
import javax.inject.Inject

class NewsRepoImpl @Inject constructor(private val newsApiService: NewsApiService) : NewsRepo {
    override suspend fun getToNewsHeadlines(): Flow<Resource<NewsResponse>> = flow {
        emit(Resource.Loading)
        val response = newsApiService.getTopHeadlinesNews("us", Utils.API_KEY)
        if (response.isSuccessful) {
            val responseData = response.body()
            if(responseData!=null){
                emit(Resource.Success(responseData))
            }else{
                emit(Resource.Success(NewsResponse("error",0, arrayListOf())))
            }

        } else {
            emit(Resource.Error(response.message().orEmpty()))
        }
    }.catch { e ->
        emit(Resource.Error("Exception: ${e.localizedMessage}"))
    }
}
