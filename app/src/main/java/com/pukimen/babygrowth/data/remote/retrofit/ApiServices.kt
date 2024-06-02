package com.pukimen.babygrowth.data.remote.retrofit

import com.pukimen.babygrowth.data.remote.response.LoginResponse
import com.pukimen.babygrowth.data.remote.response.NuritionResponse
import com.pukimen.babygrowth.data.remote.response.NuritionResponseItem
import com.pukimen.babygrowth.data.remote.response.RecomendationResponse
import com.pukimen.babygrowth.data.remote.response.RegisterResponse
import com.pukimen.babygrowth.data.remote.response.RekomendasiItem
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("nutrition")
    fun getNutrition(@Query("query") query: String): Call<List<NuritionResponseItem>>

    @GET("recommend")
    fun getRecomendation(@Query("id_resep") query: String): Call<RecomendationResponse>


}