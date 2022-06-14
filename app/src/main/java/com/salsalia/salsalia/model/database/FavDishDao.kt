package com.salsalia.salsalia.model.database

import androidx.room.*
import com.salsalia.salsalia.model.entities.FavDish
import kotlinx.coroutines.flow.Flow

@Dao
interface FavDishDao {
    //Menambahkab resep baru
    @Insert
    suspend fun insertFavDishDetails(favDish: FavDish)
    //mengambil resep yang diurutkan dari Id
    @Query("SELECT * FROM FAV_DISHES_TABLE ORDER BY ID")
    fun getAllDishesList(): Flow<List<FavDish>>
    //update resep
    @Update
    suspend fun updateFavDishDetails(favDish: FavDish)
    //mengambil resep favorit yang diorder melalui id
    @Query("SELECT * FROM FAV_DISHES_TABLE WHERE favorite_dish = 1")
    fun getFavoriteDishesList(): Flow<List<FavDish>>
    //hapus
    @Delete
    suspend fun deleteFavDish(favDish: FavDish)
    //filter resep berdasarkan column type
    @Query("SELECT * FROM FAV_DISHES_TABLE WHERE type=:filterType")
    //filter dataa
    fun getFilteredDishesList(filterType: String): Flow<List<FavDish>>


}