package com.ekr.mis.ui.splash

import com.ekr.mis.data.ResponseError
import com.ekr.mis.data.splash.ResponseSplash
import com.ekr.mis.networking.ApiService
import com.ekr.mis.utils.SessionManager
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SplashPresenter(val view: SplashContract.View) : SplashContract.Presenter {
    init {
        view.initListener()
        view.loading(false)
    }

    override fun doGetData(email: String, phone_number: String, lat_long: String) {
        view.loading(true)
        ApiService.endpoint.checkUser("titin.iswahyudi@nashiruddin.mil.id", "(+62) 872 7143 409", lat_long).enqueue(object :
            Callback<ResponseSplash> {
            override fun onResponse(
                call: Call<ResponseSplash>,
                response: Response<ResponseSplash>
            ) {
                view.loading(false)
                when {
                    response.isSuccessful -> {
                        val responseSplash: ResponseSplash = response.body()!!
                        view.onResult(responseSplash)

                    }
                    response.code() != 200 -> {
                        val responseGlobal: ResponseError = Gson().fromJson(
                            response.errorBody()!!.charStream(),
                            ResponseError::class.java
                        )
                        responseGlobal.message?.let { view.showMessage(it) }
                    }
                }
            }

            override fun onFailure(call: Call<ResponseSplash>, t: Throwable) {
                view.loading(false)
                view.showMessage("Terjadi Kesalahan, Silahkan Coba Kembali")
            }
        })
    }

    override fun setSession(sessionManager: SessionManager, responseSplash: ResponseSplash) {
        responseSplash.token?.let { sessionManager.prefToken = it }
        responseSplash.dataUser.penilaian?.let { sessionManager.prefNilai = it }
        val mainPath = responseSplash.mainPath
        val dataOrtu = responseSplash.dataUser.dataOrangTua
        val data = responseSplash.dataUser.dataLengkapAnggota
        if (mainPath != null) {
            sessionManager.mainDefaultPath = mainPath.defaultPath
            sessionManager.mainFotoFile = mainPath.fileFoto
            sessionManager.mainIconPath = mainPath.iconPath
            sessionManager.mainLogoPath = mainPath.logoPath
            sessionManager.mainFotoPath = mainPath.fotoPath
        }
        if (data != null) {
            sessionManager.prefNohp = data.telpon.toString()
            sessionManager.prefAlamat = data.alamat.toString()
            sessionManager.prefAgama = data.agama.toString()
            sessionManager.prefEmail = data.email.toString()
            sessionManager.prefFoto = data.fileFoto.toString()
            sessionManager.prefFullname = data.nama.toString()
            sessionManager.prefRole = data.mode.toString()
            sessionManager.prefCompany = data.namaOrganisasi.toString()
            sessionManager.prefNik = data.nik.toString()
            sessionManager.prefTangglaLahir = data.tglLahir.toString()
            sessionManager.prefTempatLahir = data.tempatLahir.toString()
        }
        if (dataOrtu != null) {
            sessionManager.prefFotoOrangtua = dataOrtu.fileFoto.toString()
            sessionManager.prefNamaOrangtua = dataOrtu.nama.toString()
            sessionManager.prefAgamaOrangtua = dataOrtu.agama.toString()
            sessionManager.prefEmailOrangtua = dataOrtu.email.toString()
            sessionManager.prefGenderOrangtua = dataOrtu.gender.toString()
            sessionManager.prefNikOrangtua = dataOrtu.nik.toString()
            sessionManager.prefTelpOrangtua = dataOrtu.telpon.toString()
            sessionManager.prefOrganisasiOrangtua = dataOrtu.namaOrganisasi.toString()
        }


    }
}