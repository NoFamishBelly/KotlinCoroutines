package com.example.kotlincoroutinedemo.kotlin.frame.dispatcher

import com.example.kotlincoroutinedemo.R
import com.example.kotlincoroutinedemo.kotlin.frame.hello
import com.example.kotlincoroutinedemo.kotlin.utils.BaseAbstractActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Dispatcher2DemoAct : BaseAbstractActivity() {
    override fun getLayout(): Int {
        return R.layout.act_dispatcher2_demo
    }

    override fun initView() {
        //lifeScope绑定生命周期


        val job = CoroutineScope(Dispatchers.Main).launch {
            val msg1 = "${Thread.currentThread().name} : --- 1"
            R.id.id_tv_result.showText(msg1)
            delay(1000)
            val msg2 =
                R.id.id_tv_result.getTextFromTextView() + "\n" + "${Thread.currentThread().name} : --- 2"
            R.id.id_tv_result.showText(msg2)
            val res = hello()
            val msg3 =
                R.id.id_tv_result.getTextFromTextView() + "\n" + "${Thread.currentThread().name} : --- $res"
            R.id.id_tv_result.showText(msg3)
        }

        job.cancel()
    }

}