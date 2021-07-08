package com.example.cmre.api

import retrofit2.Call
import retrofit2.http.*

interface EndPoints {
        @FormUrlEncoded
        @POST("/myslim/api/user")
        fun login(@Field("username") first: String?, @Field("password") second: String?): Call<OutputPost>
}