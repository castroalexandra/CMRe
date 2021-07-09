package com.example.cmre

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.cmre.api.EndPoints
import com.example.cmre.api.OutputReports
import com.example.cmre.api.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CriarReportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criar_report)
    }

    fun criarReport(view: View) {
        val latitude = 42.096809
        val longitude = -8.304399
        val descricao = "Acidente"
        val imagem = "uyg"
        val userID = 2
        val titulo = "Alvaredo"
        val tipo = 1

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.adicionarReport(latitude, longitude, descricao, imagem, userID, titulo, tipo)
        call.enqueue(object : Callback<OutputReports> {
            override fun onResponse(call: Call<OutputReports>, response: Response<OutputReports>) {
                if (response.isSuccessful) {
                    Log.d("***", "funcionou insert")
                    val intent = Intent(applicationContext, MapsActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onFailure(call: Call<OutputReports>, t: Throwable) {
                //Toast.makeText(applicationContext, "${t.message}", Toast.LENGTH_SHORT).show()
                Log.d("***", "ErrorOccur:  ${t.message}, ${call}")


            }
        })
    }
}