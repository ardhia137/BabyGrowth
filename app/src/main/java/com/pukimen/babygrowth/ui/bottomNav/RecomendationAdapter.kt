package com.pukimen.babygrowth.ui.bottomNav

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pukimen.babygrowth.R
import com.pukimen.babygrowth.data.model.RecomendationModel
import com.pukimen.babygrowth.data.remote.response.RekomendasiItem
import com.pukimen.babygrowth.databinding.ItemRowRecipeBinding


class RecomendationAdapter() : ListAdapter<RecomendationModel, RecomendationAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val recipe = getItem(position)
        holder.bind(recipe)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailRecipe::class.java)
            intent.putExtra(DetailRecipe.EXTRA_ID, recipe.id_resep)
            holder.itemView.context.startActivity(intent)
        }
    }

    class MyViewHolder(val binding: ItemRowRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
            val cardView: CardView = binding.card
        fun bind(nutrition: RecomendationModel) {
            val layoutParams = cardView.layoutParams as ViewGroup.MarginLayoutParams

            if (position == 0) {
                layoutParams.leftMargin = (20 * itemView.context.resources.displayMetrics.density).toInt() // Convert dp to pixels
            } else {
                layoutParams.leftMargin = 0
            }
            layoutParams.width = itemView.resources.getDimensionPixelSize(R.dimen.card_width)
            cardView.layoutParams = layoutParams
            binding.title.text = nutrition.nama_resep
            binding.caloriesRecipe.text = "${nutrition.kalori} Cal"
            binding.timeRecipe.text = "${nutrition.porsi} Serving"
            Glide.with(binding.root.context)
                .load("https://storage.googleapis.com/babygrowth-bucket/recipe-images/${nutrition.id_resep}.png")
                .apply(RequestOptions().error(R.drawable.i_no_image))
                .into(binding.image)

        }

    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RecomendationModel>() {
            override fun areItemsTheSame(oldItem: RecomendationModel, newItem: RecomendationModel): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: RecomendationModel, newItem: RecomendationModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}



//class RecomendationAdapter(private val recipes: List<Recipe>) : RecyclerView.Adapter<RecomendationAdapter.RecomendationViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecomendationViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_recipe, parent, false)
//        return RecomendationViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: RecomendationViewHolder, position: Int) {
//
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
//    class RecomendationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val image: ImageView = itemView.findViewById(R.id.image)
//        private val title: TextView = itemView.findViewById(R.id.title)
//        private val duration: TextView = itemView.findViewById(R.id.timeRecipe)
//        private val calories: TextView = itemView.findViewById(R.id.caloriesRecipe)
//        private val cardView: CardView = itemView.findViewById(R.id.card)
//
//        fun bind(recipe: Recipe) {
//            val layoutParams = cardView.layoutParams
//            layoutParams.width = itemView.resources.getDimensionPixelSize(R.dimen.card_width)
//            cardView.layoutParams = layoutParams
//
//            image.setImageResource(recipe.imageResId)
//            title.text = recipe.title
//            duration.text = "${recipe.duration} min"
//            calories.text = "${recipe.calories} cal"
//        }
//    }
//data class Recipe(val imageResId: Int, val title: String, val duration: Int, val calories: Int)
//}

