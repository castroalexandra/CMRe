package com.example.cmre

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun abrirMapas(view: View) {
        val sharedPref: SharedPreferences = getSharedPreferences("LoginSP", Context.MODE_PRIVATE)
        var loggedIn = false

        if (sharedPref != null){
            if(sharedPref.all["estadoLogin"]==true){
                loggedIn = true
            }
        }


        var intent : Intent
        if(loggedIn){
            intent = Intent(this, MapsActivity::class.java)
        } else {
            intent = Intent(this, LoginActivity::class.java)
        }
        startActivity(intent)
    }

    fun abrirNotas(view: View) {
        val intent = Intent(this, NotasActivity::class.java)
        startActivity(intent)
    }
}