package com.example.kotlincoroutinedemo.kotlin.frame

import kotlin.concurrent.thread
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun hello() = suspendCoroutine<String> { continuation ->
    thread {
        println("${Thread.currentThread().name} --- in hello()")
        Thread.sleep(1000)
        continuation.resume("hello kotlin")
    }
}


fun showLog(i: String = "", msg: String = "") {
    println("${Thread.currentThread().name} --- $i   $msg")
}