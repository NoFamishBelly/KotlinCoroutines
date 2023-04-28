package com.example.kotlincoroutinedemo.kotlin.launchasync

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import java.net.InetAddress
import kotlin.concurrent.thread
import kotlin.coroutines.resume


/**
 * launch 和 async 唯一的区别就是返回值不同：
 * launch的Job携带返回值，async的Deferred携带返回值
 *
 *
 * launch 和 async 构建器都可以用于异步执行任务
 * 它们都可以在协程上下文中启动一个新的协程，并且该协程的执行是异步的，不会阻塞当前线程
 *
 *
 * launch的join()方法、async的await()方法会阻塞当前协程体, 但不会阻塞当前线程
 *
 *
 * Deferred 继承自 Job 接口，Job有的它都有，增加了一个方法 await ，这个方法接收的是 async 闭包中返回的值
 *
 */
fun main() {
    testLaunch.invoke()
//    testLaunchJoin.invoke()
//    testRunBlocking.invoke()
//    testAsync.invoke()
//    testAsyncAwait.invoke()
    end()
}


/**
 * async的await()方法，要在协程体内才可调用
 * await()会阻塞当前协程体, 不会阻塞当前线程
 *
 * 输出结果
 * -1 --- main
 * 6 --- main   (-1, 6是当前线程 ,不会被阻塞)
 *
 * 0 --- DefaultDispatcher-worker-1
 * 4 --- DefaultDispatcher-worker-1 (0, 4, 5是join所在协程, 会被阻塞)
 *
 * 1 --- DefaultDispatcher-worker-1
 * 2 --- Thread-2   14.119.104.254
 * 3 --- DefaultDispatcher-worker-1
 *
 * 5 --- DefaultDispatcher-worker-1  Deferred返回值
 */
val testAsyncAwait: () -> Unit = {
    showLog(-1)

    GlobalScope.launch {
        showLog(0)
        val deferred = GlobalScope.async {
            showLog(1)
            analysisDns()
            showLog(3)
            "Deferred返回值"
        }
        showLog(4)
        val res = deferred.await()
        showLog(5, res)
    }

    showLog(6)
}


/**
 * async 异步执行，lambda最后一行为返回结果，不会阻塞当前线程
 *
 * 输出结果
 *
 * 0 --- main
 * 4 --- main (0, 4当前线程, 不会被阻塞)
 *
 * 1 --- DefaultDispatcher-worker-1
 * 2 --- Thread-2   14.119.104.254
 * 3 --- DefaultDispatcher-worker-1
 */
val testAsync: () -> Unit = {
    showLog(0)
    GlobalScope.async {
        showLog(1)
        analysisDns()
        showLog(3)
    }
    showLog(4)
}


/**
 * runBlocking启动的协程任务会  阻断当前线程，直到该协程执行结束
 *
 * 输出结果
 * 0 --- main
 * 1 --- main
 * 2 --- Thread-0   14.119.104.189
 * 3 --- main
 * 4 --- main
 */
val testRunBlocking: () -> Unit = {
    showLog(0)

    runBlocking {
        showLog(1)
        analysisDns()
        showLog(3)
    }

    showLog(4)
}


/**
 * launch的join()方法，要在协程体内才可调用
 * join()会阻塞当前协程体, 不会阻塞当前线程
 *
 * 输出结果
 * -1 --- main
 * 6 --- main   (-1, 6是当前线程 ,不会被阻塞)
 *
 * 0 --- DefaultDispatcher-worker-1
 * 4 --- DefaultDispatcher-worker-1 (0, 4, 5是join所在协程, 会被阻塞)
 *
 * 1 --- DefaultDispatcher-worker-1
 * 2 --- Thread-2   14.119.104.254
 * 3 --- DefaultDispatcher-worker-1
 *
 * 5 --- DefaultDispatcher-worker-1
 */
val testLaunchJoin: () -> Unit = {
    showLog(-1)

    GlobalScope.launch {
        showLog(0)
        val job = GlobalScope.launch {
            showLog(1)
            analysisDns()
            showLog(3)
        }
        showLog(4)
        job.join()
        showLog(5)
    }

    showLog(6)
}


/**
 * launch 异步执行，无需返回结果，不会阻塞当前线程
 *
 * 输出结果
 *
 * 0 --- main
 * 4 --- main (0, 4当前线程, 不会被阻塞)
 *
 * 1 --- DefaultDispatcher-worker-1
 * 2 --- Thread-2   14.119.104.254
 * 3 --- DefaultDispatcher-worker-1
 */
val testLaunch: () -> Unit = {
    showLog(0)
    GlobalScope.launch {
        showLog(1)
        analysisDns()
        showLog(3)
    }
    showLog(4)
}


/**
 * 防止程序过早结束，看不到效果
 */
private fun end() {
    print("\n----------------------------- 输入任意键结束 -----------------------------\n\n")
    readLine()
}


private suspend fun analysisDns() = suspendCancellableCoroutine<String> {
    thread {
        try {
            val ipAddress = InetAddress.getByName("www.baidu.com")
            showLog(2, ipAddress.hostAddress)
            it.resume(ipAddress.hostAddress)
        } catch (e: Exception) {
            it.resume("解析失败: $e")
        }
    }
}


fun showLog(i: Int, msg: String = "") {
    println("$i --- ${Thread.currentThread().name}   $msg")
}