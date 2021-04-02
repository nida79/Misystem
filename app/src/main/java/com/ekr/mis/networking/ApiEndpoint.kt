package com.ekr.mis.networking

import com.ekr.mis.data.info.ResponseInfo
import com.ekr.mis.data.splash.ResponseSplash
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

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
    ) :Call<ResponseInfo>
}