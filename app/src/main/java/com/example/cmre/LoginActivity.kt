package com.example.cmre

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import com.example.cmre.api.EndPoints
import com.example.cmre.api.OutputPost
import com.example.cmre.api.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }


    fun login(view: View){
        val username = findViewById<EditText>(R.id.usernameET)
        val password = findViewById<EditText>(R.id.passwordET)

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.login(username.text.toString(), password.text.toString())

        Log.e("sadfsa", call.toString())

        if (username.text.isNullOrEmpty() || password.text.isNullOrEmpty()) {

            if (username.text.isNullOrEmpty() && !password.text.isNullOrEmpty()) {
                username.error = "Falta preencher campo"
            }
            if (!username.text.isNullOrEmpty() && password.text.isNullOrEmpty()) {
                password.error = "Falta preencher campo"
            }
            if (username.text.isNullOrEmpty() && password.text.isNullOrEmpty()) {
                username.error = "Falta preencher campo"
                username.error = "Falta preencher campo"
            }
        }


        call.enqueue(object : Callback<OutputPost> {
            override fun onResponse(call: Call<OutputPost>, response: Response<OutputPost>) {
                if (response.isSuccessful) {
                    val c: OutputPost = response.body()!!
                    Log.d("STATUS", c.status.toString())
                }
            }

            override fun onFailure(call: Call<OutputPost>, t: Throwable) {
                Log.e("STATUS", t.toString())
            }
        })

    }
}