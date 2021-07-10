package com.example.cmre

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.cmre.api.EndPoints
import com.example.cmre.api.OutputReports
import com.example.cmre.api.ServiceBuilder
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*


private lateinit var locationRequest: LocationRequest
private const val REQUEST_CODE = 42
private val IMAGE_PICK_CODE=1000;
private val PERMISSION_CODE=1001;

class CriarReportActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private var latitude:Double = 0.0
    private var longitude:Double = 0.0
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criar_report)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        checkPermissions()


        createLocationRequest()

        val spinner: Spinner = findViewById(R.id.TipoSP)
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.Tipos_Problema,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = this

    }
    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        Log.e( "kskskskskssk", pos.toString())
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }


    fun criarReport(view: View) {
        val imageView = findViewById<ImageView>(R.id.imagemIV)
        val tituloTV  = findViewById<EditText>(R.id.TituloET)
        val descricaoTV = findViewById<EditText>(R.id.DescricaoET)
        val tipoSP = findViewById<Spinner>(R.id.TipoSP)

        val titulo  = tituloTV.text.toString()
        val descricao = descricaoTV.text.toString()
        val tipo = (tipoSP.selectedItemPosition + 1)

        val userID = 2

        val bitmap = imageView.drawable.toBitmap()
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
        val encodedImage = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray())



        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.adicionarReport(latitude, longitude, descricao, encodedImage, userID, titulo, tipo)
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

    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        // interval specifies the rate at which your app will like to receive updates.
        locationRequest.interval = 10000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private fun checkPermissions(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION), 1)
            return
        }else{
            getLocalizacao()
        }
    }

    private fun getLocalizacao() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        fusedLocationClient.lastLocation?.addOnSuccessListener {

                location : Location? ->
            if (location != null) {
                latitude = location.latitude
                longitude = location.longitude
            }
        }
    }

    fun tirarFoto(view: View) {
        val tirarPhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if(tirarPhotoIntent.resolveActivity(this.packageManager) != null){
            startActivityForResult(tirarPhotoIntent, REQUEST_CODE)
        }else{
            Toast.makeText(this@CriarReportActivity, getString(R.string.erro), Toast.LENGTH_SHORT).show()
        }
    }
    fun Galeria(view: View) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                //permissao negada
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                requestPermissions(permissions, PERMISSION_CODE)
            }else{
                //ja tem permissoes
               escolherImagem()
            }
        }else{
            //SO baixo
            escolherImagem()
        }
    }
    private fun escolherImagem() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_CODE -> {
                if(grantResults.size >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    escolherImagem()
                } else{
                    Toast.makeText(this, getString(R.string.permissao_negada) , Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val imageView = findViewById<ImageView>(R.id.imagemIV)
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            val takenImage = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(takenImage)
        }
        else if( requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK ){
            imageView.setImageURI(data?.data)
        }
        else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}