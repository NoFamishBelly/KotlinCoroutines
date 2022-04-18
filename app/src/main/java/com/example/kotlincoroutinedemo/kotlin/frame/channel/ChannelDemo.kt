package com.example.kotlincoroutinedemo.kotlin.frame.channel

import com.example.kotlincoroutinedemo.kotlin.frame.showLog
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.broadcast
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

/**
 * Channel分类 :
 *
 * RENDEZVOUS   不见不散, send调用后挂起直到receive到达
 * UNLIMITED    无限容量, send调用后直接返回
 * CONFLATED    保留最新, receive只能获取最近一次send值
 * BUFFERED     默认容量, 可通过程序参数设置默认大小, 默认为64
 * FIXED        固定容量, 通过参数执行缓存大小
 *
 */
suspend fun main() {

    sendReceive()

//    sendBuilder()
//    receiveBuilder()

//    broadcastChannel()
}


/**
 * Channel 发送/接收
 */
suspend fun sendReceive() {
//    val channel = Channel<Int>(Channel.RENDEZVOUS)
//    val channel = Channel<Int>(Channel.UNLIMITED)
    val channel = Channel<Int>(Channel.CONFLATED)
//    val channel = Channel<Int>(Channel.BUFFERED)
//    val channel = Channel<Int>(1)


    //发送消息
    val producer = GlobalScope.launch {
        for (i in 0..3) {
            showLog(msg = "sending $i")
            channel.send(i)
            showLog(msg = "sent $i")
        }
        showLog(msg = "isClosedForSend - ${channel.isClosedForSend}")
        channel.close()
        showLog(msg = "isClosedForSend - ${channel.isClosedForSend}")
    }

    //接收消息
    val consumer = GlobalScope.launch {
        showLog(msg = "isClosedForReceive - ${channel.isClosedForReceive}  start")
        while (!channel.isClosedForReceive) {
            showLog(msg = "receiving")
            val value = channel.receive()
            showLog(msg = "received $value")
        }
        showLog(msg = "isClosedForReceive - ${channel.isClosedForReceive}  end")
    }


    //接收消息 - 迭代方式
//    val consumer = GlobalScope.launch {
//        showLog(msg = "isClosedForReceive - ${channel.isClosedForReceive}  start")
//        for (e in channel) {
//            showLog(msg = "received $e")
//        }
//        showLog(msg = "isClosedForReceive - ${channel.isClosedForReceive}  end")
//    }


    producer.join()
    consumer.join()
}


/**
 * 发送 Builder : 自动 close
 */
suspend fun sendBuilder() {

    val receiverChannel = GlobalScope.produce<Int>(capacity = Channel.UNLIMITED) {
        for (i in 0..3) {
            showLog(msg = "sending $i")
            send(i)
            showLog(msg = "sent $i")
        }
    }


    val consumer = GlobalScope.launch {
        for (e in receiverChannel) {
            showLog(msg = "received $e")
        }
    }
    consumer.join()
}


/**
 * 接收 Builder
 */
suspend fun receiveBuilder() {

    val sendChannel = GlobalScope.actor<Int>(capacity = Channel.UNLIMITED) {
        for (e in this) {
            showLog(msg = "received $e")
        }
    }

    val producer = GlobalScope.launch {
        for (i in 0..3) {
            showLog(msg = "sending $i")
            sendChannel.send(i)
            showLog(msg = "sent $i")
        }
    }

    producer.join()
}


/**
 * 广播 Channel
 * 三种创建方式
 */
suspend fun broadcastChannel() {

    //方式一
//    val channel = Channel<Int>(Channel.UNLIMITED)
//    val broadcastChannel = channel.broadcast()

    //方式二
//    val broadcastChannel = BroadcastChannel<Int>(Channel.UNLIMITED)

    //方式三
    val broadcastChannel = GlobalScope.broadcast<Int>(capacity = Channel.BUFFERED) {
        for (i in 0..3) {
            send(i)
        }
    }


    //开启5个接收协程
    List(5) { index ->
        GlobalScope.launch {
            val receiveChannel = broadcastChannel.openSubscription()
            for (e in receiveChannel) {
                showLog(msg = "[#$index]  received  $e")
            }
        }
    }.joinAll()

}









