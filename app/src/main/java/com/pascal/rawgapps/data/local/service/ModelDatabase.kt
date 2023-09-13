package com.pascal.rawgapps.data.local.service

import android.content.Context
import androidx.room.*
import com.pascal.rawgapps.data.local.model.DBModelDetails
import com.pascal.rawgapps.data.local.util.Converters

@Database(entities = [DBModelDetails::class], version = 1)
@TypeConverters(Converters::class)
abstract class ModelDatabase : RoomDatabase() {

    abstract fun modelDao(): ModelDAO


    companion object {
        @Volatile
        private var instance: ModelDatabase? = null

        private val lock = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(lock) {
            instance ?: makeDatabase(context).also {
                instance = it
            }
        }

        private fun makeDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, ModelDatabase::class.java, "modeldatabase"
        ).build()
    }
}