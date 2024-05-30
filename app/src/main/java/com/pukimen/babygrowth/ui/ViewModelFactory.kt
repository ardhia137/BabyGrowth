package com.pukimen.babygrowth.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pukimen.babygrowth.data.repository.AuthRepository
import com.pukimen.babygrowth.data.repository.NutritionRepository
import com.pukimen.babygrowth.di.Injection
import com.pukimen.babygrowth.ui.auth.AuthViewModel
import com.pukimen.babygrowth.ui.ui.FoodViewModel

class ViewModelFactory private constructor(private val loginRepository: AuthRepository,private val nutritionRepository: NutritionRepository,application: Application) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(loginRepository) as T
        }else  if (modelClass.isAssignableFrom(FoodViewModel::class.java)) {
            return FoodViewModel(nutritionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }




    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context,application: Application): ViewModelFactory =
            instance ?: synchronized(this) {
                val authRepository = Injection.provideAuthRepository(context)
                val nutritionRepository = Injection.provideNutritionRepository(context,application)
                instance ?: ViewModelFactory(authRepository,nutritionRepository,application
                )
            }.also { instance = it }
    }
}