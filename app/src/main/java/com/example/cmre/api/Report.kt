package com.example.cmre.api

data class Report (
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val descricao: String,
    val imagem: String,
    val id_utilizador: Int,
    val titulo: String,
    val id_tipo: Int )

