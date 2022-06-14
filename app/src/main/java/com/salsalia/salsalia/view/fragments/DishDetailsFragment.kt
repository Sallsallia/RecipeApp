package com.salsalia.salsalia.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.salsalia.salsalia.application.FavDishApplication
import com.example.recipeapp.databinding.FragmentDishDetailsBinding
import com.salsalia.salsalia.viewmodel.FavDishViewModel
import com.salsalia.salsalia.viewmodel.FavDishViewModelFactory
import java.io.IOException

class DishDetailsFragment : Fragment() {
    // membuat objek untuk memanggil fragment
    private var mBinding: FragmentDishDetailsBinding? = null
    private val mFavDishViewModel: FavDishViewModel by viewModels {
        // data repository agar dapat menampil
        FavDishViewModelFactory(((requireActivity().application) as FavDishApplication).repository)
    }

    //
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding =
            FragmentDishDetailsBinding.inflate(inflater, container, false)
        return mBinding!!.root
    }

    // Fugnsi Untuk menyimpan data makanan untuk di masukan ke favourite
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // menganggil fugnsi onviewcard dari super untuk mengambil data dari halaman favourite
        // yang akan di tampilkan ke halaman detail
        super.onViewCreated(view, savedInstanceState)
        val args: com.salsalia.salsalia.view.fragments.DishDetailsFragmentArgs by navArgs()
        // untuk mengambil gambar dari db lokal melalui dishDetails yang akan di tampilkan ke view
        args.let {
            try {
                Glide.with(requireActivity())
                    .load(it.dishDetails.image)
                    .centerCrop()
                    .into(mBinding!!.ivDishImage)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            // mengambil data yang di lempar dari dishDetail
            mBinding!!.dtTitle.text = it.dishDetails.title
            mBinding!!.dtType.text = it.dishDetails.type
            mBinding!!.dtCategory.text = it.dishDetails.category
            mBinding!!.dtIngredientsVal.text = it.dishDetails.ingredients
            mBinding!!.dtDirectionVal.text = it.dishDetails.cookingDirection
            mBinding!!.dtTime.text =
                resources.getString(R.string.cook_time_format, it.dishDetails.cookingTime)

            // ubah icon favourite sebagai tanda menu favourite dan sebaliknya
            if (args.dishDetails.favoriteDish) {
                mBinding!!.ivFavoriteDish.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(), R.drawable.ic_favorite_selected
                    )
                )

            } else {
                mBinding!!.ivFavoriteDish.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(), R.drawable.ic_favorite_unselected
                    )
                )
            }
        }

        // membuat icon favourite dapat di click
        mBinding!!.ivFavoriteDish.setOnClickListener {
            args.dishDetails.favoriteDish = !args.dishDetails.favoriteDish
            mFavDishViewModel.update(args.dishDetails)

            // tampilkan toast jika menandai favouirite pada masakan
            if (args.dishDetails.favoriteDish) {
                mBinding!!.ivFavoriteDish.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(), R.drawable.ic_favorite_selected
                    )
                )
                Toast.makeText(requireContext(), "Hidangan ditambahkan difavorit", Toast.LENGTH_SHORT)
                    .show()

                // tampilkan toast jika menghapus favouirite pada masakan
            } else {
                mBinding!!.ivFavoriteDish.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(), R.drawable.ic_favorite_unselected
                    )
                )
                Toast.makeText(requireContext(), "Hidangan dihapus dari favorit", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}