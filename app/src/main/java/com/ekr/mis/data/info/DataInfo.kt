package com.ekr.mis.data.info


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataInfo(
    @SerializedName("alamat") val alamat: String?,
    @SerializedName("default_path") val defaultPath: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("icon_path") val iconPath: String?,
    @SerializedName("logo_path") val logoPath: String?,
    @SerializedName("nama_organisasi") val namaOrganisasi: String?,
    @SerializedName("telpon") val telpon: String?
):Parcelable