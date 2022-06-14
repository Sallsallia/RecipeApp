package com.salsalia.salsalia.application

import android.app.Application
import com.salsalia.salsalia.model.database.FavDishRepository
import com.salsalia.salsalia.model.database.FavDishRoomDatabase
/*
class ini dibuat untuk menginstance database
 */
class FavDishApplication : Application() {
    private val database by lazy { FavDishRoomDatabase.getDatabase(this@FavDishApplication) }
    val repository by lazy { FavDishRepository(database.favDishDao()) }
}