package com.example.image.core

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}