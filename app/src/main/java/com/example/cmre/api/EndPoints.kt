package com.example.cmre.api

import retrofit2.Call
import retrofit2.http.*

interface EndPoints {
        @FormUrlEncoded
        @POST("MySlim/api/api/user")
        fun login(@Field("username") first: String?, @Field("password") second: String?): Call<OutputPost>

        @GET("/MySlim/api/api/report")
        fun getReports(): Call<List<Report>>
}