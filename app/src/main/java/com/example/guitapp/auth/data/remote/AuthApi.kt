package com.example.guitapp.auth.data.remote

import com.example.guitapp.auth.data.TokenHolder
import com.example.guitapp.auth.data.User
import com.example.guitapp.core.Api
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

object AuthApi {
    interface AuthService {
        @Headers("Content-Type: application/json")
        @POST("/api/auth/login")
        suspend fun login(@Body user: User): TokenHolder
    }

    private val authService: AuthService = Api.retrofit.create(AuthService::class.java)

    suspend fun login(user: User): TokenHolder {
        return authService.login(user)
    }
}

