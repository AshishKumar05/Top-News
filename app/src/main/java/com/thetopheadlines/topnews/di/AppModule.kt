package com.thetopheadlines.topnews.di

import com.thetopheadlines.topnews.data.utils.Utils
import com.thetopheadlines.topnews.data.remote.NewsApiService
import com.thetopheadlines.topnews.data.repository.NewsRepoImpl
import com.thetopheadlines.topnews.domain.repository.NewsRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.CertificatePinner
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {

        val httpLoggingInterceptor =
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

        val headerInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer YOUR_TOKEN")
                .addHeader("Accept", "application/json")
                .build()
            chain.proceed(request)
        }
//        val certificatePin = ""
//
//        val certificatePinner = CertificatePinner.Builder()
//            .add("com.thetopheadlines",certificatePin) // host name
//            .build()

        val httpClient = OkHttpClient.Builder()
 //           .certificatePinner(certificatePinner)
            .addInterceptor(httpLoggingInterceptor)  // Logs API requests
            .addInterceptor(headerInterceptor)  // Adds authentication headers
            .connectTimeout(30, TimeUnit.SECONDS)  // Connection timeout
            .readTimeout(30, TimeUnit.SECONDS)  // Read timeout
            .writeTimeout(30, TimeUnit.SECONDS)  // Write timeout
            .retryOnConnectionFailure(true)  // Retries on failure
            .build()

        return Retrofit.Builder()
            .baseUrl(Utils.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
    }


    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): NewsApiService {
        return retrofit.create(NewsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsRepo(newsApiService: NewsApiService): NewsRepo {
        return NewsRepoImpl(newsApiService)
    }

}