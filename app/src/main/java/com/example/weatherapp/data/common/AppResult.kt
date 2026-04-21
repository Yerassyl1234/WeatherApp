package com.example.weatherapp.data.common

import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import kotlin.coroutines.cancellation.CancellationException

sealed interface AppResult<out T> {
    data class Success<T>(val data: T) : AppResult<T>
    data class Error(val exception: AppException) : AppResult<Nothing>
}

inline fun <T> AppResult<T>.onSuccess(action: (T) -> Unit): AppResult<T> {
    if (this is AppResult.Success) action(data)
    return this
}

inline fun <T> AppResult<T>.onError(action: (AppException) -> Unit): AppResult<T> {
    if (this is AppResult.Error) action(exception)
    return this
}

inline fun <T, R> AppResult<T>.map(transform: (T) -> R): AppResult<R> {
    return when (this) {
        is AppResult.Success -> AppResult.Success(transform(data))
        is AppResult.Error -> AppResult.Error(exception)
    }
}

suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): AppResult<T> {
    return try {
        val response = apiCall()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                AppResult.Success(body)
            } else {
                AppResult.Error(AppException.Unknown("Body is null"))
            }
        } else {
            when (response.code()) {
                in 400..499 -> AppResult.Error(AppException.ClientError(response.code()))
                in 500..599 -> AppResult.Error(AppException.ServerError(response.code()))
                else -> AppResult.Error(AppException.Unknown("Unexpected response code: ${response.code()}"))
            }
        }
    } catch (e: CancellationException) {
        throw e
    } catch (e: SocketTimeoutException) {
        AppResult.Error(AppException.Timeout)
    } catch (e: IOException) {
        AppResult.Error(AppException.NoInternet)
    } catch (e: Exception) {
        AppResult.Error(AppException.Unknown(e.message ?: "Unknown error"))
    }
}