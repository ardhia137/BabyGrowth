package com.pukimen.babygrowth.data.model

data class RecipeModel(
    val id: String,
    val name: String,
    val image: String,
    var kategori: Int,
    val porsi: Int,
    val langkah: List<Langkah>,
    val bahan: List<Bahan>,
    val nutrisi: Nutrisi,
    val createdAt: String,
    val updatedAt: String
)

data class Langkah(
    val step: Int,
    val deskripsi: String
)

data class Bahan(
    val jumlah: String, // Can be Int or String
    val nama_bahan: Any,
    val satuan: Any,
    val id_bahan: String
)

data class Nutrisi(
    val kalori: Double,
    val karbohidrat: Double,
    val gula: Double,
    val protein: Double,
    val serat: Double,
    val natrium: Double,
    val lemak: Double
)
