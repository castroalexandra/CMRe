package com.example.cmre.api

import retrofit2.Call
import retrofit2.http.*

interface EndPoints {
        @FormUrlEncoded
        @POST("MySlim/api/api/user")
        fun login(@Field("username") first: String?, @Field("password") second: String?): Call<OutputPost>

        @GET("/MySlim/api/api/report")
        fun getReports(): Call<List<Report>>


        @FormUrlEncoded
        @POST("/MySlim/api/api/inserir_report")
        fun adicionarReport(@Field("latitude") latitude: Double?,
                            @Field("longitude") longitude: Double?,
                            @Field("descricao") descricao: String?,
                            @Field("imagem") imagem: String?,
                            @Field("id_utilizador") users_id: Int?,
                            @Field("titulo") titulo: String?,
                            @Field("id_tipo") id_tipo: Int?
        ): Call<OutputReports>




}