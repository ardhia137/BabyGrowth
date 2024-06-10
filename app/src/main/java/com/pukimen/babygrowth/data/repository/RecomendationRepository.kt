package com.pukimen.babygrowth.data.repository

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.pukimen.babygrowth.data.model.RecomendationModel
import com.pukimen.babygrowth.data.remote.response.RecomendationResponse
import com.pukimen.babygrowth.data.remote.response.RekomendasiItem
import com.pukimen.babygrowth.data.remote.retrofit.ApiService
import com.pukimen.babygrowth.utils.Results
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecomendationRepository private constructor(
    private val apiService: ApiService,
){
    private val results = MediatorLiveData<Results<List<RecomendationModel>>>()
    fun getRecomendation(query: String,): LiveData<Results<List<RecomendationModel>>> {
        results.value = Results.Loading
        val client = apiService.getRecomendation(query = query)
        client.enqueue(object : Callback<RecomendationResponse> {
            override fun onResponse(call: Call<RecomendationResponse>, response: Response<RecomendationResponse>) {
                Log.e(ContentValues.TAG, "response")
                if (response.isSuccessful) {
                    val rekom = response.body()?.rekomendasi
                    val listrekomModel = rekom?.mapNotNull { mapToRekomendasiItem(it) }
                    results.value = Results.Success(listrekomModel ?: emptyList())
                } else {
                    Log.e(ContentValues.TAG, "onFailurel: ${response.errorBody().toString()}")
                    Log.e(ContentValues.TAG, "onFailurel: ${response.code()}")
                    results.value = Results.Error(response.message())
                }
            }

            override fun onFailure(call: Call<RecomendationResponse>, t: Throwable) {
                results.value = Results.Error(t.message.toString())
                Log.e(ContentValues.TAG, t.message.toString())
            }
        })
        return results
    }
    private fun mapToRekomendasiItem(rekomendasiItem: RekomendasiItem?): RecomendationModel? {
        return rekomendasiItem?.let {
            RecomendationModel(
                it.idResep!!,
                it.namaResep!!,
                it.gambar!!,
                it.kalori!!,
                it.karbo!!,
                it.lemak!!,
                it.protein!!,
                it.kategori!!,
                it.porsi!!,
                it.similarityScore!!,
            )
        }
    }
    companion object {
        @Volatile
        private var instance: RecomendationRepository? = null

        fun getInstance(
            apiService: ApiService,
        ): RecomendationRepository =
            instance ?: synchronized(this) {
                instance ?: RecomendationRepository(apiService)
            }.also { instance = it }
    }
}