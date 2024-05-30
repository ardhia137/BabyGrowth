package com.pukimen.babygrowth.ui.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.room.util.query
import com.pukimen.babygrowth.data.database.NutritionDay
import com.pukimen.babygrowth.databinding.FragmentHomeBinding
import com.pukimen.babygrowth.ui.AddFoodActivity
import com.pukimen.babygrowth.ui.DetailMealPlaner
import com.pukimen.babygrowth.ui.ViewModelFactory
import com.pukimen.babygrowth.ui.ui.FoodViewModel
import com.pukimen.babygrowth.utils.DateHelper
import java.util.Date

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    val umur = 12

    var kategori = 1
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext(), requireActivity().application)

        val viewModel: FoodViewModel by viewModels { factory }

        getAllNutrition(viewModel)
        getfoodtime(viewModel)


        binding.cardBreakfast.setOnClickListener {
            val intent = Intent(requireContext(), DetailMealPlaner::class.java)
            intent.putExtra(DetailMealPlaner.EAT_TIME,"Breakfast")
            startActivity(intent)
        }

        binding.cardLunch.setOnClickListener {
            val intent = Intent(requireContext(), DetailMealPlaner::class.java)
            intent.putExtra(DetailMealPlaner.EAT_TIME,"Lunch")
            startActivity(intent)
        }

        binding.cardDinner.setOnClickListener {
            val intent = Intent(requireContext(), DetailMealPlaner::class.java)
            intent.putExtra(DetailMealPlaner.EAT_TIME,"Dinner")
            startActivity(intent)
        }


        return root
    }



    fun getAllNutrition(viewModel:FoodViewModel){
        viewModel.getAllNutrition().observe(viewLifecycleOwner, Observer { nutritionList ->
            if (nutritionList != null) {
                val currentDate = DateHelper.getCurrentDate() // Mendapatkan tanggal saat ini
                val filteredNutritionList = nutritionList.filter { it.date == currentDate }

                val totalCalories = filteredNutritionList.map { it.calories?.toDouble() ?: 0.0 }.sum()
                val totalProtein = filteredNutritionList.map { it.protein?.toDouble() ?: 0.0 }.sum()
                val totalCarbo = filteredNutritionList.map { it.carbohydrates?.toDouble() ?: 0.0 }.sum()
                val totalFat = filteredNutritionList.map { it.fat?.toDouble() ?: 0.0 }.sum()
                Log.d("HomeFragment", "Total Calories: $totalCalories")
                var kategori = 0
                if (umur <= 6){
                    kategori = 1
                }else if (umur <= 11){
                    kategori = 2
                }else if (umur >= 12){
                    kategori = 3
                }
                viewModel.getNutritionDay(kategori).observe(viewLifecycleOwner, Observer { nutrition_day ->
                    if (nutrition_day != null) {

                        binding.tvFat.text = "Fat: ${String.format("%.2f", totalFat)} / ${nutrition_day.fat} G"
                        binding.tvProtein.text = "Protein: ${String.format("%.2f", totalProtein)} / ${nutrition_day.protein} G"
                        binding.tvCarbo.text = "Carbo: ${String.format("%.2f", totalCarbo)} / ${nutrition_day.carbohydrates} G"
                        binding.tvCalories.text = "Calories: ${String.format("%.2f", totalCalories)} / ${nutrition_day.calories} Cal"
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

    fun getfoodtime(viewModel: FoodViewModel){
        viewModel.getAllNutritionByEat("Breakfast").observe(viewLifecycleOwner, Observer { nutritionList ->
            if (nutritionList != null) {
                val currentDate = DateHelper.getCurrentDate() // Mendapatkan tanggal saat ini
                val filteredNutritionList = nutritionList.filter { it.date == currentDate }

                val totalCalories = filteredNutritionList.map { it.calories?.toDouble() ?: 0.0 }.sum()
                        binding.tvBreakfast.text = "${String.format("%.2f", totalCalories)} Cal"
            }
        })
        viewModel.getAllNutritionByEat("Lunch").observe(viewLifecycleOwner, Observer { nutritionList ->
            if (nutritionList != null) {
                val currentDate = DateHelper.getCurrentDate() // Mendapatkan tanggal saat ini
                val filteredNutritionList = nutritionList.filter { it.date == currentDate }

                val totalCalories = filteredNutritionList.map { it.calories?.toDouble() ?: 0.0 }.sum()
                binding.tvLunch.text = "${String.format("%.2f", totalCalories)} Cal"
            }
        })
        viewModel.getAllNutritionByEat("Dinner").observe(viewLifecycleOwner, Observer { nutritionList ->
            if (nutritionList != null) {
                val currentDate = DateHelper.getCurrentDate() // Mendapatkan tanggal saat ini
                val filteredNutritionList = nutritionList.filter { it.date == currentDate }

                val totalCalories = filteredNutritionList.map { it.calories?.toDouble() ?: 0.0 }.sum()
                binding.tvDinner.text = "${String.format("%.2f", totalCalories)} Cal"
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}