package com.junseo.postboy.service

import com.junseo.postboy.util.web.callback.BaseCallback
import io.reactivex.rxjava3.disposables.Disposable

interface BaseService {

    fun get(uri: String, queryMap: Map<String, String>?, callback: BaseCallback<String>): Disposable

    fun post(uri: String, queryMap: Map<String, String>?, callback: BaseCallback<String>): Disposable

}