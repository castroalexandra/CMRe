package com.example.cmre

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cmre.adapters.NotasAdapter
import com.example.cmre.entities.Nota
import com.example.cmre.viewmodels.NotasViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NotasActivity : AppCompatActivity(), NotasAdapter.EnviarInformacao {


    private lateinit var notasViewModel: NotasViewModel
    private val newNotaActivityRequestCode = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notas)


        val recyclerView = findViewById<RecyclerView>(R.id.lista_notas_rv)
        val adapter = NotasAdapter(this , this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // view model
        notasViewModel = ViewModelProvider(this).get(NotasViewModel::class.java)
        notasViewModel.allNotas.observe(this, Observer { notas ->
            // Update the cached copy of the words in the adapter.
            notas?.let { adapter.setNotas(it) }
        })


        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@NotasActivity, CriarNotaActivity::class.java)
            startActivityForResult(intent, newNotaActivityRequestCode)
        }

        recyclerView.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
    }

    override fun sendID(id: Int?) {
        notasViewModel.deleteByID(id)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newNotaActivityRequestCode && resultCode == Activity.RESULT_OK) {
            val ptitulo = data?.getStringExtra(CriarNotaActivity.EXTRA_REPLY_TITULO)
            val pdescricao = data?.getStringExtra(CriarNotaActivity.EXTRA_REPLY_DESCRICAO)

            if (ptitulo != null) {
                Log.e("titulo", ptitulo)
            }
            if (pdescricao != null) {
                Log.e("descricao", pdescricao)
            }


            if (ptitulo!= null && pdescricao != null) {
                val nota = Nota(titulo = ptitulo, descricao = pdescricao)

                // FUNÇÃO QUE FAZ O INSERT
                notasViewModel.insert(nota)

                Toast.makeText(applicationContext,"Inseriu com sucesso", Toast.LENGTH_LONG).show()
            }

        }
    }
}