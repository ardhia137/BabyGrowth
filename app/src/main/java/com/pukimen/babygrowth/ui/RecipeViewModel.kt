package com.pukimen.babygrowth.ui

import androidx.lifecycle.ViewModel
import com.pukimen.babygrowth.data.repository.RecipeRepository
import com.pukimen.babygrowth.data.repository.RecomendationRepository

class RecipeViewModel (private val recipeRepository: RecipeRepository) :  ViewModel() {
    fun getDetailRecipe(token:String,id:String) = recipeRepository.getDetailRecipe(token,id)
    fun getRecipe(token:String) = recipeRepository.getRecipe(token)
    fun getSearchRecipe(token:String,name:String) = recipeRepository.getSearchRecipe(token,name)
}