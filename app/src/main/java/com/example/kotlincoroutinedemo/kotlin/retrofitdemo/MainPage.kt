package com.example.kotlincoroutinedemo.kotlin.retrofitdemo

import com.example.kotlincoroutinedemo.kotlin.frame.showLog


suspend fun main() {

    //异步逻辑, 同步写法
    val loginIdA = "A"
    val userA = getUserInfoService
        .getUserInfo(loginIdA)
        .waitUser()
    showLog(msg = "--- response : $userA")

    val loginIds = arrayOf("A", "B")
    val users = loginIds.map { loginId ->
        getUserInfoService.getUserInfo(loginId).waitUser()
    }
    users.forEach {
        it?.let {
            showLog(msg = "--- name : ${it.name}   info : $it")
        }
    }


//    GlobalScope.launch {
//        val loginIdA = "A"
//        val userA = getUserInfoService.getUserInfo(loginIdA).waitUser()
//        showLog(msg = "--- response : $userA")
//    }.join()

//    GlobalScope.launch {
//        val loginIds = arrayOf("A", "B")
//        val users = loginIds.map { loginId ->
//            getUserInfoService.getUserInfo(loginId).waitUser()
//        }
//        users.forEach {
//            it?.let {
//                showLog(msg = "--- name : ${it.name}   info : $it")
//            }
//        }
//    }.join()

}






