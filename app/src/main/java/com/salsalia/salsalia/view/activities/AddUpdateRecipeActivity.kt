package com.salsalia.salsalia.view.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.recipeapp.R
import com.salsalia.salsalia.application.FavDishApplication
import com.example.recipeapp.databinding.ActivityAddUpdateRecipeBinding
import com.example.recipeapp.databinding.CustomDialogBinding
import com.example.recipeapp.databinding.DialogCustomListBinding
import com.salsalia.salsalia.model.entities.FavDish
import com.salsalia.salsalia.utils.Constants
import com.salsalia.salsalia.view.adapters.CustomListItemAdapter
import com.salsalia.salsalia.viewmodel.FavDishViewModel
import com.salsalia.salsalia.viewmodel.FavDishViewModelFactory
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*


class AddUpdateRecipeActivity : AppCompatActivity(), View.OnClickListener,
    EasyPermissions.PermissionCallbacks {
    // inisialisasi viewbinding
    private lateinit var mBinding: ActivityAddUpdateRecipeBinding
    private lateinit var mCustomListDialog: Dialog
    // global variabel untuk menyimpan image path
    private var mImagePath: String = ""
    private var mFavDishDetails: FavDish? = null
    // inisialisai viewmodel
    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory((application as FavDishApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAddUpdateRecipeBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        // melempar data favorit dengan intent extra
        if (intent.hasExtra(Constants.EXTRA_DETAIL)) {
            mFavDishDetails = intent.getParcelableExtra(Constants.EXTRA_DETAIL)
            supportActionBar?.title = getString(R.string.edit_dish_title)
        }
        // mengimplemntasi data ke ui
        mFavDishDetails?.let {
            if (it.id != 0) {
                mImagePath = it.image
                Glide.with(this@AddUpdateRecipeActivity)
                    .load(mImagePath)
                    .centerCrop()
                    .into(mBinding.ivDishImage)
                mBinding.etTitle.setText(it.title)
                mBinding.etType.setText(it.type)
                mBinding.etCategory.setText(it.category)
                mBinding.etIngredients.setText(it.ingredients)
                mBinding.etCookingTime.setText(it.cookingTime)
                mBinding.etDirectionToCook.setText(it.cookingDirection)

                mBinding.btnAddDish.text = resources.getString(R.string.lbl_update_dish)
            }
        }

        // memberi fungsi saat komponen di klik
        mBinding.ivAddDishImage.setOnClickListener(this)
        mBinding.etType.setOnClickListener(this)
        mBinding.etCategory.setOnClickListener(this)
        mBinding.etCookingTime.setOnClickListener(this)
        mBinding.btnAddDish.setOnClickListener(this)

    }
    // function untuk masing2 field untuk inputan
    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                // memberi fungsi onclick untuk button upload image
                // untuk memanggil fungsi costumeImageSlectionDialog
                R.id.iv_add_dish_image -> {
                    customImageSelectionDialog()
                    return
                }

                // memberi fungsi onclick pada field type untuk membuka dialog
                // yang sudah di buat pada function customItemListDialog
                // dialog tersebut menampilan isi list dari fungsi dishTypes()
                R.id.et_type -> {
                    customItemsListDialog(
                        "Select Dish Type",
                        Constants.dishTypes(),
                        Constants.TIPE_HIDANGAN
                    )
                    return
                }
                // memberi fungsi onclick pada field category untuk membuka dialog
                // yang sudah di buat pada function customItemListDialog
                // dialog tersebut menampilan isi list dari fungsi dishCategories()
                R.id.et_category -> {
                    customItemsListDialog(
                        "Select Dish Category",
                        Constants.dishCategories(),
                        Constants.KATEGORI_HIDANGAN
                    )
                    return
                }
                // memberi fungsi onclick pada field lama masak untuk membuka dialog
                // yang sudah di buat pada function customItemListDialog
                // dialog tersebut menampilan isi list dari fungsi dashTime()
                R.id.et_cooking_time -> {
                    customItemsListDialog(
                        "Select Dish Cooking Time",
                        Constants.dishTime(),
                        Constants.LAMA_MASAK
                    )
                    return
                }

                R.id.btn_add_dish -> {
                    val title = mBinding.etTitle.text.toString().trim { it <= ' ' }
                    val type = mBinding.etType.text.toString().trim { it <= ' ' }
                    val category = mBinding.etCategory.text.toString().trim { it <= ' ' }
                    val ingredients = mBinding.etIngredients.text.toString().trim { it <= ' ' }
                    val cookingTimeInMinutes =
                        mBinding.etCookingTime.text.toString().trim { it <= ' ' }
                    val cookingDirection =
                        mBinding.etDirectionToCook.text.toString().trim { it <= ' ' }
                    when {
                        /*
                        function ini untuk mengecek apakah field berisi data atau belum
                         */
                        TextUtils.isEmpty(mImagePath) -> {
                            Toast.makeText(
                                this,
                                getString(R.string.err_msg_select_recipe_image),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        TextUtils.isEmpty(title) -> {
                            Toast.makeText(
                                this,
                                getString(R.string.err_msg_enter_recipe_title),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        TextUtils.isEmpty(type) -> {
                            Toast.makeText(
                                this,
                                getString(R.string.err_msg_select_recipe_type),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        TextUtils.isEmpty(category) -> {
                            Toast.makeText(
                                this,
                                getString(R.string.err_msg_select_recipe_category),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        TextUtils.isEmpty(ingredients) -> {
                            Toast.makeText(
                                this,
                                getString(R.string.err_msg_enter_recipe_ingredients),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        TextUtils.isEmpty(cookingTimeInMinutes) -> {
                            Toast.makeText(
                                this,
                                getString(R.string.err_msg_select_recipe_cooking_time),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        TextUtils.isEmpty(cookingDirection) -> {
                            Toast.makeText(
                                this,
                                getString(R.string.err_msg_enter_recipe_cooking_instructions),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else -> {
                            var dishId = 0
                            var imageSource = Constants.IMAGE_LOKAL
                            var favoriteDish = false

                            mFavDishDetails?.let {
                                if (it.id != 0) {
                                    dishId = it.id
                                    imageSource = it.imageSource
                                    favoriteDish = it.favoriteDish
                                }
                            }


                            // membuat objek kelas modeluntuk maping data
                            val favDishDetails = FavDish(
                                mImagePath,
                                imageSource,
                                title,
                                type,
                                category,
                                ingredients,
                                cookingTimeInMinutes,
                                cookingDirection,
                                favoriteDish,
                                dishId
                            )
                            // jika tidak membawa id jalankan fungsi insert
                            if (dishId == 0) {
                                mFavDishViewModel.insert(favDishDetails)
                                Toast.makeText(
                                    this,
                                    "You successfully added your dish details.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            // jika membawa ig maka jalankan printah update data
                            else{
                                mFavDishViewModel.update(favDishDetails)
                                Toast.makeText(
                                    this,
                                    "You successfully updated your dish details.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            finish()
                        }
                    }
                }
            }
        }
    }


    // untuk menampilkan dialog pilihan ambil image dari galery / cameraa
    private fun customImageSelectionDialog() {
        val dialog = Dialog(this)
        val binding: CustomDialogBinding =
            CustomDialogBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        binding.tvCamera.setOnClickListener {
            checkCameraPermission()
            dialog.dismiss()
        }
        binding.tvGallery.setOnClickListener {
            checkStoragePermission()
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        AlertDialog.Builder(this)
            .setMessage(
                "It looks like you haven't given permissions required for " +
                        "this feature. It can be enabled under settings"
            )
            .setPositiveButton("GO TO SETTINGS") { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }.setNegativeButton("No", null)
            .show()
    }

    // saat aplikasi di instal akan meminta akses camera
    private fun checkCameraPermission() {
        if (hasPermission()) {
            // jika aplikasi sudah di beri hak akases maka akan di exsekusi
            val galleryIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (galleryIntent.resolveActivity(packageManager) != null) { // its always null
                startActivityForResult(galleryIntent, REQUEST_IMAGE_CAPTURE)
            }
        } else {
            // Meminta permission dari prangkat
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.rationale_camera),
                REQUEST_CODE,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
            )
        }
    }

    //  cek apakah aplikasi sudah di beri hak akses atau belum
    private fun checkStoragePermission() {
        if (hasPermission()) {
            // jika sudah di beri akses makaka dapat mengambil gambar di galeri
            val takePictureIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
          // ambil gambar dari galeri
            if (takePictureIntent.resolveActivity(packageManager) != null) { // its always null
                startActivityForResult(takePictureIntent, REQUEST_FILE_GALLERY)
            }

        } else {
           // jika belum di kasih akses maka aplikasi meminta akses
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.rationale_camera),
                REQUEST_CODE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
            )
        }
    }
    // meminta akses kamera dan galeri
    private fun hasPermission(): Boolean {
        return EasyPermissions.hasPermissions(
            this,
            Manifest.permission.CAMERA
        ) && EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                data?.extras?.let {
                    val thumbnail: Bitmap = data.extras!!.get("data") as Bitmap
                    Glide.with(this)
                        .load(thumbnail)
                        .centerCrop()
                        .placeholder(R.drawable.ic_add)
                        .into(mBinding.ivDishImage)
                    // simpan img di storege
                    mImagePath = saveImageToInternalStorage(thumbnail)
                    // cek path img
                    Log.w("image path", mImagePath)

                    Glide.with(this)
                        .load(R.drawable.ic_edit)
                        .placeholder(R.drawable.ic_add)
                        .into(mBinding.ivAddDishImage)
                }
            }
            // ababila user memilih ambil gambar dari galeri
            if (requestCode == REQUEST_FILE_GALLERY) {
                data?.let {
                    // ambil data dari galeri
                    val selectedPhotoUri = data.data
                    Glide.with(this)
                        .load(selectedPhotoUri)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(object : RequestListener<Drawable> {
                            // apablia gambar gagal memuat lakukan ambil ualang gambar
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                // cek apabila terjadi error saat pengambilakn gambar
                                Log.e("TAG", "Error loading image", e)
                                return false
                            }
                            // apabila gambar sudah ready
                            // melakukan simpan gambar ke db
                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                resource?.let {
                                    val bitmap: Bitmap = resource.toBitmap()
                                    mImagePath = saveImageToInternalStorage(bitmap)
                                }
                                return false
                            }
                        })
                        .placeholder(R.drawable.ic_add)
                        .into(mBinding.ivDishImage)

                    Glide.with(this)
                            // untuk mengambil ulang gambar
                        .load(R.drawable.ic_edit)
                        .placeholder(R.drawable.ic_add)
                        .into(mBinding.ivAddDishImage)
                }
            }
            // jika gagal maka tampilkan log
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("Cancelled", "User Cancelled images selection")
        }
    }

    // untuk menyimpan gambar ke internal Storage.
    private fun saveImageToInternalStorage(bitmap: Bitmap): String {
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")
        try {
            // compres file img dengan menubah format menjadi jpg dengan kualitas 100
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file.absolutePath
    }
    // membuat dialog untuk memnampilkan data" yang ada di list pada file constants
    private fun customItemsListDialog(title: String, itemsList: List<String>, selection: String) {
        mCustomListDialog = Dialog(this)
        val binding: DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)
        mCustomListDialog.setContentView(binding.root)
        binding.tvTitle.text = title
        binding.rvList.layoutManager = LinearLayoutManager(this)
        val adapter = CustomListItemAdapter(this,null, itemsList, selection)
        binding.rvList.adapter = adapter
        mCustomListDialog.show()
    }

    // untuk mengeset nilai dari tipe hidangan dan kategori dari dialog
    fun selectedListItem(item: String, selection: String) {

        when (selection) {
            // menampilkan dialog berisi jenis hidangan, dan set nilai tipe hidangan
            Constants.TIPE_HIDANGAN -> {
                mCustomListDialog.dismiss()
                mBinding.etType.setText(item)
            }

            // menampilkan dialog berisi data kategori dan set nilai tipe kagegori
            Constants.KATEGORI_HIDANGAN -> {
                mCustomListDialog.dismiss()
                mBinding.etCategory.setText(item)
            }
            else -> {
                // menampilakn dialog berisi data lama masak dan set nilai lama masak
                mCustomListDialog.dismiss()
                mBinding.etCookingTime.setText(item)
            }
        }
    }

    // membuat objek dengan nilai pasti untuk pengkondisian
    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val REQUEST_FILE_GALLERY = 2
        private const val REQUEST_CODE: Int = 123
        private const val IMAGE_DIRECTORY = "RecipesImg"
    }
}
