package com.pukimen.babygrowth.data.remote.response

import com.google.gson.annotations.SerializedName

data class RecomendationResponse(

	@field:SerializedName("rekomendasi")
	val rekomendasi: List<RekomendasiItem?>? = null
)

data class RekomendasiItem(

	@field:SerializedName("kalori")
	val kalori: Any? = null,

	@field:SerializedName("porsi")
	val porsi: Int? = null,

	@field:SerializedName("protein")
	val protein: Any? = null,

	@field:SerializedName("karbo")
	val karbo: Any? = null,

	@field:SerializedName("nama_resep")
	val namaResep: String? = null,

	@field:SerializedName("id_resep")
	val idResep: String? = null,

	@field:SerializedName("kategori")
	val kategori: Int? = null,

	@field:SerializedName("gambar")
	val gambar: String? = null,

	@field:SerializedName("similarity_score")
	val similarityScore: Any? = null,

	@field:SerializedName("lemak")
	val lemak: Any? = null
)
