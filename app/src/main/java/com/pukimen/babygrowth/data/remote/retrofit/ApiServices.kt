package com.pukimen.babygrowth.data.remote.retrofit

import com.pukimen.babygrowth.data.remote.response.AllRecipeResponse
import com.pukimen.babygrowth.data.remote.response.AuthResponse
import com.pukimen.babygrowth.data.remote.response.DetailRecipeResponse
import com.pukimen.babygrowth.data.remote.response.LoginResponse
import com.pukimen.babygrowth.data.remote.response.NuritionResponseItem
import com.pukimen.babygrowth.data.remote.response.PredictResponse
import com.pukimen.babygrowth.data.remote.response.RecomendationResponse
import com.pukimen.babygrowth.data.remote.response.UpdateProfileResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*
interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<AuthResponse>

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

    @FormUrlEncoded
    @POST("profile")
    fun profile(
        @Header("Authorization") token: String,
        @Field("name") name: String,
        @Field("birthday") birthday: String,
        @Field("height") height: Int,
        @Field("weight") weight: Int,
        @Field("gender") gender: String,
    ): Call<UpdateProfileResponse>

    @GET("recipe/{id}")
    fun getDetailRecipe(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): Call<DetailRecipeResponse>

    @GET("recipe")
    fun getRecipe(
        @Header("Authorization") token: String,
    ): Call<AllRecipeResponse>

    @GET("recipe/search/{name}")
    fun getSearchRecipe(
        @Header("Authorization") token: String,
        @Path("name") name: String,
    ): Call<AllRecipeResponse>

    @Multipart
    @POST("predict")
    fun uploadImage(
        @Header("Authorization") token: String,
        @Part image : MultipartBody.Part,
    ): Call<PredictResponse>
}