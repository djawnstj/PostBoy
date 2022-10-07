package com.junseo.postboy.repository.local.dao

import androidx.room.*
import com.junseo.postboy.entity.HttpCall
import com.junseo.postboy.entity.HttpCallWithParams

@Dao
interface HttpCallDAO {

    @Query("select * from http_call")
    fun findAll(): List<HttpCall>

    @Query("select * from http_call where httpCallSeq = :httpCallSeq")
    fun findById(httpCallSeq: Long): HttpCall

    @Insert(onConflict = OnConflictStrategy.REPLACE) // PK 충돌시 덮어쓰기
    fun save(httpCall: HttpCall): Long

    @Query("delete from http_call where httpCallSeq = :httpCallSeq")
    fun deleteParamById(httpCallSeq: Int)

    @Update
    fun updateParam(httpCall: HttpCall)

    @Transaction
    @Query("SELECT * FROM http_call")
    fun findHttpCallWithParams(): List<HttpCallWithParams>

    @Transaction
    @Query("SELECT * FROM http_call WHERE httpCallSeq = :httpCallSeq")
    fun findHttpCallWithParamsById(httpCallSeq: Long): HttpCallWithParams?

}