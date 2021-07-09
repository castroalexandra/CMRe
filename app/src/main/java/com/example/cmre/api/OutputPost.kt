package com.example.cmre.api

data class OutputPost(
    val status: Boolean,
    val MSG: String,
    val username: String,
    val id: Int,
)

    data class OutputReports(
val id: Int,
val latitude: Double,
val longitude: Double,
val descricao: String,
val imagem: String,
val id_utilizador: Int,
val titulo: String,
val id_tipo: Int
)