package com.example.cmre.api

data class Report (
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val descricao: String,
    val imagem: String,
    val users_id: Int,
    val titulo: String,
    val tipo_id: Int )

