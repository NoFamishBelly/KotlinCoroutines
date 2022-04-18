package com.example.kotlincoroutinedemo.kotlin.frame.scope

import com.example.kotlincoroutinedemo.kotlin.frame.showLog
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

suspend fun main() {


    //顶级作用域
//    topLevelScope()

    //协同作用域
//    coordinationScope()

    //主从作用域
    masterSlaveScope()
}


/**
 * 顶级作用域
 * 异常：不向外传播
 *
 * 协程#1内的异常不会向外传播
 */
suspend fun topLevelScope() {
    val job = GlobalScope.launch { //协程 #1
        showLog("1")
        launch { //协程 #2
            throw ArithmeticException("Div 0")
        }.join()
        showLog("2")
    }
    showLog("3", "${job.isActive}")
    job.join()
    showLog("4")
}


/**
 * 协同作用域
 * 异常：双向传播
 *
 * 协程#1异常会取消协程#2, 协程#2异常会取消协程#1
 */
suspend fun coordinationScope() {
    val job = GlobalScope.launch { //协程 #1
        coroutineScope {
            showLog("1")
            launch { //协程 #2
                throw java.lang.ArithmeticException("Div 0")
            }.join()
            showLog("2")
        }
    }
    showLog("3", "${job.isActive}")
    job.join()
    showLog("4")
}


/**
 * 主从作用域
 * 异常：自上而下  单向传播
 *
 * 协程#1异常会取消协程#2, 协程#2异常不会取消协程#1
 */
suspend fun masterSlaveScope() {
    val job = GlobalScope.launch { //协程 #1
        supervisorScope {
            showLog("1")
            launch { //协程 #2
                throw java.lang.ArithmeticException("Div 0")
            }.join()
            showLog("2")
        }
    }
    showLog("3", "${job.isActive}")
    job.join()
    showLog("4")
}

