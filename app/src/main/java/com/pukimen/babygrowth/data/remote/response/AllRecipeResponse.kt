package com.pukimen.babygrowth.data.remote.response

import com.google.gson.annotations.SerializedName

data class AllRecipeResponse(

	@field:SerializedName("data")
	val data: List<DataItemAll?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataItemAll(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("langkah")
	val langkah: List<LangkahItemAll?>? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("bahan")
	val bahan: List<BahanItemAll?>? = null,

	@field:SerializedName("nutrisi")
	val nutrisi: NutrisiAll? = null,

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

data class NutrisiAll(

	@field:SerializedName("kalori")
	val kalori: Any? = null,

	@field:SerializedName("karbohidrat")
	val karbohidrat: Any? = null,

	@field:SerializedName("gula")
	val gula: Int? = null,

	@field:SerializedName("protein")
	val protein: Any? = null,

	@field:SerializedName("serat")
	val serat: Int? = null,

	@field:SerializedName("natrium")
	val natrium: Int? = null,

	@field:SerializedName("lemak")
	val lemak: Any? = null
)

data class LangkahItemAll(

	@field:SerializedName("step")
	val step: Int? = null,

	@field:SerializedName("deskripsi")
	val deskripsi: String? = null
)

data class BahanItemAll(

	@field:SerializedName("jumlah")
	val jumlah: Any? = null,

	@field:SerializedName("nama_bahan")
	val namaBahan: String? = null,

	@field:SerializedName("satuan")
	val satuan: String? = null,

	@field:SerializedName("id_bahan")
	val idBahan: String? = null
)
