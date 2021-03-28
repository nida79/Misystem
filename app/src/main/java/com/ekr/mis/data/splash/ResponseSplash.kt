package com.ekr.mis.data.splash


import com.google.gson.annotations.SerializedName

data class ResponseSplash(
    @SerializedName("data_user") val dataUser: DataUser,
    @SerializedName("main_path") val mainPath: MainPath?,
    @SerializedName("status") val status: Boolean,
    @SerializedName("token") val token: String?
)