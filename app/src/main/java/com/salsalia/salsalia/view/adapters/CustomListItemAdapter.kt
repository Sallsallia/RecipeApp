package com.salsalia.salsalia.view.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.databinding.ItemCustomListBinding
import com.salsalia.salsalia.view.activities.AddUpdateRecipeActivity
import com.salsalia.salsalia.view.fragments.AllRecipesFragment

/*
class in custom adaper yang bibuat untuk menampilkan list secara grid
 */
class CustomListItemAdapter(
    // deklarasi atribut activity, fragment, list untuk menyimpan data, select untuk menyimpan
    // data dari item yang di pilih
    private val activity: Activity,
    private val fragment: Fragment?,
    private val listItems: List<String>,
    private val selection: String
) : RecyclerView.Adapter<CustomListItemAdapter.ViewHolder>() {

    class ViewHolder(view: ItemCustomListBinding) : RecyclerView.ViewHolder(view.root) {
        val tvText = view.tvText
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCustomListBinding =
            ItemCustomListBinding.inflate(LayoutInflater.from(activity), parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listItems[position]
        // set tvText dengan item
        holder.tvText.text = item
        holder.itemView.setOnClickListener {
            // jika aktivity update, maka ambil item dan selection
            if (activity is AddUpdateRecipeActivity) {
                activity.selectedListItem(item, selection)
            }
            // untuk memilih item dari dialog filter
            if(fragment is AllRecipesFragment){
                fragment.filterSelection(item)
            }
        }
    }
    // mengambl panjang list item
    override fun getItemCount(): Int {
        return listItems.size
    }

}