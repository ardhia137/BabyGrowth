package com.pukimen.babygrowth.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface NutritionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNutritionDay(student: List<NutritionDay>)
    @Query("SELECT * from NutritionDay where id = :id ORDER BY id ASC")
    fun getNutririonDay(id : Int): LiveData<NutritionDay>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(nutrition: Nutrition)
    @Update
    fun update(nutrition: Nutrition)
    @Delete
    fun delete(nutrition: Nutrition)
    @Query("SELECT * from nutrition ORDER BY id ASC")
    fun getAllNutrition(): LiveData<List<Nutrition>>

    @Query("SELECT * from nutrition where eat_time = :eat ORDER BY id ASC")
    fun getNutritionByEat(eat : String): LiveData<List<Nutrition>>
}