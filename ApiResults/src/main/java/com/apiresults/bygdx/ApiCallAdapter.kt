package com.apiresults.bygdx

import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class ApiCallAdapter(
    private val type: Type
) : CallAdapter<Type, Call<ApiResult<Type>>> {
    override fun responseType(): Type = type

    override fun adapt(call: Call<Type>): Call<ApiResult<Type>> =
        ApiResultCall(call)
}