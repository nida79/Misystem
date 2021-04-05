package com.ekr.mis.data

import com.google.gson.annotations.SerializedName

data class ResponseBoolean(
    @SerializedName("status") val status: Boolean
)