package com.pukimen.babygrowth.di

import android.content.Context
import com.pukimen.babygrowth.data.local.UserPreference
import com.pukimen.babygrowth.data.repository.AuthRepository
import com.pukimen.babygrowth.data.remote.retrofit.ApiConfig
import com.pukimen.babygrowth.data.local.dataStore

object Injection {
    fun provideAuthRepository(context: Context): AuthRepository {
        val apiService = ApiConfig.getApiService()
        val pref = UserPreference.getInstance(context.dataStore)

        return AuthRepository.getInstance(apiService,  pref)
    }

//    fun provideStroyRepository(context: Context): StroyRepository {
//        val apiService = ApiConfig.getApiService()
//        val pref = UserPreference.getInstance(context.dataStore)
//        return StroyRepository.getInstance(apiService,pref)
//    }
}
