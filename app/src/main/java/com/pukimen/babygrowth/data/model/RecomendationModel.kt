package com.pukimen.babygrowth.data.model

data class RecomendationModel(
    val id_resep: String,
    val nama_resep: String,
    val gambar: String,
    val kalori: Any,
    val karbo: Any,
    val lemak: Any,
    val protein: Any,
    val kategori: Int,
    val porsi: Int,
    val similarity_score: Any
)