package com.salsalia.salsalia.view.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeapp.R
import com.salsalia.salsalia.application.FavDishApplication
import com.example.recipeapp.databinding.DialogCustomListBinding
import com.example.recipeapp.databinding.FragmentAlldishesBinding
import com.salsalia.salsalia.model.entities.FavDish
import com.salsalia.salsalia.utils.Constants
import com.salsalia.salsalia.view.activities.AddUpdateRecipeActivity
import com.salsalia.salsalia.view.activities.MainActivity
import com.salsalia.salsalia.view.adapters.CustomListItemAdapter
import com.salsalia.salsalia.view.adapters.FavDishAdapter
import com.salsalia.salsalia.viewmodel.FavDishViewModel
import com.salsalia.salsalia.viewmodel.FavDishViewModelFactory

class AllRecipesFragment : Fragment() {

    // membuat objek global untuk FragmentAlldisheBinding
    private lateinit var mBinding: FragmentAlldishesBinding
    // membuat objek global untuk class FavDishAdapter
    private lateinit var mFavDishAdapter: FavDishAdapter
    // membuat objek global dialog
    private lateinit var mCustomListDialog: Dialog
    // membuat objek global view model
    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }

    // function pertama yang akan di jalankan ketika class ini di panggil
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    // untuk menampilkan data di xml
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentAlldishesBinding.inflate(inflater, container, false)

        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    // menyusun tata letak view pada layout menggunakan grid dengan menampilkan 2 kontent ke samping
        mBinding.rvDishesList.layoutManager = GridLayoutManager(requireActivity(), 2)
        // membuat adapter dari favDishAdapter
        mFavDishAdapter = FavDishAdapter(this)
        // nilai dari mFavDishAdapter akan di berikan ke mBinding untuk di tampilkan ke view
        mBinding.rvDishesList.adapter = mFavDishAdapter
        mFavDishViewModel.allDishesList.observe(viewLifecycleOwner) { dishes ->
            dishes.let {
                for (item in it) {
                    if (it.isNotEmpty()) {
                        mBinding.rvDishesList.visibility = View.VISIBLE
                        mBinding.tvNoDishesAddedYet.visibility = View.GONE

                        //it-> is the list with dishes we get from the observer
                        mFavDishAdapter.dishesList(it)
                    } else {
                        mBinding.rvDishesList.visibility = View.GONE
                        mBinding.tvNoDishesAddedYet.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
    // mengambil nilai dari list makanan untuk di tampilkan di laout detail
    fun dishDetails(favDish: FavDish) {
        findNavController().navigate(
            com.salsalia.salsalia.view.fragments.AllRecipesFragmentDirections.actionAllDishesToDishDetails(
                favDish)
        )
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)?.hideBottomNavBar()
        }
    }

    // saat aplikasi di tutup dan di buka kembali, aktifitas sebelumnya akan di teruskan
    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)?.showBottomNavBar()
        }
    }

    // agar menu filter dan add ada dapat berjalan dengan baik
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_all_recipes, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    // mengatsi agar tidak menjalankan doubel aktiviti
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add_recipe -> {
                startActivity(Intent(requireActivity(), AddUpdateRecipeActivity::class.java))
                return true
            }
            R.id.action_filter_dishes -> {
                filterDishes()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // menghapus data
    fun deleteDish(dish: FavDish) {
        // menggunakan alretDialog saat konfirmasi data ingin di hapus atau tidak
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete Dish")
        builder.setMessage("Are you sure you want to delete this dish?")
        // memasang icon pada dialog
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        // jika memilih yes maka data akan terhapus dan dialog akan tertup
        builder.setPositiveButton("Yes") { dialogInterface, _ ->
            mFavDishViewModel.delete(dish)
            dialogInterface.dismiss()
        }
        // jika memilih no maka dialog akan hilang dan data tidak di hapus
        builder.setNegativeButton("NO") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        // menampilkan dialog
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    // membuat aktifitas filter
    private fun filterDishes() {
        // filter menampilkan filter dialgo
        mCustomListDialog = Dialog(requireActivity())
        val binding: DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)
        // menampilkan dialog kosong
        mCustomListDialog.setContentView(binding.root)
        // memberi titel dialog
        binding.tvTitle.text = getString(R.string.select_filter)
        // memberikan item pada dialog
        val dishTypes = Constants.dishTypes()
        dishTypes.add(0, Constants.ALL_ITEMS)
        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        // simpan item filter
        val adapter =
            CustomListItemAdapter(
                requireActivity(),
                this@AllRecipesFragment,
                dishTypes,
                Constants.FILTER_SELECTION
            )
        // tampilkan dialog dan dapatkan data item
        binding.rvList.adapter = adapter
        mCustomListDialog.show()
    }
    // saat item filter di pilih akan menyimpan data string
    fun filterSelection(filterItem: String) {
        mCustomListDialog.dismiss()
        Log.i("Filter Selection", filterItem)
        // awal mula tampilkan semua data
        if (filterItem == Constants.ALL_ITEMS) {
            mFavDishViewModel.allDishesList.observe(viewLifecycleOwner) { dishes ->
                dishes.let {
                    for (item in it) {
                        if (it.isNotEmpty()) {
                            // tampilkan data yang ada
                            mBinding.rvDishesList.visibility = View.VISIBLE
                            mBinding.tvNoDishesAddedYet.visibility = View.GONE
                            //it-> yang di dapat dari hindanng pengamat
                            mFavDishAdapter.dishesList(it)
                        } else {
                            // jika tidak ada data tampilkan halaman kosong
                            mBinding.rvDishesList.visibility = View.GONE
                            mBinding.tvNoDishesAddedYet.visibility = View.VISIBLE
                        }
                    }
                }
            }
        } else {

            // jika filter di jalankan maka tampilkan data berdasarkan selection pada filter yang di pilih
            mFavDishViewModel.filteredList(filterItem).observe(viewLifecycleOwner) { dishes ->
                dishes.let {
                    if (it.isNotEmpty()) {
                        mBinding.rvDishesList.visibility = View.VISIBLE
                        mBinding.tvNoDishesAddedYet.visibility = View.GONE
                        //it-> is the list with dishes we get from the observer
                        mFavDishAdapter.dishesList(it)
                    } else {
                        mBinding.rvDishesList.visibility = View.GONE
                        mBinding.tvNoDishesAddedYet.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}