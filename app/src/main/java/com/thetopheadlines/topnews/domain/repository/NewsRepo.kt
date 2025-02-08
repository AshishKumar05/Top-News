package com.thetopheadlines.topnews.domain.repository

import com.thetopheadlines.topnews.domain.model.NewsItem
import com.thetopheadlines.topnews.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface NewsRepo {

    suspend fun getToNewsHeadlines(): Flow<Resource<List<NewsItem>>>

}