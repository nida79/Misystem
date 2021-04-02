package com.ekr.mis.ui.home.info.organization

import com.ekr.mis.data.ResponseError
import com.ekr.mis.data.info.ResponseInfo
import com.ekr.mis.networking.ApiService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrgPresenter(val view: OrganizationContract.View) : OrganizationContract.Presenter {
    init {
        view.initListener()
        view.loading(false)
    }

    override fun getNews(
        token: String,
        telpon: String,
        mode: Int,
        latlong: String,
        email: String
    ) {
        view.loading(true)
        ApiService.endpoint.getInfoHomeOrg(email, telpon, latlong, token, mode)
            .enqueue(object : Callback<ResponseInfo> {
                override fun onResponse(
                    call: Call<ResponseInfo>,
                    response: Response<ResponseInfo>
                ) {
                    view.loading(false)
                    when {
                        response.isSuccessful -> {
                            val result: ResponseInfo? = response.body()
                            result?.let { view.onResult(it) }
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

                override fun onFailure(call: Call<ResponseInfo>, t: Throwable) {
                    view.loading(false)
                    view.showMessage("Terjadi Kesalahan, Silahkan Coba Kembali")
                }

            })
    }
}