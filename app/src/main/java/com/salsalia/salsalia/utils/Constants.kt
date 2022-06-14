package com.salsalia.salsalia.utils

/*
objek ini berisi data yang akan ditampilkan secara statis di ui
 */
object Constants {
    const val TIPE_HIDANGAN: String = "DishType"
    const val KATEGORI_HIDANGAN : String = "DishCategory"
    const val LAMA_MASAK : String = "DishCookingTime"

    const val IMAGE_LOKAL: String = "Local"
    const val EXTRA_DETAIL: String = "DishDetails"
    const val ALL_ITEMS: String = "All"
    const val FILTER_SELECTION: String = "FilterSelection"
    //mengidentifikasi tipe hidangan
    fun dishTypes(): ArrayList<String> {
        val list = ArrayList<String>()
        list.add("Sarapan")
        list.add("Makan Siang")
        list.add("Snack")
        list.add("Makan Malam")
        list.add("Dessert")
        return list
    }
    //mengidentifikasi kategori hidangan
    fun dishCategories(): ArrayList<String> {
        val list = ArrayList<String>()
        list.add("Makanan")
        list.add("Minuman")
        return list
    }
    //mengidentifikasi lama masak hidangan
    fun dishTime(): ArrayList<String> {
        val list = ArrayList<String>()
        list.add("5")
        list.add("10")
        list.add("15")
        list.add("20")
        list.add("25")
        list.add("30")
        list.add("45")
        list.add("60")
        list.add("70")
        list.add("80")
        list.add("90")
        list.add("100")
        list.add("110")
        list.add("120")
        return list
    }
}