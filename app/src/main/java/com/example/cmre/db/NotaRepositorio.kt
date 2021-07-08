package com.example.cmre.db

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.cmre.dao.NotasDao
import com.example.cmre.entities.Nota

class NotaRepositorio(private val notasDao: NotasDao) {

    val allNotas: LiveData<List<Nota>> = notasDao.getAllNotas()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread


    suspend fun inserir(nota: Nota) {
        notasDao.insert(nota)
    }

    suspend fun apagarTudo(){
        notasDao.deleteAll()
    }


    suspend fun  atualizar(nota: Nota) {
        notasDao.updateNota(nota)
    }

    suspend fun apagarNota(id: Int?) {
        notasDao.deleteById(id)
    }

}