package com.pukimen.babygrowth.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.pukimen.babygrowth.utils.InitialDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Nutrition::class,NutritionDay::class], version = 1)
abstract class BabyRoomDatabase : RoomDatabase() {
    abstract fun nutritionDao(): NutritionDao
    companion object {
        @Volatile
        private var INSTANCE: BabyRoomDatabase? = null
        @JvmStatic
        fun getDatabase(context: Context,applicationScope: CoroutineScope): BabyRoomDatabase {
            if (INSTANCE == null) {
                synchronized(BabyRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        BabyRoomDatabase::class.java, "baby_database")
                        .fallbackToDestructiveMigration()
                        .addCallback(object : Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                INSTANCE?.let { database ->
                                    applicationScope.launch {
                                        val nutritionDao = database.nutritionDao()
                                        nutritionDao.insertNutritionDay(InitialDataSource.getNutririonDay())
                                    }
                                }
                            }
                        })
                        .build()
                }
            }
            return INSTANCE as BabyRoomDatabase
        }
    }
}