package com.apiresults.bygdx

import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class ApiResultCall<T : Any>(
    private val request: Call<T>
) : Call<ApiResult<T>> {

    override fun enqueue(callback: Callback<ApiResult<T>>) {
        request.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val networkResult = handleApi(response)
                callback.onResponse(
                    this@ApiResultCall,
                    Response.success(networkResult)
                )
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val apiResult = ApiResult.Exception<T>(t)
                callback.onResponse(
                    this@ApiResultCall,
                    Response.success(apiResult)
                )
            }
        })
    }

    override fun execute(): Response<ApiResult<T>> = throw NotImplementedError()

    override fun clone(): Call<ApiResult<T>> = ApiResultCall(request.clone())

    override fun isExecuted(): Boolean = request.isExecuted

    override fun cancel() = request.cancel()

    override fun isCanceled(): Boolean = request.isCanceled

    override fun request(): Request = request.request()

    override fun timeout(): Timeout = request.timeout()

    fun <T : Any> handleApi(
        data: Response<T>
    ): ApiResult<T> {
        return try {
            val body = data.body()

            if (data.isSuccessful && body != null) {
                ApiResult.Success(body)
            } else {
                ApiResult.Error(data.code(), data.message())
            }
        } catch (ex: HttpException) {
            ApiResult.Error(ex.code(), ex.message())
        } catch (ex: Throwable) {
            ApiResult.Exception(ex)
        }
    }
}