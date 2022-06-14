package com.salsalia.salsalia.viewmodel

import androidx.lifecycle.*
import com.salsalia.salsalia.model.database.FavDishRepository
import com.salsalia.salsalia.model.entities.FavDish
import kotlinx.coroutines.launch
/*
class ini beris bussines logic fungtion masing2 request dari repository untuk ditampilkan ke UI
 */
class FavDishViewModel(private val repository: FavDishRepository) : ViewModel() {
    //Menambah data
    fun insert(dish: FavDish) = viewModelScope.launch {
        repository.insertFavDishData(dish)
    }
    //Mampilkan semua data
    val allDishesList: LiveData<List<FavDish>> = repository.allDishesList.asLiveData()
    //Mengupdate data
    fun update(dish: FavDish) = viewModelScope.launch {
        repository.updateFavDishData(dish)
    }
    //menambahkan ke favorit
    val favoriteDish: LiveData<List<FavDish>> = repository.favoriteDishes.asLiveData()
    //menghapus data
    fun delete(dish: FavDish) = viewModelScope.launch {
        repository.deleteFavDishData(dish)
    }
    //menfilter data
    fun filteredList(value: String): LiveData<List<FavDish>> =
        repository.filteredListDishes(value).asLiveData()
}
/*
membuat class instance view model dengan di inject
 */
class FavDishViewModelFactory(private val repository: FavDishRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavDishViewModel::class.java)) {
            return FavDishViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}