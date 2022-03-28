package com.mik.recyloc

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.maintoolbar.*

class MainActivity : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbarSetup()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext,"Permission granted.",Toast.LENGTH_SHORT).show()
            }else {
                Toast.makeText(applicationContext,"Permission not granted.",Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }


    private fun toolbarSetup() {
        if (maintoolbar != null) {
            setSupportActionBar(maintoolbar)
            maintoolbar.title = "RecyLoc"
            maintoolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24)
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        NavigationUI.setupWithNavController(navigationView,navHostFragment.navController)
        val toggle = ActionBarDrawerToggle(this,drawer,maintoolbar,0,0)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

    }

    override fun onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        }
        super.onBackPressed()
    }

}