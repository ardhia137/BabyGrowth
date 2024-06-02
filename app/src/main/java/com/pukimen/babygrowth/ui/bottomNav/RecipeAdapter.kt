package com.pukimen.babygrowth.ui.bottomNav

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pukimen.babygrowth.R

class RecipeAdapter(private val recipes: List<Recipe>) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_recipe, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.bind(recipe)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailRecipe::class.java)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = recipes.size

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.image)
        private val title: TextView = itemView.findViewById(R.id.title)
        private val duration: TextView = itemView.findViewById(R.id.timeRecipe)
        private val calories: TextView = itemView.findViewById(R.id.caloriesRecipe)

        fun bind(recipe: Recipe) {
            image.setImageResource(recipe.imageResId)
            title.text = recipe.title
            duration.text = "${recipe.duration} min"
            calories.text = "${recipe.calories} cal"
        }
    }
}


data class Recipe(val imageResId: Int, val title: String, val duration: Int, val calories: Int)