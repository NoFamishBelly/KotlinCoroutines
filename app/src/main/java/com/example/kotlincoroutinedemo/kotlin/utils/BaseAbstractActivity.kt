package com.example.kotlincoroutinedemo.kotlin.utils

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

abstract class BaseAbstractActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getLayout() != 0) {
            val inflater = LayoutInflater.from(this@BaseAbstractActivity)
            val baseView = inflater.inflate(getLayout(), null)
            setContentView(baseView)
        }

        initView()
    }


    protected abstract fun getLayout(): Int

    protected abstract fun initView()

    protected fun getContext(): Context {
        return this@BaseAbstractActivity
    }


    protected fun Int.setViewClick(click: (view: View) -> Unit) {
        findViewById<View>(this).setOnClickListener {
            click(it)
        }
    }


    protected fun Int.showText(text: String) {
        findViewById<TextView>(this).text = text
    }

    protected fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }


    protected fun Int.getTextFromTextView(): String {
        return findViewById<TextView>(this).text.toString().trim()
    }
}