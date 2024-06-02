package com.pukimen.babygrowth.data.remote.response

import com.google.gson.annotations.SerializedName

data class RecomendationResponse(

	@field:SerializedName("rekomendasi")
	val rekomendasi: List<RekomendasiItem?>? = null
)

data class RekomendasiItem(

	@field:SerializedName("nama_resep")
	val namaResep: String? = null,

	@field:SerializedName("id_resep")
	val idResep: String? = null,

	@field:SerializedName("similarity_score")
	val similarityScore: Any? = null
)
