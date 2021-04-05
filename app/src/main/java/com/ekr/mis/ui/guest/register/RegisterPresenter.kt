package com.ekr.mis.ui.guest.register

import com.ekr.mis.data.ResponseBoolean
import com.ekr.mis.data.ResponseError
import com.ekr.mis.networking.ApiService
import com.ekr.mis.utils.SentenceMessage
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class RegisterPresenter(val view: RegisterContract.View) : RegisterContract.Presenter {
    init {
        view.initListener()
        view.onLoading(false)
    }

    override fun doRegister(
        image: File?,
        email: String,
        token: String,
        nama: String,
        kodepos: String,
        telpon: String,
        alamat: String,
        agama: String
    ) {
        view.onLoading(true)
        val requestBody: RequestBody?
        var foto: MultipartBody.Part? = null
        if (image != null) {
            requestBody = RequestBody.create(MediaType.parse("image/*"), image)
            foto = MultipartBody.Part.createFormData(
                "foto",
                image.name, requestBody
            )
        }


        val data: HashMap<String, RequestBody> = HashMap()
        data["token"] = createPartFromString(token)
        data["nama"] = createPartFromString(nama)
        data["alamat"] = createPartFromString(alamat)
        data["telpon"] = createPartFromString(telpon)
        data["agama"] = createPartFromString(agama)
        data["email"] = createPartFromString(email)
        data["kodepos"] = createPartFromString(kodepos)
        ApiService.endpoint.doRegisUser(foto, data).enqueue(object : Callback<ResponseBoolean> {
            override fun onResponse(
                call: Call<ResponseBoolean>,
                response: Response<ResponseBoolean>
            ) {
                view.onLoading(false)
                when {
                    response.isSuccessful -> {
                        if (response.body()?.status == true) {
                            view.showMessage(SentenceMessage.SUCCESS_INPUT)
                        } else {
                            view.showMessage(SentenceMessage.FAIL_INPUT)
                        }

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

            override fun onFailure(call: Call<ResponseBoolean>, t: Throwable) {
                view.onLoading(false)
                view.showMessage(SentenceMessage.ERROR_MESSAGE)
            }
        })
    }

    private fun createPartFromString(descriptionString: String): RequestBody {
        return RequestBody.create(
            MultipartBody.FORM, descriptionString
        )
    }
}