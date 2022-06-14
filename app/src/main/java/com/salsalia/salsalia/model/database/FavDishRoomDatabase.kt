package com.salsalia.salsalia.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.salsalia.salsalia.model.entities.FavDish
/*
class utntuk membuat daabase
 */
@Database(entities = [FavDish::class], version = 1)
abstract class FavDishRoomDatabase : RoomDatabase() {
    abstract fun favDishDao(): FavDishDao
    companion object {
        @Volatile
        private var INSTANCE: FavDishRoomDatabase? = null
        //menginjek database untuk membuat nama database
        fun getDatabase(context: Context): FavDishRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavDishRoomDatabase::class.java,
                    "word_database"
                ).build()
                INSTANCE = instance
                // mereturn instance
                instance
            }
        }
    }
}
