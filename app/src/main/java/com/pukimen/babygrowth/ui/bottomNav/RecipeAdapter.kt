package com.pukimen.babygrowth.ui.bottomNav

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pukimen.babygrowth.R
import com.pukimen.babygrowth.data.database.Nutrition
import com.pukimen.babygrowth.data.model.RecipeModel
import com.pukimen.babygrowth.databinding.ItemRowRecipeBinding


class RecipeAdapter() : ListAdapter<RecipeModel, RecipeAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val recipe = getItem(position)
        holder.bind(recipe)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailRecipe::class.java)
            intent.putExtra(DetailRecipe.EXTRA_ID, recipe.id)
            holder.itemView.context.startActivity(intent)
        }

    }

    class MyViewHolder(val binding: ItemRowRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        private val image: ImageView = itemView.findViewById(R.id.image)
        private val title: TextView = itemView.findViewById(R.id.title)
        private val duration: TextView = itemView.findViewById(R.id.timeRecipe)
        private val calories: TextView = itemView.findViewById(R.id.caloriesRecipe)

        fun bind(recipe: RecipeModel) {

            title.text = recipe.name
            duration.text = "${recipe.porsi} min"
            calories.text = String.format("%.2f cal", recipe.nutrisi.kalori)
            Glide.with(binding.root.context)
                .load("https://storage.googleapis.com/babygrowth-bucket/recipe-images/${recipe.id}.png")
                .apply(RequestOptions().error(R.drawable.i_no_image))
                .into(binding.image)

        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RecipeModel>() {
            override fun areItemsTheSame(oldItem: RecipeModel, newItem: RecipeModel): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: RecipeModel, newItem: RecipeModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}
//class RecipeAdapter(private val recipes: List<Recipe>) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_recipe, parent, false)
//        return RecipeViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
//        val recipe = recipes[position]
//        holder.bind(recipe)
//        holder.itemView.setOnClickListener {
//            val intent = Intent(holder.itemView.context, DetailRecipe::class.java)
//            holder.itemView.context.startActivity(intent)
//        }
//    }
//
//    override fun getItemCount() = recipes.size
//
//    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val image: ImageView = itemView.findViewById(R.id.image)
//        private val title: TextView = itemView.findViewById(R.id.title)
//        private val duration: TextView = itemView.findViewById(R.id.timeRecipe)
//        private val calories: TextView = itemView.findViewById(R.id.caloriesRecipe)
//
//        fun bind(recipe: Recipe) {
//            image.setImageResource(recipe.imageResId)
//            title.text = recipe.title
//            duration.text = "${recipe.duration} min"
//            calories.text = "${recipe.calories} cal"
//        }
//    }
//}
//
//
//data class Recipe(val imageResId: Int, val title: String, val duration: Int, val calories: Int)