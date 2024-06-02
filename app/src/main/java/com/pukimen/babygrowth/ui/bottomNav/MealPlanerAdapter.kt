package com.pukimen.babygrowth.ui.bottomNav

import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pukimen.babygrowth.data.database.Nutrition
import com.pukimen.babygrowth.databinding.ItemRowDetailBinding

class MealPlanerAdapter(private val viewModel: FoodViewModel, ) : ListAdapter<Nutrition, MealPlanerAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val nutrition = getItem(position)
        holder.bind(nutrition)
        holder.binding.deleteButton.setOnClickListener {
            viewModel.delete(nutrition as Nutrition)
            Log.e(ContentValues.TAG, "deleted: ${nutrition}")
            Toast.makeText(
                holder.itemView.context,
                "Data Deleted successfully",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    class MyViewHolder(val binding: ItemRowDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(nutrition: Nutrition) {
            binding.tvCardName.text = nutrition.name
            binding.tvCardCalories.text = "Calories : ${nutrition.calories} Kcal"
            binding.tvCardProtein.text = "Protein : ${nutrition.protein} G"
            binding.tvCardCarbo.text = "Carbo : ${nutrition.carbohydrates} G"
            binding.tvCardFat.text = "Fat : ${nutrition.fat} G"
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Nutrition>() {
            override fun areItemsTheSame(oldItem: Nutrition, newItem: Nutrition): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Nutrition, newItem: Nutrition): Boolean {
                return oldItem == newItem
            }
        }
    }
}
