package com.example.kotlincoroutinedemo.kotlin.retrofitdemo.entity

import java.io.Serializable


data class User(
    val id: String,
    val name: String,
    val url: String,
    val email: String
) : Serializable
