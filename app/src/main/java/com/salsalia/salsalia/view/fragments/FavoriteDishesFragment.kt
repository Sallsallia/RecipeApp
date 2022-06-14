package com.salsalia.salsalia.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.recipeapp.R
import com.example.recipeapp.databinding.FragmentFavoriteDishBinding
import com.salsalia.salsalia.application.FavDishApplication
import com.salsalia.salsalia.model.entities.FavDish
import com.salsalia.salsalia.view.activities.MainActivity
import com.salsalia.salsalia.view.adapters.FavDishAdapter
import com.salsalia.salsalia.viewmodel.FavDishViewModel
import com.salsalia.salsalia.viewmodel.FavDishViewModelFactory

class FavoriteDishesFragment : Fragment() {

    private lateinit var mBinding: FragmentFavoriteDishBinding
    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentFavoriteDishBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    // untuk menampilkan data dari database locak ke fragment
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.rvFavDishesList.layoutManager = GridLayoutManager(requireActivity(), 2)
        val favDishAdapter = FavDishAdapter(this@FavoriteDishesFragment)
        // membuat adapter untuk menampilkan data di fragment favourite sish
        mBinding.rvFavDishesList.adapter = favDishAdapter
        // jika data favourite tidak ada makan tampilkan text Favourite belum ditambahkan
        // jika datanya tidak kosong maka tampilkan data
        mFavDishViewModel.favoriteDish.observe(viewLifecycleOwner) { dishes ->
            dishes.let {
                if (it.isNotEmpty()) {
                    for (dish in it) {
                        mBinding.rvFavDishesList.visibility = View.VISIBLE
                        mBinding.tvNoFavDishesAddedYet.visibility = View.GONE
                        favDishAdapter.dishesList(it)
                    }
                } else {
                    mBinding.rvFavDishesList.visibility = View.GONE
                    mBinding.tvNoFavDishesAddedYet.visibility = View.VISIBLE
                    mBinding.tvNoFavDishesAddedYet.text =
                        getString(R.string.fav_dishes_fragment_label)
                }
            }
        }
    }
    // untuk membuka halaman detail makanan dari menu favourite food
    fun dishDetails(favDish: FavDish) {
        // untuk navaigasi ke halaman detail
        findNavController().navigate(
            FavoriteDishesFragmentDirections.actionFavDishesToDishDetails(
                favDish
            )
        )

        // jika di halaman detail activity makan hilangkan button navbar
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)?.hideBottomNavBar()
        }
    }

    // jika sudah pindah ke main activity maka tampilkan button navabar
    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)?.showBottomNavBar()
        }
    }
}