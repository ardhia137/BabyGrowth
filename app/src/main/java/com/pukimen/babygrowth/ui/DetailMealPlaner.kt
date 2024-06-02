package com.pukimen.babygrowth.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.pukimen.babygrowth.databinding.ActivityDetailMealPlanerBinding
import com.pukimen.babygrowth.ui.bottomNav.FoodViewModel
import com.pukimen.babygrowth.ui.bottomNav.MealPlanerAdapter
import com.pukimen.babygrowth.utils.DateHelper

class DetailMealPlaner : AppCompatActivity() {
    private lateinit var binding: ActivityDetailMealPlanerBinding
    private lateinit var adapter: MealPlanerAdapter
    val umur = 12

    var kategori = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMealPlanerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val eatTime = intent.getStringExtra(EAT_TIME)
        binding.topAppBar.title = eatTime
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this, application)
        val viewModel: FoodViewModel by viewModels { factory }
        adapter = MealPlanerAdapter(viewModel)
        viewModel.getAllNutritionByEat(eatTime!!).observe(this) {
                nutritionList ->
            if (nutritionList != null) {
                val currentDate = DateHelper.getCurrentDate() // Mendapatkan tanggal saat ini
                val filteredNutritionList = nutritionList.filter { it.date == currentDate }
                binding.listDetail.adapter = adapter
                adapter.submitList(filteredNutritionList)
                val totalCalories = filteredNutritionList.map { it.calories?.toDouble() ?: 0.0 }.sum()
                val totalProtein = filteredNutritionList.map { it.protein?.toDouble() ?: 0.0 }.sum()
                val totalCarbo = filteredNutritionList.map { it.carbohydrates?.toDouble() ?: 0.0 }.sum()
                val totalFat = filteredNutritionList.map { it.fat?.toDouble() ?: 0.0 }.sum()
                Log.d("HomeFragment", "Total Calories: $totalCalories")

                if (umur <= 6){
                    kategori = 1
                }else if (umur <= 11){
                    kategori = 2
                }else if (umur >= 12){
                    kategori = 3
                }
                viewModel.getNutritionDay(kategori).observe(this, Observer { nutrition_day ->
                    if (nutrition_day != null) {
                        binding.tvFat.text = "Fat: ${String.format("%.2f", totalFat)} / ${nutrition_day.fat} G"
                        binding.tvProtein.text = "Protein: ${String.format("%.2f", totalProtein)} / ${nutrition_day.protein} G"
                        binding.tvCarbo.text = "Carbo: ${String.format("%.2f", totalCarbo)} / ${nutrition_day.carbohydrates} G"
                        binding.tvCalories.text = "Calories: ${String.format("%.2f", totalCalories)} / ${nutrition_day.calories} Cal"
                    }
                })

                if (nutritionList.isNotEmpty()){
                    binding.info.visibility = View.INVISIBLE
                }
            }

        }
        binding.addButton.setOnClickListener {
            val intent = Intent(this, AddFoodActivity::class.java)
            intent.putExtra(AddFoodActivity.EAT_TIME, eatTime)
            startActivity(intent)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.listDetail.layoutManager = layoutManager
        binding.listDetail.adapter = adapter
    }
    companion object{
        const val EAT_TIME = "eat_time"
    }
}