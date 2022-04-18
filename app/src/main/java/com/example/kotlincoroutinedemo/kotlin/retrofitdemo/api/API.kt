package com.example.kotlincoroutinedemo.kotlin.retrofitdemo

import com.example.kotlincoroutinedemo.kotlin.retrofitdemo.entity.User
import com.example.kotlincoroutinedemo.kotlin.retrofitdemo.utils.KotlinConstants
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


interface GetUserInfoService {
    //suspend 挂起标志
    @GET("users/{login}")
    fun getUserInfo(@Path("login") login: String): Call<User?>
}


val getUserInfoService by lazy {
    val retrofit = Retrofit.Builder()
        .client(OkHttpClient.Builder().addInterceptor(Interceptor {
            it.proceed(it.request()).apply {
                println("${it.request()}")
            }
        }).build())
        .baseUrl(KotlinConstants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    retrofit.create(GetUserInfoService::class.java)
}


/**
 * 协程处理异步代码逻辑
 * Call的扩展函数写法
 */
suspend fun <T> Call<T>.waitUser() = suspendCoroutine<T> { continuation ->
    enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            response.takeIf { it.isSuccessful }?.body()
                ?.also {
                    //成功, 返回结果
                    continuation.resume(it)
                }
                ?: //失败, 返回异常
                continuation.resumeWithException(HttpException(response))
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            //失败, 返回异常
            continuation.resumeWithException(t)
        }
    })
}


/**
 * 协程处理异步代码逻辑
 * Call的扩展函数写法 - 支持协程取消 suspendCancellableCoroutine
 */
suspend fun <T> Call<T>.waitUserCancellable() =
    suspendCancellableCoroutine<T> { cancellableContinuation ->

        //取消
        cancellableContinuation.invokeOnCancellation {
            cancel()
        }

        //执行
        enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                response.takeIf { it.isSuccessful }?.body()
                    ?.also {
                        //成功, 返回结果
                        cancellableContinuation.resume(it)
                    }
                    ?: //失败, 返回异常
                    cancellableContinuation.resumeWithException(HttpException(response))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                //失败, 返回异常
                cancellableContinuation.resumeWithException(t)
            }

        })

    }