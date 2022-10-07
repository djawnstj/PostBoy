package com.junseo.postboy.repository

import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface BaseRepository {

    @POST("{uri}")
    @FormUrlEncoded
    fun post(@Path(value = "uri", encoded = true) uri: String, @FieldMap queryMap: Map<String, String>?): Single<String>

    @GET("{uri}")
    fun get(@Path(value = "uri", encoded = true) uri: String, @QueryMap queryMap: Map<String, String>?): Single<String>

}