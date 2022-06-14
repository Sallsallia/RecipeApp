package com.salsalia.salsalia.view.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.databinding.ItemDishLayoutBinding
import com.salsalia.salsalia.model.entities.FavDish
import com.salsalia.salsalia.utils.Constants
import com.salsalia.salsalia.view.activities.AddUpdateRecipeActivity
import com.salsalia.salsalia.view.fragments.AllRecipesFragment
import com.salsalia.salsalia.view.fragments.FavoriteDishesFragment

class FavDishAdapter(private val fragment: Fragment) :
    RecyclerView.Adapter<FavDishAdapter.ViewHolder>() {
    // membuat list tipe FavDish
    private var dishes: List<FavDish> = listOf()

    // membuat viewholder untuk menampilkan img, title
    class ViewHolder(view: ItemDishLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        val ivDishImage = view.ivDishImage
        val tvTitle = view.tvDishTitle
        val ibMore = view.ibMore
    }
    // agar list dapat di mengirimkan data id item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemDishLayoutBinding =
            ItemDishLayoutBinding.inflate(
                LayoutInflater.from((fragment.context)), parent, false
            )
        return ViewHolder(binding)
    }

    // menambil data dari db lokal
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dish = dishes[position]
        Glide.with(fragment)
            .load(dish.image)
            .into(holder.ivDishImage)
        holder.tvTitle.text = dish.title
        // memberi fungsi clik pada tiap item
        // dan melempar ke detail page
        holder.itemView.setOnClickListener {
            if (fragment is AllRecipesFragment) {
                fragment.dishDetails(dish)
            }
            // membuka detail detail pada favourite menu
            if (fragment is FavoriteDishesFragment) {
                fragment.dishDetails(dish)
            }
        }
        //
        holder.ibMore.setOnClickListener {
            // membuka pop up delet / update
            val popupMenu = PopupMenu(fragment.context, holder.ibMore)
            popupMenu.menuInflater.inflate(R.menu.menu_adapter, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
                // membuka halaman edit saat tombol edit data di jalankan
                if (it.itemId == R.id.action_edit_dish) {
                    // sebelum pindah halaman edit data
                        // intent membawa data dari constatns
                    val intent =
                        Intent(fragment.requireActivity(), AddUpdateRecipeActivity::class.java)
                    intent.putExtra(Constants.EXTRA_DETAIL, dish)
                    fragment.requireActivity().startActivity(intent)


                } else if (it.itemId == R.id.action_delete_dish) {
                    // delet data bisa dilakukan hanya di menu daftar resep
                    if (fragment is AllRecipesFragment) {
                        fragment.deleteDish(dish)
                    }


                }
                true
            }
            // menampilkan pop up menu
            popupMenu.show()
        }

            // jika fragmentRecipesFragment
        if (fragment is AllRecipesFragment) {
            holder.ibMore.visibility = View.VISIBLE
        } else if (fragment is FavoriteDishesFragment) {
            // tidak tampilkan di favourite
            holder.ibMore.visibility = View.INVISIBLE
        }


    }

    // mengambil panjang item yang ada di db
    override fun getItemCount(): Int {
        return dishes.size
    }
    // mengambil data list di db
    fun dishesList(list: List<FavDish>) {
        dishes = list
        notifyDataSetChanged()
    }

}