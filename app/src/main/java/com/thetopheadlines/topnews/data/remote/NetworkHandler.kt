package com.thetopheadlines.topnews.data.remote

import com.thetopheadlines.topnews.domain.model.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.Response
import javax.net.ssl.SSLHandshakeException

object NetworkHandler {

    fun <T> makeSafeApiCall(
        apiCall: suspend () -> Response<T>
    ): Flow<Resource<T>> = flow{
        emit(Resource.Loading)
        val call = apiCall()
        if (call.isSuccessful){
            call.body()?.let {
                emit(Resource.Success(it))
            }?.run {
                emit(Resource.Error("Empty response body"))
            }
        }else {
            emit(Resource.Error(call.message()))
        }
    }.catch { exception->
       val errorMsg = when (exception){
           is SSLHandshakeException -> "SSL handsake exception"
           is IOException -> "IO exception"
           else -> "Exception ${exception.message} "
        }
        emit(Resource.Error(errorMsg))
    }


}