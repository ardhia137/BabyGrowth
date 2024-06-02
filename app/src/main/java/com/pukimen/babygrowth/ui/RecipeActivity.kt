package com.pukimen.babygrowth.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pukimen.babygrowth.R
import com.pukimen.babygrowth.ui.bottomNav.Recipe
import com.pukimen.babygrowth.ui.bottomNav.RecipeAdapter

class RecipeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        val recyclerView: RecyclerView = findViewById(R.id.rvRecipe)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        val recipes = listOf(
            Recipe(R.drawable.i_labu, "Bubur Labu", 60, 70),
            Recipe(R.drawable.i_labu, "Bubur Labu", 60, 70),
            Recipe(R.drawable.i_labu, "Bubur Labu", 60, 70),
            // add more recipes
        )

        recyclerView.adapter = RecipeAdapter(recipes)
    }
}