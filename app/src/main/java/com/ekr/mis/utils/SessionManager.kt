package com.ekr.mis.utils

import android.content.Context
import android.content.SharedPreferences
import hu.autsoft.krate.Krate
import hu.autsoft.krate.booleanPref
import hu.autsoft.krate.stringPref

class SessionManager(context: Context) : Krate {
    private val PREFS_IS_LOGIN: String = "prefs_is_login"

    //Data Member
    private val PREFS_FULL_NAME: String = "prefs_is_full_name"
    private val PREFS_ALAMAT: String = "prefs_is_alamat"
    private val PREFS_TOKEN: String = "prefs_is_token"
    private val PREFS_NO_HP: String = "prefs_is_no_hp"
    private val PREFS_FOTO: String = "prefs_is_foto"
    private val PREFS_ROLE: String = "prefs_is_role"
    private val PREFS_EMAIL: String = "prefs_is_email"
    private val PREFS_AGAMA: String = "prefs_agama"
    private val PREFS_COMPANY: String = "prefs_company"
    private val PREFS_NIK: String = "prefs_nik"
    private val PREFS_TEMPAT_LAHIR: String = "prefs_t_lahir"
    private val PREFS_TGL_LAHIR: String = "prefs_tgl_lahir"

    //Main Path
    private val PREFS_MAIN_DEFAULT: String = "prefs_main_main_path"
    private val PREFS_MAIN_FOTO: String = "prefs_main_foto"
    private val PREFS_MAIN_FOTO_PATH: String = "prefs_main_foto_path"
    private val PREFS_MAIN_ICON_PATH: String = "prefs_main_icon_path"
    private val PREFS_MAIN_LOGO_PATH: String = "prefs_main_logo_path"

    //Data Parents
    private val PREFS_NAMA_ORTU: String = "prefs_nama_ortu"
    private val PREFS_ALAMAT_ORTU: String = "prefs_alamat_ortu"
    private val PREFS_GENDER_ORTU: String = "prefs_gender_ortu"
    private val PREFS_AGAMA_ORTU: String = "prefs_agama_ortu"
    private val PREFS_ORGANISASI_ORTU: String = "prefs_organisasi_ortu"
    private val PREFS_TELEPON_ORTU: String = "prefs_telp_ortu"
    private val PREFS_EMAIL_ORTU: String = "prefs_email_ortu"
    private val PREFS_NIK_ORTU: String = "prefs_nik_ortu"
    private val PREFS_FOTO_ORTU: String = "prefs_foto_ortu"

    //Data Nila
    private val PREFS_NILAI: String = "prefs_nilai"

    override val sharedPreferences: SharedPreferences = context.applicationContext
        .getSharedPreferences("jularis_prefs", Context.MODE_PRIVATE)
    var prefIsLogin by booleanPref(PREFS_IS_LOGIN, false)

    //Main Path
    var mainDefaultPath by stringPref(PREFS_MAIN_DEFAULT, "")
    var mainFotoPath by stringPref(PREFS_MAIN_FOTO_PATH, "")
    var mainIconPath by stringPref(PREFS_MAIN_ICON_PATH, "")
    var mainLogoPath by stringPref(PREFS_MAIN_LOGO_PATH, "")
    var mainFotoFile by stringPref(PREFS_MAIN_FOTO, "")

    //Data member
    var prefFullname by stringPref(PREFS_FULL_NAME, "")
    var prefToken by stringPref(PREFS_TOKEN, "")
    var prefAlamat by stringPref(PREFS_ALAMAT, "")
    var prefNohp by stringPref(PREFS_NO_HP, "")
    var prefFoto by stringPref(PREFS_FOTO, "")
    var prefRole by stringPref(PREFS_ROLE, "")
    var prefEmail by stringPref(PREFS_EMAIL, "")
    var prefAgama by stringPref(PREFS_AGAMA, "")
    var prefCompany by stringPref(PREFS_COMPANY, "")
    var prefNik by stringPref(PREFS_NIK, "")
    var prefTempatLahir by stringPref(PREFS_TEMPAT_LAHIR, "")
    var prefTangglaLahir by stringPref(PREFS_TGL_LAHIR, "")

    //Data Orang Tua
    var prefFotoOrangtua by stringPref(PREFS_FOTO_ORTU, "")
    var prefNamaOrangtua by stringPref(PREFS_NAMA_ORTU, "")
    var prefGenderOrangtua by stringPref(PREFS_GENDER_ORTU, "")
    var prefAgamaOrangtua by stringPref(PREFS_AGAMA_ORTU, "")
    var prefOrganisasiOrangtua by stringPref(PREFS_ORGANISASI_ORTU, "")
    var prefTelpOrangtua by stringPref(PREFS_TELEPON_ORTU, "")
    var prefEmailOrangtua by stringPref(PREFS_EMAIL_ORTU, "")
    var prefNikOrangtua by stringPref(PREFS_NIK_ORTU, "")

    //data nilai
    var prefNilai by stringPref(PREFS_NILAI,"")

    fun logOut() {
        sharedPreferences.edit().clear().apply()
    }
}