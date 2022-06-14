package com.salsalia.salsalia.view.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.recipeapp.R
import com.example.recipeapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // membuat objek ActivityMainBinding dengan nama objek binding
    private lateinit var binding: ActivityMainBinding
    // membuat objek NavController dengan nama mNavController
    private lateinit var mNavController: NavController
    // function ini dipanggil pertama saat class ini dijalankan
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // memanggil activity_main.xml untuk menjadikan tampilan aktif di layar
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // memanggil fragment yang dibuat pada activity_main.xml
        mNavController = findNavController(R.id.nav_host_fragment_activity_main)
        // konfigurasi button bar yang sudah dibuat dan memasang 2 icon dari button_nav_menu.xml
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_all_dishes,
                R.id.navigation_favorite_dishes,
            )
        )

        // setup action bar dengan kontroller
        setupActionBarWithNavController(mNavController, appBarConfiguration)
        binding.navView.setupWithNavController(mNavController)
    }

    // membuat navigasi antar ui
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(mNavController, null)
    }

    // menyembuyikan bottomnavbar
    fun hideBottomNavBar() {
        binding.navView.clearAnimation()
        binding.navView.animate().translationY(binding.navView.height.toFloat()).duration = 300
        binding.navView.visibility = View.GONE
    }

    // menampilkan butonNavbar
    fun showBottomNavBar() {
        binding.navView.visibility = View.VISIBLE
        binding.navView.clearAnimation()
        binding.navView.animate().translationY(0f).duration = 300
    }
}