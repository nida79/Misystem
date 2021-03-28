package com.ekr.mis.data.splash


import com.google.gson.annotations.SerializedName

data class DataUser(
    @SerializedName("data_lengkap_anggota") val dataLengkapAnggota: DataLengkapAnggota?,
    @SerializedName("data_orang_tua") val dataOrangTua: DataOrangTua?,
    @SerializedName("penilaian") val penilaian: String?
)