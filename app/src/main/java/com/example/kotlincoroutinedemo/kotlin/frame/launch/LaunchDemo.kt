package com.example.kotlincoroutinedemo.kotlin.frame.launch

import com.example.kotlincoroutinedemo.kotlin.frame.hello
import com.example.kotlincoroutinedemo.kotlin.frame.showLog
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/************************************************************************
 * Job : launch 会返回一个 Job对象
 *
 * join()	        挂起当前协程, 等待 job 协程执行结束
 * cancel()	        取消协程
 * cancelAndJoin()	取消协程并等待结束. 协程被取消, 但不一定立即结束, 或许还有收尾工作
 */

/************************************************************************
 * CoroutineScope内包含的字段
 *
 * isActive	      是否否正在运行
 * isCompleted	  是否运行完成
 * isCancelled	  是否已取消
 */

suspend fun main() {
    val job = GlobalScope.launch(start = CoroutineStart.DEFAULT) {
        showLog("1")
        val res = hello()
        showLog("2", res)
        delay(1000)
        showLog("3")
    }
    showLog("4", "${job.isActive}")
    //join() : 确保 main 会等待 job执行结束后在结束
    job.join()
}


