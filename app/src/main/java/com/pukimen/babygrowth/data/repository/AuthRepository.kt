package com.pukimen.babygrowth.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.pukimen.babygrowth.utils.Results
import com.pukimen.babygrowth.data.local.UserPreference
import com.pukimen.babygrowth.data.model.UserModel
import com.pukimen.babygrowth.data.remote.response.LoginResponse
import com.pukimen.babygrowth.data.remote.response.RegisterResponse
import com.pukimen.babygrowth.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {
    private val results = MediatorLiveData<Results<UserModel>>()
    private val resultRegister = MediatorLiveData<Results<RegisterResponse>>()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun login(email:String,password:String): LiveData<Results<UserModel>> {

        results.value = Results.Loading
        val client = apiService.login(email = email, password = password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.e(TAG, "response")
                if (response.isSuccessful) {
                    val user = response.body()?.loginResult
                    Log.e(TAG, "onSuccess: ${user}")
                    coroutineScope.launch {
                        saveSession(UserModel(email, user?.token.toString()))
                    }
                        results.value = Results.Success(UserModel(email,user?.token.toString(),true))
                }
                else {
                    Log.e(TAG, "onFailurel: ${response.errorBody().toString()}")
                    Log.e(TAG, "onFailurel: ${response.code()}")
                    Log.e(TAG, "onFailurel: ${email}")
                    results.value = Results.Error(response.message())
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                results.value = Results.Error(t.message.toString())
                Log.e(TAG, t.message.toString())
            }
        })
        return results
    }

    fun register(name:String, email:String,password:String): LiveData<Results<RegisterResponse>> {

        resultRegister.value = Results.Loading
        val client = apiService.register(name = name,email = email, password = password)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                Log.e(TAG, "response")
                if (response.isSuccessful) {
                    val user = response.body()
                    Log.e(TAG, "onSuccess: ${user}")

                    resultRegister.value = Results.Success(RegisterResponse())
                }
                else {
                    Log.e(TAG, "onFailurel: ${response.errorBody().toString()}")
                    Log.e(TAG, "onFailurel: ${response.code()}")
                    Log.e(TAG, "onFailurel: ${email}")
                    resultRegister.value = Results.Error(response.message())
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                resultRegister.value = Results.Error(t.message.toString())
                Log.e(TAG, t.message.toString())
            }
        })
        return resultRegister
    }


    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: AuthRepository? = null

        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): AuthRepository =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(apiService, userPreference)
            }.also { instance = it }
    }
}
