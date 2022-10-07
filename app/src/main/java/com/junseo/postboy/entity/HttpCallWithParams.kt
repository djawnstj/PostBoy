package com.junseo.postboy.entity

import androidx.room.Embedded
import androidx.room.Relation

data class HttpCallWithParams(
    @Embedded val httpCall: HttpCall = HttpCall(),
    @Relation(
          parentColumn = "httpCallSeq",
          entityColumn = "owner_call"
    )
    val params: List<Param> = listOf()
)