package com.pukimen.babygrowth.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pukimen.babygrowth.data.repository.AuthRepository
import com.pukimen.babygrowth.data.repository.NutritionRepository
import com.pukimen.babygrowth.data.repository.RecipeRepository
import com.pukimen.babygrowth.data.repository.RecomendationRepository
import com.pukimen.babygrowth.data.repository.ScanRepository
import com.pukimen.babygrowth.di.Injection
import com.pukimen.babygrowth.ui.auth.AuthViewModel
import com.pukimen.babygrowth.ui.bottomNav.FoodViewModel
import com.pukimen.babygrowth.ui.bottomNav.RecomendationViewModel

class ViewModelFactory private constructor(private val loginRepository: AuthRepository,private val nutritionRepository: NutritionRepository,application: Application,private val recomendationRepository: RecomendationRepository,private val recipeRepository: RecipeRepository,private val scanRepository: ScanRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(loginRepository) as T
        }else  if (modelClass.isAssignableFrom(FoodViewModel::class.java)) {
            return FoodViewModel(nutritionRepository) as T
        }else  if (modelClass.isAssignableFrom(RecomendationViewModel::class.java)) {
            return RecomendationViewModel(recomendationRepository) as T
        }else  if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
            return RecipeViewModel(recipeRepository) as T
        }else  if (modelClass.isAssignableFrom(ScanViewModel::class.java)) {
            return ScanViewModel(scanRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }




    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context,application: Application): ViewModelFactory =
            instance ?: synchronized(this) {
                val authRepository = Injection.provideAuthRepository(context)
                val recomendationRepository = Injection.provideRecomndationRepository(context)
                val nutritionRepository = Injection.provideNutritionRepository(context,application)
                val recipeRepository = Injection.provideRecipeRepository(context)
                val scanRepository = Injection.provideScanRepository(context)
                instance ?: ViewModelFactory(authRepository,nutritionRepository,application, recomendationRepository,recipeRepository,scanRepository
                )
            }.also { instance = it }
    }
}