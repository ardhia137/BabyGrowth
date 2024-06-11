package com.pukimen.babygrowth.data.repository

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.pukimen.babygrowth.data.remote.response.PredictResponse
import com.pukimen.babygrowth.data.remote.retrofit.ApiService
import com.pukimen.babygrowth.utils.Results
import com.pukimen.babygrowth.utils.reduceFileImage
import com.pukimen.babygrowth.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ScanRepository private constructor(private val apiService: ApiService) {

    private val results = MediatorLiveData<Results<PredictResponse>>()

    fun uploadImage(token: String, imageUri: Uri, context: Context): LiveData<Results<PredictResponse>> {
        results.value = Results.Loading

        try {
            val imageFile = uriToFile(imageUri, context).reduceFileImage()
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "image",
                imageFile.name,
                requestImageFile
            )

            val client = apiService.uploadImage("Bearer $token", multipartBody)
            client.enqueue(object : Callback<PredictResponse> {
                override fun onResponse(call: Call<PredictResponse>, response: Response<PredictResponse>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            results.value = Results.Success(responseBody)
                        } else {
                            results.value = Results.Error("Response body is null")
                        }
                    } else {
                        handleErrorResponse(response)
                    }
                }

                override fun onFailure(call: Call<PredictResponse>, t: Throwable) {
                    val errorMessage = when (t) {
                        is IOException -> "Network Failure"
                        else -> "Unknown error"
                    }
                    results.value = Results.Error(t.message ?: errorMessage)
                    Log.e(ContentValues.TAG, t.message ?: errorMessage)
                }
            })
        } catch (e: Exception) {
            results.value = Results.Error("File error: ${e.message}")
        }

        return results
    }

    private fun handleErrorResponse(response: Response<PredictResponse>) {
        val errorBody = response.errorBody()?.string()
        val errorMessage = if (errorBody != null) {
            try {
                val errorResponse = Gson().fromJson(errorBody, PredictResponse::class.java)
                errorResponse.message ?: "Unknown error"
            } catch (e: Exception) {
                response.message()
            }
        } else {
            response.message()
        }
        results.value = Results.Error(errorMessage)
        Log.e(ContentValues.TAG, "Server Error: ${response.code()} $errorMessage")
    }

    companion object {
        @Volatile
        private var instance: ScanRepository? = null

        fun getInstance(apiService: ApiService): ScanRepository =
            instance ?: synchronized(this) {
                instance ?: ScanRepository(apiService)
            }.also { instance = it }
    }
}
