package com.rak12.gymapp.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.rak12.gymapp.Fragments.FavouriteFragment
import com.rak12.gymapp.Fragments.HomeFragment
import com.rak12.gymapp.R
import java.lang.reflect.Array.newInstance
import javax.xml.datatype.DatatypeFactory.newInstance

class DashboardActivity : AppCompatActivity() {
    lateinit var sp: SharedPreferences
    lateinit var fl: FrameLayout
    lateinit var toolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        val bottomNavigation:BottomNavigationView = findViewById(R.id.navigationView)

        fl = findViewById(R.id.container)
        sp = getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE)
        toolbar = findViewById(R.id.toolbar)
        openFragment(HomeFragment())
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        setuptoolbar()
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.home -> {
                toolbar.title = "All Cuisines"

                openFragment(HomeFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.fav -> {
                toolbar.title = "Favourites"

                openFragment(FavouriteFragment())
                return@OnNavigationItemSelectedListener true
            }

        }
        false
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }
    fun setuptoolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "All Cuisines"

    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.container)
        when (frag) {
            !is HomeFragment -> openFragment(HomeFragment())
            else -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Confirmation")
                        .setMessage("Are you sure you want exit?")
                        .setPositiveButton("Yes") { _, _ ->
                            sp.edit().clear().apply()
                            startActivity(Intent(this@DashboardActivity, LoginActivity::class.java))
                            Volley.newRequestQueue(this).cancelAll(this::class.java.simpleName)
                            ActivityCompat.finishAffinity(this)

                        }
                        .setNegativeButton("No") { _, _ ->


                        }
                        .create()
                        .show()

            }
            }
        }


    }
