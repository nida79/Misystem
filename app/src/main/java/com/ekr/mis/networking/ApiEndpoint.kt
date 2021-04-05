package com.ekr.mis.networking

import com.ekr.mis.data.ResponseBoolean
import com.ekr.mis.data.info.ResponseInfo
import com.ekr.mis.data.splash.ResponseSplash
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface ApiEndpoint {
    @FormUrlEncoded
    @POST("apimis/awal")
    fun checkUser(
        @Field("email") email: String,
        @Field("telpon") telpon: String,
        @Field("lalo") lalo: String
    ): Call<ResponseSplash>

    @FormUrlEncoded
    @POST("apimis/organisasall")
    fun getInfoHomeOrg(
        @Field("email") email: String,
        @Field("telpon") telpon: String,
        @Field("lalo") lalo: String,
        @Field("token") token: String,
        @Field("mode") mode: Int,
    ): Call<ResponseInfo>

    @Multipart
    @POST("apimis/input_anggota_post")
    fun doRegisUser(
        @Part photo: MultipartBody.Part?,
        @PartMap data: Map<String, @JvmSuppressWildcards RequestBody>,
    ): Call<ResponseBoolean>

}