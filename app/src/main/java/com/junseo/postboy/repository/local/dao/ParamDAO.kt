package com.junseo.postboy.repository.local.dao

import androidx.room.*
import com.junseo.postboy.entity.Param
import java.util.*

@Dao
interface ParamDAO {

    @Query("select * from param")
    fun findAll(): List<Param>

    @Query("select * from param where phraseSeq = :paramSeq")
    fun findById(paramSeq: Long): Optional<Param>

    @Insert(onConflict = OnConflictStrategy.REPLACE) // PK 충돌시 이전 데이터를 수정하고 트렌젝션 진행
    fun save(vararg param: Param)

    @Query("delete from param where phraseSeq = :paramSeq")
    fun deleteParamById(paramSeq: Long)

    @Query("delete from param where owner_call = :ownerCall")
    fun deleteParamByHttpCallId(ownerCall: Long)

    @Update
    fun updateParam(param: Param)

    @Query("SELECT * FROM param where owner_call = :ownerCall")
    fun findAllWithOwnerCall(ownerCall: Long): List<Param>

}