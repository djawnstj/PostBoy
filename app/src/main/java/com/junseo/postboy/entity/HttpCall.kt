package com.junseo.postboy.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.junseo.postboy.entity.converter.DateConverter
import java.util.*

@Entity(tableName = "http_call")
data class HttpCall(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("http_call_seq")
    var httpCallSeq: Long? = null,
    @ColumnInfo(name = "call_name")
    @SerializedName("call_name")
    var callName: String = "",
    @ColumnInfo(name = "call_url")
    @SerializedName("call_url")
    var callUrl: String = "",
    @ColumnInfo(name = "call_path")
    @SerializedName("call_path")
    var callPath: String = "",
    @ColumnInfo(name = "create_date_time", defaultValue = "CURRENT_TIMESTAMP")
    @SerializedName("create_date_time")
    @TypeConverters(DateConverter::class)
    var createDateTime: Date = Date(),
    @ColumnInfo(name = "update_date_time", defaultValue = "CURRENT_TIMESTAMP")
    @SerializedName("update_date_time")
    @TypeConverters(DateConverter::class)
    var updateDateTime: Date = Date()

)
