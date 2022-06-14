package com.salsalia.salsalia.model.database

import androidx.annotation.WorkerThread
import com.salsalia.salsalia.model.entities.FavDish
import kotlinx.coroutines.flow.Flow

/*
class repository adalah clas yang digunakan untuk mengambil data dari lokal data base unuk diteruskan ke view model
 */

class FavDishRepository(private val favDishDao: FavDishDao) {
    // fungsi ini digunakan untuk mengambil fungsi insertFavDishDetails dari Dao dengan data yang di dambil dari entitis
    @WorkerThread
    suspend fun insertFavDishData(favDish: FavDish) {
        favDishDao.insertFavDishDetails(favDish)
    }
    // list ini mengambil semua data yang ada di db lokal
    val allDishesList: Flow<List<FavDish>> = favDishDao.getAllDishesList()
    // fungsi ini digunakan untuk mengambil fungsi updateFavDishList dari Dao dengan data yang di dambil dari entitis
    @WorkerThread
    suspend fun updateFavDishData(favDish: FavDish) {
        favDishDao.updateFavDishDetails(favDish)
    }
    // List ini digunakan untuk mengambil fungsi getFavouriteDishList dari Dao dengan data yang di dambil dari entitis
    val favoriteDishes: Flow<List<FavDish>> = favDishDao.getFavoriteDishesList()

    // fungsi ini digunakan untuk mengambil fungsi deletFavDishDetails dari Dao dengan data yang di dambil dari entitis
    @WorkerThread
    suspend fun deleteFavDishData(favDish: FavDish) {
        favDishDao.deleteFavDish(favDish)
    }
    // fungsi ini digunakan untuk mengambil fungsi getFilterFavDishList dari Dao dengan data yang di dambil dari entitis
    fun filteredListDishes(value: String): Flow<List<FavDish>> =
        favDishDao.getFilteredDishesList(value)
}