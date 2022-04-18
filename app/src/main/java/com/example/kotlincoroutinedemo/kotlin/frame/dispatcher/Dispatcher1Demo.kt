package com.example.kotlincoroutinedemo.kotlin.frame.dispatcher

import com.example.kotlincoroutinedemo.kotlin.frame.hello
import com.example.kotlincoroutinedemo.kotlin.frame.showLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/************************************************************************
 * Dispatchers切换到线程
 *
 * 不指定	                 它从启动了它的 CoroutineScope 中承袭了上下文
 * Dispatchers.Main	         用于Android. 在UI线程中执行
 * Dispatchers.IO	        子线程, 适合执行磁盘或网络 I/O操作
 * Dispatchers.Default	    子线程,适合 执行 cpu 密集型的工作
 * Dispatchers.Unconfined	从当前线程直接执行, 直到第一个挂起点
 *
 */
suspend fun main() {
    CoroutineScope(Dispatchers.Default).launch {
        showLog("1")
        val res = hello()
        showLog("2", res)
        delay(1000)
        showLog("3")
    }.join()
}