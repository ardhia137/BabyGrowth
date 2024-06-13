package com.pukimen.babygrowth.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.pukimen.babygrowth.databinding.ActivityAddFoodBinding
import com.pukimen.babygrowth.ui.bottomNav.FoodAdapter
import com.pukimen.babygrowth.ui.bottomNav.FoodViewModel
import com.pukimen.babygrowth.utils.Results

class AddFoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddFoodBinding
    private lateinit var adapter: FoodAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this, application)
        val viewModel: FoodViewModel by viewModels { factory }

        val eatTime = intent.getStringExtra(EAT_TIME)
        adapter = FoodAdapter(viewModel,eatTime.toString())

        // Setup search functionality
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.toolbar.navigationIcon = null
            searchView.editText.setOnEditorActionListener { textView, actionId, event ->
                searchBar.setText(searchView.text)
                searchView.hide()
                val query = searchView.text.toString()
                if (query.isNotEmpty()) {
                    viewModel.getNutrition(query).observe(this@AddFoodActivity) { results ->
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
                                    binding.listFood.visibility = View.GONE
                                }else{
                                    adapter.submitList(newsData)
                                    binding.listFood.visibility = View.VISIBLE
                                }
                                Log.e("jancok", "${results.data}")
                            }

                            is Results.Error -> {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(
                                    this@AddFoodActivity,
                                    "Terjadi kesalahan: ${results.error}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
                false
            }
        }

        val layoutManager = LinearLayoutManager(this)
        binding.listFood.layoutManager = layoutManager
        binding.listFood.adapter = adapter
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    companion object{
        const val EAT_TIME = "eat_time"
    }
}
