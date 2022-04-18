package com.example.kotlincoroutinedemo.kotlin.frame.flow

import com.example.kotlincoroutinedemo.kotlin.frame.showLog
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.concurrent.Executors

suspend fun main() {

    flowCreate()

//    flowDispatch()

//    flowDispatchAppoint()

//    flowException()

//    flowCancel()

//    flowChannel()

//    flowBackPressure()
}


/**
 * flow 创建
 */
suspend fun flowCreate() {
    /*** 方式一 ***/
    //新元素通过 emit 函数
    val intFlow = flow<Int> {
        emit(1)
        delay(1000)
        emit(2)
        emit(3)
    }
    //读取 flow 需要调用 collect函数
    intFlow.collect { value ->
        showLog(msg = value.toString())
    }


    /*** 方式二 ***/
    val myFlow = flowOf("flow", "kotlin", "android")
    myFlow.collect {
        showLog(msg = it)
    }


    /*** 方式三 ***/
    val listFlow = listOf<Int>(4, 5, 6).asFlow()
    val setFlow = setOf<Int>(7, 8, 9).asFlow()
    listFlow.collect {
        showLog(msg = it.toString())
    }
    setFlow.collect {
        showLog(msg = it.toString())
    }

}


/**
 * flow 调度器 - 线程切换
 */
suspend fun flowDispatch() {

    val intFlow = flow<Int> {
        emit(1)
        delay(1000)
        emit(2)
        emit(3)
    }
    //运行在IO线程上
    intFlow.flowOn(Dispatchers.IO).collect { value ->
        showLog(msg = value.toString())
    }
}


/**
 * 自定义一个调度器
 */
suspend fun flowDispatchAppoint() {

    //调度器
    val dispatcher = Executors
        .newSingleThreadExecutor {
            Thread(it, "My Thread").also { it.isDaemon = true }
        }
        .asCoroutineDispatcher()

    //协程体
    GlobalScope.launch(dispatcher) {
        val intFlow = flow<Int> {
            emit(1)
            delay(1000)
            emit(2)
            emit(3)
        }
        intFlow.flowOn(Dispatchers.IO).collect { value ->
            showLog(msg = value.toString())
        }
    }.join()
}


/**
 * flow 异常处理
 */
suspend fun flowException() {

    val intFlow = flow<Int> {
        emit(100)
        throw ArithmeticException("Div 0")
    }.catch { t -> //捕获异常 (不包括取消异常)
        showLog(msg = "exception : ${t.toString()}")
    }.onCompletion { t ->
        showLog(msg = "finally : $t")
    }
    intFlow.collect {
        showLog(msg = it.toString())
    }
}


/**
 * flow取消
 *
 * 只需取消flow所在的协程即可取消对应的flow
 */
suspend fun flowCancel() {

    val job = GlobalScope.launch(start = CoroutineStart.DEFAULT) {
        val intFlow = flow<Int> {
            emit(1)
            delay(1000)
            emit(2)
            emit(3)
        }
        intFlow.collect { value ->
            showLog(msg = value.toString())
        }
    }
    showLog("1")
    job.cancel()
    showLog("2")
}


/**
 * flow - Channel
 *
 * 创建一个 flow并在里面切换线程, 需要用ChannelFlow的send(), emit()是不被允许的
 * 因为ChannelFlow的send()是线程安全的
 */
suspend fun flowChannel() {

    //方式一
//    val channel = Channel<Int>()
//    channel.consumeAsFlow()


    //方式二
    val myChannelFlow = channelFlow<Int> {
        send(1)
        //withContext()切换到指定的线程，并在闭包内的逻辑执行结束之后，自动把线程切回去继续执行
        withContext(Dispatchers.IO) {
            send(2)
        }
    }
    myChannelFlow.collect {
        showLog(msg = it.toString())
    }

}


/**
 * 背压问题
 *
 * collectLatest - 新值发送时取消之前的
 */
suspend fun flowBackPressure() {
    //1 发射后要处理100ms, 还没有处理完又发射了2
    //此时会取消1的处理
    flow<Int> {
        emit(1)
        delay(50)
        emit(2)
    }.collectLatest { vaule ->
        showLog(msg = "collecting $vaule")
        delay(100)
        showLog(msg = "collected $vaule")
    }
}