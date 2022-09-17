package com.cluster.bookstore.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.cluster.bookstore.*
import com.cluster.bookstore.fragment.AboutAppFragment
import com.cluster.bookstore.fragment.DashboardFragment
import com.cluster.bookstore.fragment.FavouritesFragment
import com.cluster.bookstore.fragment.ProfileFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView

    var previousMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        toolbar = findViewById(R.id.toolbar)
        frameLayout = findViewById(R.id.frameLayout)
        navigationView = findViewById(R.id.navigationView)

        setUpToolbar()
        openDashboard()         // Opens the Dashboard Activity as the home screen

        var actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.cloe_drawer)

        // Whenever the toggle will be pressed, the drawer will get to know about it
        drawerLayout.addDrawerListener(actionBarDrawerToggle)   // This will add a click listener on a Hamburger Icon

        actionBarDrawerToggle.syncState()  // Turns the Hamburger icon to Back Arrow when the navigation drawer is open

        navigationView.setNavigationItemSelectedListener {

            // To check the current menu item, we use
            if (previousMenuItem != null){
                previousMenuItem?.isChecked = false
            }

                it.isCheckable = true
                it.isChecked = true
                previousMenuItem = it


            when(it.itemId){    // 'it' is used to indicate the currently selected item

                R.id.dashboard -> {
                    openDashboard()

                    supportActionBar?.title = "Dashboard"   // set the toolbar title
                    drawerLayout.closeDrawers()
                }

                R.id.favourites -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, FavouritesFragment())
                        .commit()

                    supportActionBar?.title = "Favourites"
                    drawerLayout.closeDrawers()
                }

                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, ProfileFragment())
                        .commit()

                    supportActionBar?.title = "Profile"
                    drawerLayout.closeDrawers()
                }

                R.id.about -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, AboutAppFragment())
                        .commit()

                    supportActionBar?.title = "About App"
                    drawerLayout.closeDrawers()
                }
            }
            return@setNavigationItemSelectedListener true
        }

    }

    fun setUpToolbar(){
        setSupportActionBar(toolbar)    // Converts toolbar into action bar
        supportActionBar?.title = "Toolbar Title"
        // Hamburger icon is predefined
        supportActionBar?.setHomeButtonEnabled(true)      // Enabling Hamburger Icon
        supportActionBar?.setDisplayHomeAsUpEnabled(true)      // Display Hamburger Icon
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if(id == android.R.id.home){         // Here, home button is Hamburger Icon
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    fun openDashboard(){
        val fragment = DashboardFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
            .commit()
        supportActionBar?.title = "Dashboard"
        navigationView.setCheckedItem(R.id.dashboard)    // selects the Dashboard menu when open the navigation menu
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frameLayout)

        when(frag) {
            !is DashboardFragment -> openDashboard()

            else -> super.onBackPressed()
        }

    }
}