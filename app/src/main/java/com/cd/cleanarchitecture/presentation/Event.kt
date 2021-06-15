package com.cd.cleanarchitecture.presentation

data class Event<out T>(private val content : T){

    private var hasbeenHandled = false

    fun getContentIfNotHandled() : T? = if(hasbeenHandled){
        null
    }else{
        hasbeenHandled = true
        content
    }
}