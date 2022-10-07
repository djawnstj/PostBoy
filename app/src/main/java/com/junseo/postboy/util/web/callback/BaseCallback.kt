package com.junseo.postboy.util.web.callback

interface BaseCallback<T> {

    fun onSuccess(data: T)

    fun onFailure(description: String)

    fun onError(throwable: Throwable)

    fun onLoading()

    fun endLoading()

}