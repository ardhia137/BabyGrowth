package com.pukimen.babygrowth.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.pukimen.babygrowth.R
import com.pukimen.babygrowth.databinding.ActivityDetailMealPlanerBinding
import com.pukimen.babygrowth.ui.auth.AuthViewModel
import com.pukimen.babygrowth.ui.bottomNav.FoodViewModel
import com.pukimen.babygrowth.ui.bottomNav.MealPlanerAdapter
import com.pukimen.babygrowth.utils.DateHelper
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class DetailMealPlaner : AppCompatActivity() {
    private lateinit var binding: ActivityDetailMealPlanerBinding
    private lateinit var adapter: MealPlanerAdapter
    var umur = 0


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMealPlanerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val eatTime = intent.getStringExtra(EAT_TIME)
        binding.topAppBar.title = eatTime
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this, application)
        val viewModel: FoodViewModel by viewModels { factory }
        val authViewModel: AuthViewModel by viewModels { factory }

        adapter = MealPlanerAdapter(viewModel)
        authViewModel.getSession().observe(this){user ->
            umur = calculateAgeInMonths(user.birthDay).toInt()
            viewModel.getAllNutritionByEat(eatTime!!).observe(this) { nutritionList ->
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

                    var kategori = when {
                        umur <= 6 -> 1
                        umur <= 11 -> 2
                        else -> 3
                    }
                    viewModel.getNutritionDay(kategori).observe(this, Observer { nutrition_day ->
                        if (nutrition_day != null) {
                            val totalFatInt = totalFat.toInt()
                            val totalProteinInt = totalProtein.toInt()
                            val totalCarboInt = totalCarbo.toInt()
                            val totalCaloriesInt = totalCalories.toInt()

                            val rFatInt = nutrition_day.fat.toInt()
                            val rProteinInt = nutrition_day.protein.toInt()
                            val rCarboInt = nutrition_day.carbohydrates?.toInt()
                            val rCaloriesInt = nutrition_day.calories.toInt()

                            binding.tvFat.text = "$totalFatInt"
                            binding.tvRFat.text = nutrition_day.fat
                            binding.tvProtein.text = "$totalProteinInt"
                            binding.tvRProtein.text = nutrition_day.protein
                            binding.tvCarbo.text = "$totalCarboInt"
                            binding.tvRCarbo.text = nutrition_day.carbohydrates
                            binding.tvCalories.text = "$totalCaloriesInt"
                            binding.tvRCalories.text = nutrition_day.calories

                            if (totalFatInt > rFatInt) {
                                binding.tvFat.setTextColor(ContextCompat.getColor(this, R.color.red))
                            }

                            if (totalProteinInt > rProteinInt) {
                                binding.tvProtein.setTextColor(ContextCompat.getColor(this, R.color.red))
                            }

                            if (totalCarboInt > rCarboInt!!) {
                                binding.tvCarbo.setTextColor(ContextCompat.getColor(this, R.color.red))
                            }

                            if (totalCaloriesInt > rCaloriesInt) {
                                binding.tvCalories.setTextColor(ContextCompat.getColor(this, R.color.red))
                            }
                        }

                    })

                    if (nutritionList.isNotEmpty()){
                        binding.info.visibility = View.INVISIBLE
                    }
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
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateAgeInMonths(birthday: String?): Long {
        return if (!birthday.isNullOrEmpty()) {
            try {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val birthDate = LocalDate.parse(birthday, formatter)
                val currentDate = LocalDate.now()
                ChronoUnit.MONTHS.between(birthDate, currentDate)
            } catch (e: Exception) {
                // Log the error if needed
                0
            }
        } else {
            0
        }
    }
    companion object{
        const val EAT_TIME = "eat_time"
        const val UMUR = 0
    }
}