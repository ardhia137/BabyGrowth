package com.pukimen.babygrowth.ui.bottomNav

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pukimen.babygrowth.data.database.Nutrition
import com.pukimen.babygrowth.data.database.NutritionDay
import com.pukimen.babygrowth.data.repository.NutritionRepository

class FoodViewModel(private val nutritionRepository: NutritionRepository) : ViewModel() {
//    private val mNutritionRepository: NutritionRepository = NutritionRepository(application)
    fun getAllNutrition(): LiveData<List<Nutrition>> = nutritionRepository.getAllNutritionDb()
    fun getAllNutritionByEat(eat:String): LiveData<List<Nutrition>> = nutritionRepository.getAllNutritionByEatDb(eat)
    fun getNutritionDay(id:Int): LiveData<NutritionDay> = nutritionRepository.getNutritionDay(id)

    fun insert(nutrition: Nutrition) {
        nutritionRepository.insertNutritionDb(nutrition)
    }
    fun update(nutrition: Nutrition) {
        nutritionRepository.updateNutritionDb(nutrition)
    }
    fun delete(nutrition: Nutrition) {
        nutritionRepository.deleteNutritionDb(nutrition)
    }
    fun getNutrition(query:String) = nutritionRepository.getNutrition(query)
}