package com.example.sharememe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager

class Splashscreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        supportActionBar?.hide()
        waitforload()

    }

    private fun nextcellactivity(){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun waitforload(){
        Handler(Looper.getMainLooper()).postDelayed({
           nextcellactivity()
        }, 3000)
    }
}

