package com.example.kotlincoroutinedemo.kotlin.frame.runblocking

import com.example.kotlincoroutinedemo.kotlin.frame.hello
import com.example.kotlincoroutinedemo.kotlin.frame.showLog
import kotlinx.coroutines.*

fun main() = runBlocking {
    showLog("1")
    val job = CoroutineScope(Dispatchers.Default).launch {
        showLog("2")
        val res = hello()
        showLog("3", res)
        delay(1000)
    }

    showLog("4")
    job.join()
    showLog("5")
    delay(1000)
    showLog("6")
}