package com.junseo.postboy.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "param",
    foreignKeys = [
        ForeignKey(
            entity = HttpCall::class,
            parentColumns = arrayOf("httpCallSeq"),
            childColumns = arrayOf("owner_call"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Param(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("param_seq")
    var phraseSeq: Long? = null,
    @ColumnInfo(name = "param_key")
    @SerializedName("param_key")
    var paramKey: String = "",
    @ColumnInfo(name = "paramValue")
    @SerializedName("paramValue")
    var paramValue: String = "",
    @ColumnInfo(name = "owner_call")
    @SerializedName("owner_call")
    var ownerCall: Long = -1,
)
