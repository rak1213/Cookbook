package com.rak12.gymapp.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.app.ActivityCompat
import com.rak12.gymapp.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
        }, 2000)
    }

    override fun onBackPressed() {
        ActivityCompat.finishAffinity(this)
    }
    }
