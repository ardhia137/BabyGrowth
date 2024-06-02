package com.pukimen.babygrowth.data.repository

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.pukimen.babygrowth.data.database.BabyRoomDatabase
import com.pukimen.babygrowth.data.database.Nutrition
import com.pukimen.babygrowth.data.database.NutritionDao
import com.pukimen.babygrowth.data.database.NutritionDay
import com.pukimen.babygrowth.data.remote.response.NuritionResponseItem
import com.pukimen.babygrowth.data.remote.retrofit.ApiService
import com.pukimen.babygrowth.utils.Results
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class NutritionRepository private constructor(
    private val apiService: ApiService,
    application: Application
) {
    private val results = MediatorLiveData<Results<List<NuritionResponseItem>>>()
    private val mNutritionDao: NutritionDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    init {
        val applicationScope = CoroutineScope(SupervisorJob())
        val db = BabyRoomDatabase.getDatabase(application, applicationScope)
        mNutritionDao = db.nutritionDao()
    }
    fun getAllNutritionDb(): LiveData<List<Nutrition>> = mNutritionDao.getAllNutrition()
     fun getNutritionDay(id:Int,): LiveData<NutritionDay> = mNutritionDao.getNutririonDay(id, )



    fun getAllNutritionByEatDb(eat:String): LiveData<List<Nutrition>> = mNutritionDao.getNutritionByEat(eat)
    fun insertNutritionDb(nutrition: Nutrition) {
        executorService.execute { mNutritionDao.insert(nutrition) }
    }
    fun deleteNutritionDb(nutrition: Nutrition) {
        executorService.execute { mNutritionDao.delete(nutrition) }
    }
    fun updateNutritionDb(nutrition: Nutrition) {
        executorService.execute { mNutritionDao.update(nutrition) }
    }

    fun getNutrition(query: String,): LiveData<Results<List<NuritionResponseItem>>> {

        results.value = Results.Loading
        val client = apiService.getNutrition(query = query)
        client.enqueue(object : Callback<List<NuritionResponseItem>> {
            override fun onResponse(call: Call<List<NuritionResponseItem>>, response: Response<List<NuritionResponseItem>>) {
                Log.e(ContentValues.TAG, "response")
                if (response.isSuccessful) {
                    val nutritions = response.body()
                    Log.e(ContentValues.TAG, "onSuccess: ${nutritions}")
                    results.value = Results.Success(nutritions!!)
                } else {
                    Log.e(ContentValues.TAG, "onFailurel: ${response.errorBody().toString()}")
                    Log.e(ContentValues.TAG, "onFailurel: ${response.code()}")
                    results.value = Results.Error(response.message())
                }
            }

            override fun onFailure(call: Call<List<NuritionResponseItem>>, t: Throwable) {
                results.value = Results.Error(t.message.toString())
                Log.e(ContentValues.TAG, t.message.toString())
            }
        })
        return results
    }

    companion object {
        @Volatile
        private var instance: NutritionRepository? = null

        fun getInstance(
            apiService: ApiService,
            application: Application
        ): NutritionRepository =
            instance ?: synchronized(this) {
                instance ?: NutritionRepository(apiService,application)
            }.also { instance = it }
    }
}