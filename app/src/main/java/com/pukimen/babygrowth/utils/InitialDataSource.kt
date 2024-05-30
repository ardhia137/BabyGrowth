package com.pukimen.babygrowth.utils

import com.pukimen.babygrowth.data.database.NutritionDay


object InitialDataSource {
    fun getNutririonDay(): List<NutritionDay> {
        return listOf(
            NutritionDay(1,"550","9","31","59","0-6 Bulan"),
            NutritionDay(2,"800","15","31","105","7-11 Bulan"),
            NutritionDay(3,"1350","20", "45","215","12-24 Bulan"),
        )}
}