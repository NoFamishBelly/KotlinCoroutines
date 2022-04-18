package com.example.kotlincoroutinedemo.kotlin.frame.select

import com.example.kotlincoroutinedemo.kotlin.frame.showLog
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.selects.selectUnbiased

suspend fun main() {

    selectChannel()

//    selectAwait()

//    selectUnbiasedDemo()
}


/**
 * 多个 Channel进行选择
 */
suspend fun selectChannel() {
    GlobalScope.launch {
        //选择最快的一个
        val result = select<String> {
            receiveChannel1().onReceive {
                "From Channel 1 - $it"
            }
            receiveChannel2().onReceive {
                "From Channel 2 - $it"
            }
        }
        showLog(msg = result)
    }.join()
}

/**
 * 接收 Channel 1
 */
fun CoroutineScope.receiveChannel1() = produce<Int> {
    delay(300)
    send(10086)
}

/**
 * 接收 Channel 2
 */
fun CoroutineScope.receiveChannel2() = produce<Int> {
    delay(100)
    send(10010)
}


/**
 * 对多个 await 进行选择
 */
suspend fun selectAwait() {
    GlobalScope.launch {
        val result = select<String> {
            getUserInfoDeferred1().onAwait {
                "From Deferred 1 - $it"
            }
            getUserInfoDeferred2().onAwait {
                "From Deferred 2 - $it"
            }
        }
        showLog(msg = result)
    }.join()
}


/**
 * Deferred 1
 */
fun CoroutineScope.getUserInfoDeferred1() = async {
    delay(50)
    "LeonZhao"
}

/**
 * Deferred 2
 */
fun CoroutineScope.getUserInfoDeferred2() = async {
    delay(50)
    "AlexLiu"
}


suspend fun selectUnbiasedDemo() {
    GlobalScope.launch {
        val result = selectUnbiased<String> {
            getUserInfoDeferred1().onAwait {
                "From Deferred 1 - $it"
            }
            getUserInfoDeferred2().onAwait {
                "From Deferred 2 - $it"
            }
        }
        showLog(msg = result)
    }.join()
}
