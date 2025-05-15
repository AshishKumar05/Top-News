package com.thetopheadlines.topnews.domain.repository

import com.thetopheadlines.topnews.domain.model.NewsResponse
import com.thetopheadlines.topnews.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface NewsRepo {

    suspend fun getToNewsHeadlines(): Flow<Resource<NewsResponse>>

    suspend fun getToNewsHeadlinesAsync(): Resource<NewsResponse>

}