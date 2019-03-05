package com.app.qrcodeapplication.ui

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.app.qrcodeapplication.R
import com.app.qrcodeapplication.ui.global.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    lateinit var navController: NavController

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        navController = findNavController(R.id.my_nav_host_fragment)
        NavigationUI.setupWithNavController(toolbar, navController)
    }

    override fun showProgress(progress: Boolean) {
        // Nothing
    }
}
