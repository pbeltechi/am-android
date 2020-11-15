package com.example.guitapp.auth.data

import com.example.guitapp.auth.data.local.TokenDao
import com.example.guitapp.auth.data.remote.AuthApi
import com.example.guitapp.core.Api
import java.util.*

object AuthRepository {

    var user: User? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    var tokenDao: TokenDao? = null
        set(value) {
            field = value
        }

    init {
        user = null
    }

    fun logout() {
        user = null
        Api.tokenInterceptor.token = null
    }

    suspend fun login(username: String, password: String): TokenHolder {
        val user = User(username, password)
        val tokenHolder = AuthApi.login(user)
        setLoggedInUser(tokenHolder, user)
        tokenDao?.insert(tokenHolder)
        return tokenHolder
    }

    fun setLoggedInUser(tokenHolder: TokenHolder, user: User?) {
        if (user === null) {
            this.user = User("", "")
        } else {
            this.user = user
        }
        Api.tokenInterceptor.token = tokenHolder.token
    }
}
