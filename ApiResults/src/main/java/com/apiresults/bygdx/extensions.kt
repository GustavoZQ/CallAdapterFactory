package com.apiresults.bygdx

suspend fun <T : Any> ApiResult<T>.onSuccess(
    executable: suspend (T) -> Unit
): ApiResult<T> = apply {
    if (this is ApiResult.Success<T>){
        executable(data)
    }
}

suspend fun <T : Any> ApiResult<T>.onError(
    executable: suspend (code: Int, message: String) -> Unit
): ApiResult<T> = apply {
    if (this is ApiResult.Error<T>){
        executable(code,message)
    }
}

suspend fun <T : Any> ApiResult<T>.onException(
    executable: suspend (ex: Throwable) -> Unit
): ApiResult<T> = apply {
    if (this is ApiResult.Exception<T>){
        executable(ex)
    }
}