package com.pukimen.babygrowth.di

import android.app.Application
import android.content.Context
import com.pukimen.babygrowth.data.local.UserPreference
import com.pukimen.babygrowth.data.repository.AuthRepository
import com.pukimen.babygrowth.data.remote.retrofit.ApiConfig
import com.pukimen.babygrowth.data.local.dataStore
import com.pukimen.babygrowth.data.repository.NutritionRepository

object Injection {
    fun provideAuthRepository(context: Context): AuthRepository {
        val apiService = ApiConfig.getApiService()
        val pref = UserPreference.getInstance(context.dataStore)

        return AuthRepository.getInstance(apiService,  pref)
    }

    fun provideNutritionRepository(context: Context,application: Application): NutritionRepository {
        val apiService = ApiConfig.getNinjaApiService()
        return NutritionRepository.getInstance(apiService,application)
    }
}
