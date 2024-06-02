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
import com.pukimen.babygrowth.data.remote.response.NuritionResponseItem
import com.pukimen.babygrowth.databinding.ItemRowFoodBinding
import com.pukimen.babygrowth.utils.DateHelper

class FoodAdapter(private val viewModel: FoodViewModel,private val eat_time: String) : ListAdapter<NuritionResponseItem, FoodAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val nutrition = getItem(position)
        holder.bind(nutrition)
        holder.itemView.setOnClickListener {
            // Insert data ke database
            viewModel.insert(Nutrition(
                name = nutrition.name,
                calories = nutrition.calories.toString(),
                protein = nutrition.proteinG.toString(),
                carbohydrates = nutrition.carbohydratesTotalG.toString(),
                fat = nutrition.fatTotalG.toString(),
                eat_time = eat_time,
                date = DateHelper.getCurrentDate()
            ))
            Log.e(ContentValues.TAG, "Inserted: ${nutrition}")
            Toast.makeText(
                holder.itemView.context,
                "Data inserted successfully",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    class MyViewHolder(val binding: ItemRowFoodBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(nutrition: NuritionResponseItem) {
            binding.tvName.text = nutrition.name
            binding.tvCalories.text = "Calories : ${nutrition.calories} Kcal"
            binding.tvProtein.text = "Protein : ${nutrition.proteinG} G"
            binding.tvCarbo.text = "Carbohydrates : ${nutrition.carbohydratesTotalG} G"
            binding.tvFat.text = "Fat : ${nutrition.fatTotalG} G"
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NuritionResponseItem>() {
            override fun areItemsTheSame(oldItem: NuritionResponseItem, newItem: NuritionResponseItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: NuritionResponseItem, newItem: NuritionResponseItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
