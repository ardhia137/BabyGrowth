package com.pukimen.babygrowth.data.remote.response

import com.google.gson.annotations.SerializedName

data class PredictResponse(

    @field:SerializedName("data")
    val data: Data,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: String
)

data class Data(

    @field:SerializedName("suggestion")
    val suggestion: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("label")
    val label: String
)