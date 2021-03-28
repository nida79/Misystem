package com.ekr.mis.data.splash


import com.google.gson.annotations.SerializedName

data class MainPath(
    @SerializedName("default_path") val defaultPath: String,
    @SerializedName("file_foto") val fileFoto: String,
    @SerializedName("foto_path") val fotoPath: String,
    @SerializedName("icon_path") val iconPath: String,
    @SerializedName("logo_path") val logoPath: String

)
