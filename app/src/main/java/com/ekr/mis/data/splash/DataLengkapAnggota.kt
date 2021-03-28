package com.ekr.mis.data.splash


import com.google.gson.annotations.SerializedName

data class DataLengkapAnggota(
    @SerializedName("agama") val agama: String?,
    @SerializedName("alamat") val alamat: String?,
    @SerializedName("default_path") val defaultPath: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("file_foto") val fileFoto: String?,
    @SerializedName("gender") val gender: String?,
    @SerializedName("hak_akses") val hakAkses: String?,
    @SerializedName("icon_path") val iconPath: String?,
    @SerializedName("kodepos") val kodepos: String?,
    @SerializedName("logo_path") val logoPath: String?,
    @SerializedName("mode") val mode: String?,
    @SerializedName("nama") val nama: String?,
    @SerializedName("nama_organisasi") val namaOrganisasi: String?,
    @SerializedName("nik") val nik: Any?,
    @SerializedName("nis") val nis: Any?,
    @SerializedName("telpon") val telpon: String?,
    @SerializedName("tempat_lahir") val tempatLahir: Any?,
    @SerializedName("tgl_lahir") val tglLahir: String?
)