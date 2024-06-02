package com.pukimen.babygrowth.ui.bottomNav

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pukimen.babygrowth.data.database.Nutrition
import com.pukimen.babygrowth.data.database.NutritionDay
import com.pukimen.babygrowth.data.repository.NutritionRepository
import com.pukimen.babygrowth.data.repository.RecomendationRepository

class RecomendationViewModel(private val recomendationRepository: RecomendationRepository) :  ViewModel() {
    fun getRecomendation(query:String) = recomendationRepository.getRecomendation(query)
}