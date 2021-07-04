package com.example.cmre

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class CriarNotaActivity  : AppCompatActivity() {
    private lateinit var tituloText: EditText
    private lateinit var descricaoText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criar_nota)

        tituloText = findViewById(R.id.tituloET)
        descricaoText = findViewById(R.id.descricaoET)


        if (intent.getStringExtra(EXTRA_REPLY_TITULO).isNullOrEmpty() && intent.getStringExtra(EXTRA_REPLY_DESCRICAO).isNullOrEmpty()) {

            val button = findViewById<Button>(R.id.button)
            button.setOnClickListener {

                if (TextUtils.isEmpty((tituloText.text)) || TextUtils.isEmpty((descricaoText.text))) {

                    if (TextUtils.isEmpty(tituloText.text) && !TextUtils.isEmpty(descricaoText.text)) {
                        tituloText.error = "Titulo"
                    }

                    if (!TextUtils.isEmpty(descricaoText.text) && TextUtils.isEmpty(descricaoText.text)) {
                        descricaoText.error = "Descricao"
                    }

                    if (TextUtils.isEmpty(tituloText.text) && TextUtils.isEmpty(descricaoText.text)) {
                        tituloText.error = "Titulo"
                        descricaoText.error = "Descricao"
                    }
                } else {
                    val replyIntent = Intent()

                    replyIntent.putExtra(EXTRA_REPLY_TITULO, tituloText.text.toString())
                    replyIntent.putExtra(EXTRA_REPLY_DESCRICAO, descricaoText.text.toString())
                    setResult(Activity.RESULT_OK, replyIntent)

                    finish()
                }
            }
        }

    }

    companion object {
        const val EXTRA_REPLY_TITULO = ""
        const val EXTRA_REPLY_DESCRICAO = ""
    }
}