package com.pukimen.babygrowth.ui.bottomNav

import InstructionAdapter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pukimen.babygrowth.R
import com.pukimen.babygrowth.data.model.Bahan
import com.pukimen.babygrowth.data.model.Langkah
import com.pukimen.babygrowth.databinding.ActivityDetailRecipeBinding
import com.pukimen.babygrowth.ui.RecipeViewModel
import com.pukimen.babygrowth.ui.ViewModelFactory
import com.pukimen.babygrowth.ui.auth.AuthViewModel
import com.pukimen.babygrowth.utils.Results

class DetailRecipe : AppCompatActivity() {
    private lateinit var binding: ActivityDetailRecipeBinding
    private lateinit var adapter: RecomendationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this, application)
        val recipeViewModel: RecipeViewModel by viewModels { factory }
        val authViewModel: AuthViewModel by viewModels { factory }
        val rviewModel: RecomendationViewModel by viewModels { factory }
        val id = intent.getStringExtra(EXTRA_ID)

        authViewModel.getSession().observe(this) {
            if (it != null) {
                recipeViewModel.getDetailRecipe(it.token, id!!).observe(this) { results ->
                    if (results != null) {
                        when (results) {
                            is Results.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }
                            is Results.Success -> {
                                var kategori = ""
                                if (results.data.kategori == 0){
                                    kategori = "6-8 Bulan"
                                }else if (results.data.kategori == 1){
                                    kategori = "9-11 Bulan"
                                }else{
                                    kategori = "12 Bulan Keatas"
                                }
                                binding.progressBar.visibility = View.GONE
                                val detailRecipe = results.data
                                Log.d("DetailRecipe", "onCreate: $detailRecipe")
                                binding.recipeName.text = detailRecipe.name
                                binding.tvCalories.text = String.format("%.1f", detailRecipe.nutrisi.kalori)
                                binding.tvProtein.text = String.format("%.1f", detailRecipe.nutrisi.protein)
                                binding.tvFat.text = String.format("%.1f", detailRecipe.nutrisi.lemak)
                                binding.tvCarbo.text = String.format("%.1f", detailRecipe.nutrisi.karbohidrat)
                                binding.recipeTime.text = "${detailRecipe.porsi} Serving"
                                binding.kategori.text = kategori
                                setupIngredientRecyclerView(detailRecipe.bahan)
                                setupInstructionRecyclerView(detailRecipe.langkah)
                                Glide.with(binding.root.context)
                                    .load("https://storage.googleapis.com/babygrowth-bucket/recipe-images/${detailRecipe.id}.png")
                                    .apply(RequestOptions().error(R.drawable.i_no_image))
                                    .into(binding.ivDetailRecipe)

                            }
                            is Results.Error -> {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(
                                    this@DetailRecipe,
                                    "Terjadi kesalahan: ${results.error}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
        getRecomendation(rviewModel, id!!)
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    private fun getRecomendation(viewModel: RecomendationViewModel, id: String) {
        adapter = RecomendationAdapter()
        viewModel.getRecomendation(id).observe(this) { results ->
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
                    Log.e("DetailRecipe", "Error: ${results.error}")
                    Toast.makeText(this, "Terjadi kesalahan: ${results.error}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.listRecomendation.layoutManager = layoutManager
        binding.listRecomendation.adapter = adapter
    }
    private fun setupIngredientRecyclerView(ingredients: List<Bahan>) {
        val adapter = IngredientAdapter(ingredients)
        binding.rvIngredients.layoutManager = LinearLayoutManager(this)
        binding.rvIngredients.adapter = adapter
    }

    private fun setupInstructionRecyclerView(instructions: List<Langkah>) {
        val adapter = InstructionAdapter(instructions)
        binding.rvInstruction.layoutManager = LinearLayoutManager(this)
        binding.rvInstruction.adapter = adapter
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }
}
