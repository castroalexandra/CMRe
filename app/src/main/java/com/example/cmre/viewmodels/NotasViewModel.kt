package com.example.cmre.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.cmre.db.NotaRepositorio
import com.example.cmre.db.NotasDB
import com.example.cmre.entities.Nota
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotasViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NotaRepositorio

    val allNotas: LiveData<List<Nota>>

    init {
        val notasDao = NotasDB.getDatabase(application, viewModelScope).notasDao()
        repository = NotaRepositorio(notasDao)
        allNotas = repository.allNotas
    }


    fun insert(nota: Nota) = viewModelScope.launch(Dispatchers.IO) {
        repository.inserir(nota)
    }

    // delete all
    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.apagarTudo()
    }

    fun deleteByID(id: Int?) = viewModelScope.launch(Dispatchers.IO){
        repository.apagarNota(id)
    }

    fun editNota(nota: Nota) = viewModelScope.launch(Dispatchers.IO){
        repository.atualizar(nota)
    }

}
