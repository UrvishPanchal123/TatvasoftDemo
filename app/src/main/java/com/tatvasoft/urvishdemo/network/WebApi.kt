package com.tatvasoft.urvishdemo.network

import com.tatvasoft.urvishdemo.model.UserModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WebApi {

    @GET("api/users")
    fun getUserList(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
    ): Call<UserModel>
}