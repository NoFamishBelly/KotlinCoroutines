package com.example.kotlincoroutinedemo.kotlin.frame.launchmodel

import com.example.kotlincoroutinedemo.kotlin.frame.hello
import com.example.kotlincoroutinedemo.kotlin.frame.showLog
import kotlinx.coroutines.*

suspend fun main() {

//    defaultLaunch()

//    defaultLaunchCancel()

    atomicLaunch()

//    lazyJoinLaunch()

//    lazyAwaitLaunch()

//    unDispatchedLaunch()
}


/**
 * default 启动模式
 *
 * 饿汉式启动，launch 调用后，会立即进入待调度状态，一旦调度器 OK 就可以开始执行
 */
suspend fun defaultLaunch() {
    showLog("1")
    val job = GlobalScope.launch(start = CoroutineStart.DEFAULT) {
        showLog("2")
        val res = hello()
        showLog("3", res)
        delay(1000)
        showLog("4")
    }
    showLog("5")
    //GlobalScope 生命周期受整个进程限制, 进程退出才会自动结束. 它不会使进程保活, 像一个守护线程
    //所以使用延时或者join()方式保证在在main()结束前执行到协程job
    //1.延时
//    Thread.sleep(3000)
    //2.join()
    job.join()

}


/**
 * default启动模式
 *
 * 取消 - 调度前取消则直接取消
 */
suspend fun defaultLaunchCancel() {
    showLog("1")
    val job = GlobalScope.launch(start = CoroutineStart.DEFAULT) {
        showLog("2")
        val res = hello()
        showLog("3", res)
        delay(1000)
        showLog("4")
    }
    showLog("5")
    job.cancel()
    job.join()
    showLog("6")
}


/**
 * atomic启动模式
 *
 * 取消 - 在第一个挂起点之前不能取消
 */
suspend fun atomicLaunch() {
    showLog("1")
    val job = GlobalScope.launch(start = CoroutineStart.ATOMIC) {
        showLog("2")
        delay(1000)
        showLog("3")
        val res = hello()
        showLog("4", res)
    }
    showLog("5")
    job.cancel()
    job.join()
    showLog("6")
}


/**
 * lazy 启动模式 - Join
 *
 * launch 后并不会有任何调度行为，协程体也自然不会进入执行状态，直到我们需要它执行的时候
 *
 * join/await
 */
suspend fun lazyJoinLaunch() {
    showLog("1")
    val job = GlobalScope.launch(start = CoroutineStart.LAZY) {
        showLog("2")
        val res = hello()
        showLog("3", res)
        delay(1000)
        showLog("4")
    }
    showLog("5")
    job.join()
    showLog("6")
}


/**
 * lazy 启动模式 - Await
 */
suspend fun lazyAwaitLaunch() {
    showLog("1")
    val deferred = GlobalScope.async(start = CoroutineStart.LAZY) {
        showLog("2")
        delay(1000)
        showLog("3")
        val res = hello()
        showLog("4", res)
        res
    }
    showLog("5")
    val result = deferred.await()
    showLog("6", result)
}


/**
 * unDispatched 启动模式
 *
 * 直接开始在当前线程下执行，直到第一个挂起点，（在第一个挂起点之前不调度，一直在原有的线程上面立即执行）
 */
suspend fun unDispatchedLaunch() {

    showLog("1")
    val job = GlobalScope.launch(start = CoroutineStart.UNDISPATCHED) {
        showLog("2")
        val res = hello()
        showLog("3", res)
        delay(1000)
        showLog("4")
    }
    showLog("5")
    job.join()
    showLog("6")
}


