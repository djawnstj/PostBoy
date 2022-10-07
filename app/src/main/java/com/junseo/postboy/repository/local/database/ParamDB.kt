package com.junseo.postboy.repository.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.junseo.postboy.entity.HttpCall
import com.junseo.postboy.entity.HttpCallWithParams
import com.junseo.postboy.entity.Param
import com.junseo.postboy.entity.converter.DateConverter
import com.junseo.postboy.repository.local.dao.HttpCallDAO
import com.junseo.postboy.repository.local.dao.ParamDAO

@Database(entities = [Param::class, HttpCall::class], version = 2, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class ParamDB: RoomDatabase() {
    abstract fun getParamDao(): ParamDAO
    abstract fun getHttpCallDao(): HttpCallDAO
}