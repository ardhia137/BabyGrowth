package com.pukimen.babygrowth.data.remote.response

import com.google.gson.annotations.SerializedName

data class DetailRecipeResponse(

	@field:SerializedName("data")
	val data: DataRecipe? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class BahanItem(
	@SerializedName("jumlah")
	val jumlah: String? = null, // Changed to Any to handle both Int and String

	@SerializedName("nama_bahan")
	val namaBahan: String? = null,

	@field:SerializedName("satuan")
	val satuan: String? = null,

	@SerializedName("id_bahan")
	val idBahan: String? = null
)


data class DataRecipe(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("langkah")
	val langkah: List<LangkahItem?>? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("bahan")
	val bahan: List<BahanItem?>? = null,

	@field:SerializedName("nutrisi")
	val nutrisi: Nutrisi? = null,

	@field:SerializedName("porsi")
	val porsi: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("kategori")
	val kategori: Int? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)

data class Nutrisi(
	@SerializedName("kalori")
	val kalori: Any? = null,

	@SerializedName("karbohidrat")
	val karbohidrat: Any? = null,

	@SerializedName("gula")
	val gula: Any? = null,

	@SerializedName("protein")
	val protein: Any? = null,

	@SerializedName("serat")
	val serat: Any? = null,

	@SerializedName("natrium")
	val natrium: Any? = null,

	@SerializedName("lemak")
	val lemak: Any? = null
)

data class LangkahItem(

	@field:SerializedName("step")
	val step: Int? = null,

	@field:SerializedName("deskripsi")
	val deskripsi: String? = null
)
