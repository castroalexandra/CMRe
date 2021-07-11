package com.example.cmre

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.io.File
import java.util.*


class MarcadorCustom(val context: Context) : GoogleMap.InfoWindowAdapter {
    override fun getInfoWindow(p0: Marker): View? {
        return null
    }

    override fun getInfoContents(p0: Marker): View? {

        var mInfoView = (context as Activity).layoutInflater.inflate(R.layout.marcador, null)
        var mInfo: InfoMarcador? = p0?.tag as InfoMarcador?

        val titulo = mInfoView.findViewById<TextView>(R.id.tituloMarcadorTV)
        val descricao = mInfoView.findViewById<TextView>(R.id.descricaoMarcadorET)
        val tipo = mInfoView.findViewById<TextView>(R.id.tipoMarcadorET)
        val imagem = mInfoView.findViewById<ImageView>(R.id.imagemMarcador)

        titulo.text = mInfo!!.titulo
        descricao.text = mInfo!!.descricao

        var nomeTipo = ""

        when (mInfo!!.tipo) {
            1 -> nomeTipo = "Acidente"
            2 -> nomeTipo = "Obras"
            3 -> nomeTipo = "Buraco"
        }

        tipo.text = nomeTipo


        val imageByteArray = Base64.getDecoder().decode(mInfo!!.imagem)
        val bmp = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
        imagem.setImageBitmap(bmp)

        return mInfoView
    }

}