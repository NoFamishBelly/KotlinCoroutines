package com.example.kotlincoroutinedemo.kotlin.frame.cancel

import com.example.kotlincoroutinedemo.kotlin.frame.hello
import com.example.kotlincoroutinedemo.kotlin.frame.showLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

suspend fun main() {

    val job = CoroutineScope(Dispatchers.Default).launch {
        showLog("1")
        val res = hello()
        showLog("2", res)
        delay(1000)
        showLog("3")
    }
    showLog("4", "before cancel : ${job.isActive}")
    job.cancel() //hello执行, delay未执行
    showLog("5", "after cancel : ${job.isActive}")
    job.join()
}