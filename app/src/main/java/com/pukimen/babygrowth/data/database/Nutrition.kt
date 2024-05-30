package com.pukimen.babygrowth.data.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Nutrition(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "calories")
    var calories: String? = null,

    @ColumnInfo(name = "protein")
    var protein: String? = null,

    @ColumnInfo(name = "fat")
    var fat: String? = null,

    @ColumnInfo(name = "carbohydrates")
    var carbohydrates: String? = null,

    @ColumnInfo(name = "eat_time")
    var eat_time: String? = null,

    @ColumnInfo(name = "date")
    var date: String? = null
) : Parcelable


@Entity
data class NutritionDay(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val calories: String,
    val protein: String,
    val fat: String,
    val carbohydrates: String?,
    val desc : String
)
