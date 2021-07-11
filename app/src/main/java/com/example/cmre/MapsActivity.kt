package com.example.cmre

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.cmre.api.EndPoints
import com.example.cmre.api.Report
import com.example.cmre.api.ServiceBuilder
import com.example.cmre.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.slider.Slider
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var reports: List<Report>
    private var results = FloatArray(1)
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        //binding = ActivityMapsBinding.inflate(layoutInflater)
        //setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val acidenteCB = findViewById<CheckBox>(R.id.checkBoxAcidente)
        val obrasCB = findViewById<CheckBox>(R.id.checkBoxObras)
        val buracoCB = findViewById<CheckBox>(R.id.checkBoxBuraco)
        val distanciaSL = findViewById<Slider>(R.id.distanciaSli)


        acidenteCB.setOnCheckedChangeListener { buttonView, isChecked ->
            addMarcadores()
        }
        obrasCB.setOnCheckedChangeListener { buttonView, isChecked ->
            addMarcadores()
        }
        buracoCB.setOnCheckedChangeListener { buttonView, isChecked ->
            addMarcadores()
        }

        distanciaSL.addOnChangeListener { slider, value, fromUser ->
            addMarcadores()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        addMarcadores()

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            mMap.isMyLocationEnabled = true
        }

        val customInfoWindow = MarcadorCustom(this)

        mMap!!.setInfoWindowAdapter(customInfoWindow)

        //val marker = mMap!!.addMarker(markerOptions)
        //marker.tag = info
        //marker.showInfoWindow()
    }

    fun calcularDistancia(
        latitude: Double,
        longitude: Double,
        latitude2: Double,
        longitude2: Double
    ): Float {
        Location.distanceBetween(latitude, longitude, latitude2, longitude2, results)
        return results[0]
    }

    private fun addMarcadores() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1
            )
            return
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    lastLocation = location
                    mMap.clear()
                    Toast.makeText(this, "isChecked.toString()", Toast.LENGTH_SHORT).show()
                    val acidenteCB = findViewById<CheckBox>(R.id.checkBoxAcidente)
                    val obrasCB = findViewById<CheckBox>(R.id.checkBoxObras)
                    val buracoCB = findViewById<CheckBox>(R.id.checkBoxBuraco)
                    val distanciaSL = findViewById<Slider>(R.id.distanciaSli)

                    val request = ServiceBuilder.buildService(EndPoints::class.java)
                    val call = request.getReports()
                    var position: LatLng

                    call.enqueue(object : Callback<List<Report>> {
                        override fun onResponse(
                            call: Call<List<Report>>,
                            response: Response<List<Report>>
                        ) {
                            if (response.isSuccessful) {
                                reports = response.body()!!
                                for (report in reports) {
                                    Log.e("dfghj", report.id_tipo.toString())
                                    val info = InfoMarcador(
                                        report.titulo,
                                        report.descricao,
                                        report.id_tipo,
                                        report.imagem
                                    )

                                    if ((calcularDistancia(
                                            location.latitude,
                                            location.longitude,
                                            report.latitude,
                                            report.longitude
                                        ) <= distanciaSL.value) && (acidenteCB.isChecked && report.id_tipo == 1) || (obrasCB.isChecked && report.id_tipo == 2) || (buracoCB.isChecked && report.id_tipo == 3)
                                    ) {
                                        position = LatLng(report.latitude, report.longitude)

                                        val markerOptions = MarkerOptions()
                                        markerOptions.position(position)
                                            .title(report.titulo)
                                            .snippet(report.descricao)
                                            .icon(
                                                BitmapDescriptorFactory.defaultMarker(
                                                    BitmapDescriptorFactory.HUE_YELLOW
                                                )
                                            )
                                        val marcador = mMap.addMarker(markerOptions)
                                        marcador.tag = info
                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<List<Report>>, t: Throwable) {
                            Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_SHORT)
                                .show()
                        }
                    })
                }
            }
        }


    }

    companion object {
        // add to implement last known location
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1

        //added to implement location periodic updates
        private const val REQUEST_CHECK_SETTINGS = 2
    }

    fun addNovoReport(view: View) {
        val intent = Intent(applicationContext, CriarReportActivity::class.java)
        startActivity(intent)
    }

    fun logout(view: View) {
        val sharedPref: SharedPreferences = getSharedPreferences("LoginSP", Context.MODE_PRIVATE)
        with(sharedPref.edit()){
            putBoolean("estadoLogin", false)
            putString("usernameLogin", "")
            commit()
        }
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
        finish()

    }

}