package com.example.kotlincoroutinedemo.kotlin.frame.async

import com.example.kotlincoroutinedemo.kotlin.frame.showLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlin.concurrent.thread
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun main() {
    val deferred = CoroutineScope(Dispatchers.Default).async {
        val res = getValue()
        res
    }
    val result = deferred.await()
    showLog(msg = "result : $result")
}


suspend fun getValue() = suspendCoroutine<String> { continuation ->
    thread {
        Thread.sleep(1000)
        continuation.resume("value is hello")
    }
}