package com.example.cmre

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.cmre.adapters.DESCRICAO
import com.example.cmre.adapters.TITULO
import com.example.cmre.entities.Nota
import com.example.cmre.viewmodels.NotasViewModel

class AtualizarNotaActivity : AppCompatActivity() {

    private lateinit var update_titulo: EditText
    private lateinit var update_descricao: EditText
    private lateinit var notaViewModel: NotasViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_atualizar_nota)


        val titulo = intent.getStringExtra(TITULO)
        val descricao = intent.getStringExtra(DESCRICAO)


        findViewById<EditText>(R.id.tituloET).setText(titulo)
        findViewById<EditText>(R.id.descricaoET).setText(descricao)

        notaViewModel = ViewModelProvider(this).get(NotasViewModel::class.java)

    }

    fun editarNota(view: View) {
        update_titulo = findViewById(R.id.tituloET)
        update_descricao = findViewById(R.id.descricaoET)
        var item = intent.getIntExtra(Build.ID, 0)
        val replyIntent = Intent()
        if (TextUtils.isEmpty(update_titulo.text) || TextUtils.isEmpty(update_descricao.text)) {
            if (TextUtils.isEmpty((update_titulo.text)) && !TextUtils.isEmpty((update_descricao.text))) {
                update_titulo.error = "Campo Obrigatorio"
            }

            if (!TextUtils.isEmpty((update_titulo.text)) && TextUtils.isEmpty((update_descricao.text))) {
                update_descricao.error  = "Campo Obrigatorio"
            }

            if (TextUtils.isEmpty((update_titulo.text)) && TextUtils.isEmpty((update_descricao.text))) {
                update_titulo.error = "Campo Obrigatorio"
                update_descricao.error = "Campo Obrigatorio"
            }


        } else {
            val nota = Nota(
                id = item,
                titulo = update_titulo.text.toString(),
                descricao = update_descricao.text.toString()
            )


            notaViewModel.editNota(nota)

            Toast.makeText(applicationContext, "Atualizada com sucesso", Toast.LENGTH_LONG).show()

            finish()

        }

    }
}