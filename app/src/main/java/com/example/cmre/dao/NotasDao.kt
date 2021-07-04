package com.example.cmre.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.cmre.entities.Nota

@Dao
interface NotasDao {

    @Query("SELECT * FROM nota_table")
    fun getAllNotas(): LiveData<List<Nota>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(nota: Nota)

    @Query("DELETE FROM nota_table")
    suspend fun deleteAll()

    @Update
    suspend fun updateNota(nota: Nota)


    @Query("DELETE FROM nota_table WHERE id==:id")
    suspend fun deleteById(id: Int?)



}