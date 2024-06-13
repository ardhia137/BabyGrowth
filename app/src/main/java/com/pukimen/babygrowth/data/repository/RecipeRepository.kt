package com.pukimen.babygrowth.data.repository

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.pukimen.babygrowth.data.model.Bahan
import com.pukimen.babygrowth.data.model.Langkah
import com.pukimen.babygrowth.data.model.Nutrisi
import com.pukimen.babygrowth.data.model.RecipeModel
import com.pukimen.babygrowth.data.model.RecomendationModel
import com.pukimen.babygrowth.data.remote.response.AllRecipeResponse
import com.pukimen.babygrowth.data.remote.response.DataItemAll
import com.pukimen.babygrowth.data.remote.response.DataRecipe
import com.pukimen.babygrowth.data.remote.response.DetailRecipeResponse
import com.pukimen.babygrowth.data.remote.response.RecomendationResponse
import com.pukimen.babygrowth.data.remote.retrofit.ApiService
import com.pukimen.babygrowth.ui.bottomNav.DetailRecipe
import com.pukimen.babygrowth.utils.Results
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecipeRepository private constructor(
    private val apiService: ApiService,
) {
    private val results = MediatorLiveData<Results<RecipeModel>>()
    private val resultsAll = MediatorLiveData<Results<List<RecipeModel>>>()
    fun getDetailRecipe(token: String, id: String): LiveData<Results<RecipeModel>> {
        results.value = Results.Loading
        Log.e("jancok", id)
        val client = apiService.getDetailRecipe("Bearer $token",id)
        client.enqueue(object : Callback<DetailRecipeResponse> {
            override fun onResponse(call: Call<DetailRecipeResponse>, response: Response<DetailRecipeResponse>) {
                Log.e(ContentValues.TAG, "response")
                if (response.isSuccessful) {

                    val recipeModel = response.body()?.data
                    Log.d("jumlah", recipeModel?.bahan.toString())
                    val listrekomModel =  mapToRecipeModel(recipeModel!!)
                    results.value = Results.Success(listrekomModel)

                } else {
                    Log.e(ContentValues.TAG, "onFailurel: ${response.errorBody().toString()}")
                    Log.e(ContentValues.TAG, "onFailurel: ${response.code()}")
                    results.value = Results.Error(response.message())
                }
            }

            override fun onFailure(call: Call<DetailRecipeResponse>, t: Throwable) {
                results.value = Results.Error(t.message.toString())
                Log.e(ContentValues.TAG, t.toString())
            }
        })
        return results
    }

    fun getRecipe(token: String): LiveData<Results<List<RecipeModel>>> {
        resultsAll.value = Results.Loading
        val client = apiService.getRecipe("Bearer $token")
        client.enqueue(object : Callback<AllRecipeResponse> {
            override fun onResponse(call: Call<AllRecipeResponse>, response: Response<AllRecipeResponse>) {
                Log.e(ContentValues.TAG, "response")
                if (response.isSuccessful) {
                    val recipe = response.body()?.data
                    val recipeModel = recipe?.mapNotNull { mapToRecipeModel2(it!!) }
                    resultsAll.value = Results.Success(recipeModel?: emptyList())

                } else {
                    Log.e(ContentValues.TAG, "onFailurel: ${response.errorBody().toString()}")
                    Log.e(ContentValues.TAG, "onFailurel: ${response.code()}")
                    resultsAll.value = Results.Error(response.message())
                }
            }
            override fun onFailure(call: Call<AllRecipeResponse>, t: Throwable) {
                resultsAll.value = Results.Error(t.message.toString())
                Log.e(ContentValues.TAG, t.toString())
            }
        })
        return resultsAll
    }

    fun getSearchRecipe(token: String,name: String): LiveData<Results<List<RecipeModel>>> {
        resultsAll.value = Results.Loading
        val client = apiService.getSearchRecipe("Bearer $token",name)
        client.enqueue(object : Callback<AllRecipeResponse> {
            override fun onResponse(call: Call<AllRecipeResponse>, response: Response<AllRecipeResponse>) {
                Log.e(ContentValues.TAG, "response")
                if (response.isSuccessful) {
                    val recipe = response.body()?.data
                    val recipeModel = recipe?.mapNotNull { mapToRecipeModel2(it!!) }
                    resultsAll.value = Results.Success(recipeModel?: emptyList())

                } else {
                    Log.e(ContentValues.TAG, "onFailurel: ${response.errorBody().toString()}")
                    Log.e(ContentValues.TAG, "onFailurel: ${response.code()}")
                    resultsAll.value = Results.Error(response.message())
                }
            }
            override fun onFailure(call: Call<AllRecipeResponse>, t: Throwable) {
                resultsAll.value = Results.Error(t.message.toString())
                Log.e(ContentValues.TAG, t.toString())
            }
        })
        return resultsAll
    }

    fun mapToRecipeModel2(detailRecipeResponse: DataItemAll): RecipeModel {
        return RecipeModel(
            id = detailRecipeResponse.id.toString(),
            name = detailRecipeResponse.name.toString(),
            image = detailRecipeResponse.image.toString(),
            kategori = detailRecipeResponse.kategori ?: 0,
            porsi = detailRecipeResponse.porsi ?: 0,
            langkah = detailRecipeResponse.langkah!!.map { Langkah(it?.step?:0, it?.deskripsi.toString()) },
            bahan = detailRecipeResponse.bahan!!.map { Bahan(it?.jumlah.toString(), it?.namaBahan.toString(), it?.satuan.toString(), it?.idBahan.toString()) },
            nutrisi = Nutrisi(
                kalori = detailRecipeResponse.nutrisi?.kalori?.toString()?.toDouble() ?: 0.0,
                karbohidrat = detailRecipeResponse.nutrisi?.karbohidrat?.toString()?.toDouble() ?: 0.0,
                gula = detailRecipeResponse.nutrisi?.gula?.toString()?.toDouble() ?: 0.0,
                protein = detailRecipeResponse.nutrisi?.protein?.toString()?.toDouble() ?: 0.0,
                serat = detailRecipeResponse.nutrisi?.serat?.toString()?.toDouble() ?: 0.0,
                natrium = detailRecipeResponse.nutrisi?.natrium?.toString()?.toDouble() ?: 0.0,
                lemak = detailRecipeResponse.nutrisi?.lemak?.toString()?.toDouble() ?: 0.0
            ),
            createdAt = detailRecipeResponse.createdAt.toString(),
            updatedAt = detailRecipeResponse.updatedAt.toString(),
        )
    }


    fun mapToRecipeModel(detailRecipeResponse: DataRecipe): RecipeModel {
        return RecipeModel(
            id = detailRecipeResponse.id.toString(),
            name = detailRecipeResponse.name.toString(),
            image = detailRecipeResponse.image.toString(),
            kategori = detailRecipeResponse.kategori ?: 0,
            porsi = detailRecipeResponse.porsi ?: 0,
            langkah = detailRecipeResponse.langkah!!.map { Langkah(it?.step?:0, it?.deskripsi.toString()) },
            bahan = detailRecipeResponse.bahan!!.map { Bahan(it?.jumlah.toString(), it?.namaBahan.toString(), it?.satuan.toString(), it?.idBahan.toString()) },
            nutrisi = Nutrisi(
                kalori = detailRecipeResponse.nutrisi?.kalori?.toString()?.toDouble() ?: 0.0,
                karbohidrat = detailRecipeResponse.nutrisi?.karbohidrat?.toString()?.toDouble() ?: 0.0,
                gula = detailRecipeResponse.nutrisi?.gula?.toString()?.toDouble() ?: 0.0,
                protein = detailRecipeResponse.nutrisi?.protein?.toString()?.toDouble() ?: 0.0,
                serat = detailRecipeResponse.nutrisi?.serat?.toString()?.toDouble() ?: 0.0,
                natrium = detailRecipeResponse.nutrisi?.natrium?.toString()?.toDouble() ?: 0.0,
                lemak = detailRecipeResponse.nutrisi?.lemak?.toString()?.toDouble() ?: 0.0
            ),
            createdAt = detailRecipeResponse.createdAt.toString(),
            updatedAt = detailRecipeResponse.updatedAt.toString(),
        )
    }




    companion object {
        @Volatile
        private var instance: RecipeRepository? = null

        fun getInstance(
            apiService: ApiService,
        ): RecipeRepository =
            instance ?: synchronized(this) {
                instance ?: RecipeRepository(apiService)
            }.also { instance = it }
    }
}