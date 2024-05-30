package com.pukimen.babygrowth.data.remote.response

import com.google.gson.annotations.SerializedName

data class NuritionResponse(

	@field:SerializedName("NuritionResponse")
	val nuritionResponse: List<NuritionResponseItem?>? = null
)

data class NuritionResponseItem(

	@field:SerializedName("sodium_mg")
	val sodiumMg: Double? = null,

	@field:SerializedName("sugar_g")
	val sugarG: Double? = null,

	@field:SerializedName("fat_total_g")
	val fatTotalG: Double? = null,

	@field:SerializedName("cholesterol_mg")
	val cholesterolMg: Double? = null,

	@field:SerializedName("protein_g")
	val proteinG: Double? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("fiber_g")
	val fiberG: Double? = null,

	@field:SerializedName("calories")
	val calories: Double? = null,

	@field:SerializedName("serving_size_g")
	val servingSizeG: Double? = null,

	@field:SerializedName("fat_saturated_g")
	val fatSaturatedG: Double? = null,

	@field:SerializedName("carbohydrates_total_g")
	val carbohydratesTotalG: Double? = null,

	@field:SerializedName("potassium_mg")
	val potassiumMg: Double? = null
)
