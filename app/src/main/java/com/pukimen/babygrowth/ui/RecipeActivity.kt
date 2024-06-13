package com.pukimen.babygrowth.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pukimen.babygrowth.R
import com.pukimen.babygrowth.databinding.ActivityAddFoodBinding
import com.pukimen.babygrowth.databinding.ActivityRecipeBinding
import com.pukimen.babygrowth.ui.auth.AuthViewModel
import com.pukimen.babygrowth.ui.bottomNav.FoodAdapter
import com.pukimen.babygrowth.ui.bottomNav.FoodViewModel
import com.pukimen.babygrowth.ui.bottomNav.RecipeAdapter
import com.pukimen.babygrowth.utils.Results

class RecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeBinding
    private lateinit var adapter: RecipeAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this, application)
        val viewModel: RecipeViewModel by viewModels { factory }
        val authViewModel: AuthViewModel by viewModels { factory }
        adapter = RecipeAdapter()
        authViewModel.getSession().observe(this) { user ->
            if (user != null) {
                viewModel.getRecipe(user.token).observe(this@RecipeActivity) { results ->
                    when (results) {
                        is Results.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.info.visibility = View.GONE
                        }
                        is Results.Success -> {
                            binding.progressBar.visibility = View.GONE
                            val newsData = results.data
                            if(newsData.isEmpty()){
                                binding.info.visibility = View.VISIBLE
                                binding.rvRecipe.visibility = View.GONE
                            }else{
                                adapter.submitList(newsData)
                                binding.rvRecipe.visibility = View.VISIBLE
                            }
                            Log.e("jancok", "${results.data}")
                        }

                        is Results.Error -> {
                            binding.progressBar.visibility = View.GONE
                            if(results.error == "Not Found"){
                                binding.info.visibility = View.VISIBLE
                                binding.rvRecipe.visibility = View.GONE
                            }else{
                                Toast.makeText(
                                    this@RecipeActivity,
                                    "Terjadi kesalahan: ${results.error}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }
                    }
                }
            }
        }
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.toolbar.navigationIcon = null
            searchView.editText.setOnEditorActionListener { _,_,_ ->
                searchBar.setText(searchView.text)
                searchView.hide()
                val query = searchView.text.toString()
                if(query.isNotEmpty()) {
                    authViewModel.getSession().observe(this@RecipeActivity) { user ->
                        if (user != null) {
                            viewModel.getSearchRecipe(user.token,query).observe(this@RecipeActivity) { results ->
                                when (results) {
                                    is Results.Loading -> {
                                        binding.progressBar.visibility = View.VISIBLE
                                        binding.info.visibility = View.GONE
                                    }
                                    is Results.Success -> {
                                        binding.progressBar.visibility = View.GONE
                                        val newsData = results.data
                                        if(newsData.isEmpty()){
                                            binding.info.visibility = View.VISIBLE
                                            binding.rvRecipe.visibility = View.GONE
                                        }else{
                                            adapter.submitList(newsData)
                                            binding.rvRecipe.visibility = View.VISIBLE
                                        }
                                        Log.e("jancok", "${results.data}")
                                    }

                                    is Results.Error -> {
                                        binding.progressBar.visibility = View.GONE
                                        if(results.error == "Not Found"){
                                            binding.info.visibility = View.VISIBLE
                                            binding.rvRecipe.visibility = View.GONE
                                        }else{
                                            Toast.makeText(
                                                this@RecipeActivity,
                                                "Terjadi kesalahan: ${results.error}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
                false
            }
        }


        binding.rvRecipe.layoutManager = GridLayoutManager(this, 2)
        binding.rvRecipe.adapter = adapter
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }




}