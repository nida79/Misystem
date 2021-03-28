package com.ekr.mis.data.splash


import com.google.gson.annotations.SerializedName

data class DataOrangTua(
    @SerializedName("agama") val agama: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("file_foto") val fileFoto: String?,
    @SerializedName("gender") val gender: String?,
    @SerializedName("nama") val nama: String?,
    @SerializedName("nama_organisasi") val namaOrganisasi: String?,
    @SerializedName("nik") val nik: Any?,
    @SerializedName("telpon") val telpon: String?
)