package com.pukimen.babygrowth.ui.bottomNav.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pukimen.babygrowth.R
import com.pukimen.babygrowth.databinding.FragmentHomeBinding
import com.pukimen.babygrowth.ui.DetailMealPlaner
import com.pukimen.babygrowth.ui.InputBabyActivity
import com.pukimen.babygrowth.ui.RecipeActivity
import com.pukimen.babygrowth.ui.ViewModelFactory
import com.pukimen.babygrowth.ui.auth.AuthViewModel
import com.pukimen.babygrowth.ui.bottomNav.FoodAdapter
import com.pukimen.babygrowth.ui.bottomNav.FoodViewModel
import com.pukimen.babygrowth.ui.bottomNav.RecomendationAdapter
import com.pukimen.babygrowth.ui.bottomNav.RecomendationViewModel
import com.pukimen.babygrowth.utils.DateHelper
import com.pukimen.babygrowth.utils.Results
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var adapter: RecomendationAdapter
    private val binding get() = _binding!!
    private var umur = 0
//    private var kategori = 1

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext(), requireActivity().application)
        val viewModel: FoodViewModel by viewModels { factory }
        val rviewModel: RecomendationViewModel by viewModels { factory }
        val authviewModel: AuthViewModel by viewModels { factory }

        authviewModel.getSession().observe(viewLifecycleOwner, Observer {
            binding.greeting.text = "Hello ${it.name},"
            umur = calculateAgeInMonths(it.birthDay).toInt()

        })

        getAllNutrition(viewModel)
        getfoodtime(viewModel)
        setupClickListeners()

        getRecomendation(rviewModel)

        return root
    }

    private fun setupClickListeners() {
        binding.tvSee.setOnClickListener {
            val intent = Intent(requireContext(), RecipeActivity::class.java)
            startActivity(intent)
        }

        binding.cardBreakfast.setOnClickListener {
            val intent = Intent(requireContext(), DetailMealPlaner::class.java)
            intent.putExtra(DetailMealPlaner.EAT_TIME, "Breakfast")
            startActivity(intent)
        }

        binding.cardLunch.setOnClickListener {
            val intent = Intent(requireContext(), DetailMealPlaner::class.java)
            intent.putExtra(DetailMealPlaner.EAT_TIME, "Lunch")
            startActivity(intent)
        }

        binding.cardDinner.setOnClickListener {
            val intent = Intent(requireContext(), DetailMealPlaner::class.java)
            intent.putExtra(DetailMealPlaner.EAT_TIME, "Dinner")
            startActivity(intent)
        }
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
    private fun getRecomendation(viewModel: RecomendationViewModel) {
        adapter = RecomendationAdapter()
        viewModel.getRecomendation("R1").observe(viewLifecycleOwner) { results ->
            when (results) {
                is Results.Loading -> {
                    // Show loading state if needed
                }

                is Results.Success -> {
                    val newsData = results.data
                    adapter.submitList(newsData)
                    Log.e("HomeFragment", "Recomendation data: ${results.data}")
                }

                is Results.Error -> {
                    Log.e("HomeFragment", "Error: ${results.error}")
                    Toast.makeText(requireContext(), "Terjadi kesalahan: ${results.error}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.listRecomendation.layoutManager = layoutManager
        binding.listRecomendation.adapter = adapter
    }

    private fun getAllNutrition(viewModel: FoodViewModel) {
        viewModel.getAllNutrition().observe(viewLifecycleOwner, Observer { nutritionList ->
            if (nutritionList != null) {
                val currentDate = DateHelper.getCurrentDate()
                val filteredNutritionList = nutritionList.filter { it.date == currentDate }

                val totalCalories = filteredNutritionList.sumOf { it.calories?.toDouble() ?: 0.0 }
                val totalProtein = filteredNutritionList.sumOf { it.protein?.toDouble() ?: 0.0 }
                val totalCarbo = filteredNutritionList.sumOf { it.carbohydrates?.toDouble() ?: 0.0 }
                val totalFat = filteredNutritionList.sumOf { it.fat?.toDouble() ?: 0.0 }

                var kategori = when {
                    umur <= 6 -> 1
                    umur <= 11 -> 2
                    else -> 3
                }

                viewModel.getNutritionDay(kategori).observe(viewLifecycleOwner, Observer { nutritionDay ->
                    if (nutritionDay != null) {
                        binding.tvFat.text = "Fat: ${String.format("%.2f", totalFat)} / ${nutritionDay.fat} G"
                        binding.tvProtein.text = "Protein: ${String.format("%.2f", totalProtein)} / ${nutritionDay.protein} G"
                        binding.tvCarbo.text = "Carbo: ${String.format("%.2f", totalCarbo)} / ${nutritionDay.carbohydrates} G"
                        binding.tvCalories.text = "Calories: ${String.format("%.2f", totalCalories)} / ${nutritionDay.calories} Cal"
                    }
                })

                nutritionList.forEach { nutrition ->
                    if (nutrition.date != currentDate) {
                        viewModel.delete(nutrition)
                    }
                }
            }
        })
    }

    private fun getfoodtime(viewModel: FoodViewModel) {
        val currentDate = DateHelper.getCurrentDate()
        observeNutritionByEat(viewModel, "Breakfast", binding.tvBreakfast)
        observeNutritionByEat(viewModel, "Lunch", binding.tvLunch)
        observeNutritionByEat(viewModel, "Dinner", binding.tvDinner)
    }

    private fun observeNutritionByEat(viewModel: FoodViewModel, eatTime: String, textView: TextView) {
        viewModel.getAllNutritionByEat(eatTime).observe(viewLifecycleOwner, Observer { nutritionList ->
            if (nutritionList != null) {
                val currentDate = DateHelper.getCurrentDate()
                val filteredNutritionList = nutritionList.filter { it.date == currentDate }
                val totalCalories = filteredNutritionList.sumOf { it.calories?.toDouble() ?: 0.0 }
                textView.text = "${String.format("%.2f", totalCalories)} Cal"
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
