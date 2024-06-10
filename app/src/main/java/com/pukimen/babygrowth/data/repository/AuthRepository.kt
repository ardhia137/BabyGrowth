package com.pukimen.babygrowth.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.pukimen.babygrowth.utils.Results
import com.pukimen.babygrowth.data.local.UserPreference
import com.pukimen.babygrowth.data.model.UserModel
import com.pukimen.babygrowth.data.remote.response.AuthResponse
import com.pukimen.babygrowth.data.remote.response.LoginResponse
import com.pukimen.babygrowth.data.remote.response.UpdateProfileResponse
import com.pukimen.babygrowth.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AuthRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {
    private val results = MediatorLiveData<Results<UserModel>>()
    private val resultRegister = MediatorLiveData<Results<AuthResponse>>()
    private val resultUpdate = MediatorLiveData<Results<UpdateProfileResponse>>()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun login(email: String, password: String): LiveData<Results<UserModel>> {
        results.value = Results.Loading
        val client = apiService.login(email = email, password = password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val user = response.body()?.data
                    if (user != null) {
                        val userModel = UserModel(
                            email = user.email ?: "",
                            name = user.name ?: "",
                            birthDay = user.birthday ?: "",
                            height = user.height ?: 0,
                            weight = user.weight ?: 0,
                            gender = user.gender ?: "",
                            updatedAt = user.updatedAt ?: "",
                            token = user.token ?: "",
                            isLogin = true
                        )
                        coroutineScope.launch { saveSession(userModel) }
                        results.postValue(Results.Success(userModel))
                    } else {
                        results.postValue(Results.Error("No user data available"))
                    }
                } else {
                    results.postValue(Results.Error("Login failed: ${response.message()}"))
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                results.postValue(Results.Error("Network failure: ${t.message}"))
                Log.e("AuthRepository", "Login request failed: ${t.message}", t)
            }
        })
        return results
    }


    fun register(username:String, email:String,password:String): LiveData<Results<AuthResponse>> {

        resultRegister.value = Results.Loading
        val client = apiService.register(username = username,email = email, password = password)
        client.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.e(TAG, "response")
                if (response.isSuccessful) {
                    val user = response.body()
                    Log.e(TAG, "onSuccess: ${user}")

                    resultRegister.value = Results.Success(AuthResponse())
                }
                else {
                    Log.e(TAG, "onFailurel: ${response.errorBody().toString()}")
                    Log.e(TAG, "onFailurel: ${response.code()}")
                    Log.e(TAG, "onFailurel: ${email}")
                    resultRegister.value = Results.Error(response.message())
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                resultRegister.value = Results.Error("test ${t.message.toString()}")
                Log.e(TAG, t.message.toString())
            }
        })
        return resultRegister
    }

    fun update(token: String, name: String, birthDay: String, height: Int, weight: Int, gender: String): LiveData<Results<UpdateProfileResponse>> {
        resultUpdate.value = Results.Loading
        val client = apiService.profile("Bearer $token", name, birthDay, height, weight, gender)
        client.enqueue(object : Callback<UpdateProfileResponse> {
            override fun onResponse(call: Call<UpdateProfileResponse>, response: Response<UpdateProfileResponse>) {
                if (response.isSuccessful) {
                    val user = response.body()?.data
                    coroutineScope.launch {
                        val currentSession = userPreference.getSession().first() // Get current session data
                        if (user != null) {
                            val userModel = UserModel(
                                email = currentSession.email, // Retain the original email
                                name = user.name ?: "",
                                birthDay = user.birthday ?: "",
                                height = user.height ?: 0,
                                weight = user.weight ?: 0,
                                gender = user.gender ?: "",
                                updatedAt = user.updatedAt ?: "",
                                token = token,  // Use the existing token
                                isLogin = true
                            )
                            saveSession(userModel)
                        }
                    }
                    resultUpdate.postValue(Results.Success(UpdateProfileResponse()))
                } else {
                    resultUpdate.postValue(Results.Error(response.message()))
                }
            }

            override fun onFailure(call: Call<UpdateProfileResponse>, t: Throwable) {
                resultUpdate.postValue(Results.Error(t.message.toString()))
            }
        })
        return resultUpdate
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
