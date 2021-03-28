package com.ekr.mis.data

import com.google.gson.annotations.SerializedName

data class ResponseError(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String?
)
