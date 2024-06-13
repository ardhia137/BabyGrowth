package com.pukimen.babygrowth.ui.bottomNav

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pukimen.babygrowth.R
import com.pukimen.babygrowth.data.model.Bahan
import com.pukimen.babygrowth.databinding.ItemIngredientBinding

class IngredientAdapter(private val ingredients: List<Bahan>) :
    RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>() {

    inner class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemIngredientBinding.bind(itemView)

        fun bind(ingredient: Bahan) {
            binding.tvIngredientName.text = ingredient.nama_bahan.toString()
            binding.tvIngredientAmount.text = "${ingredient.jumlah} ${ingredient.satuan}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ingredient, parent, false)
        return IngredientViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        holder.bind(ingredients[position])
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }
}
