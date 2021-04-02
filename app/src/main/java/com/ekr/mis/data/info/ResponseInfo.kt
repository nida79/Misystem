package com.ekr.mis.data.info


import com.google.gson.annotations.SerializedName

data class ResponseInfo(
    @SerializedName("data_organisasi") val dataInfo: List<DataInfo>?,
    @SerializedName("status") val status: Boolean
)