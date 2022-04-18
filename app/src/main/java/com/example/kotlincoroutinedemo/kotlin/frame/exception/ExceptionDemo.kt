package com.example.kotlincoroutinedemo.kotlin.frame.exception

import com.example.kotlincoroutinedemo.kotlin.frame.showLog
import kotlinx.coroutines.*

suspend fun main() {


    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        showLog(msg = "${coroutineContext[CoroutineName]?.name} : ${coroutineContext[Job]} , $throwable")
    }

    val job = CoroutineScope(Dispatchers.Default).launch(exceptionHandler) {
        showLog("1")
        delay(1000)
        showLog("2")
        throw ArithmeticException("Div 0")
        showLog("3")
    }

    showLog("4", "${job.isActive}")
    job.join()

}