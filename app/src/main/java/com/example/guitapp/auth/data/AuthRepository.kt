package com.example.guitapp.auth.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.guitapp.auth.data.local.TokenDao
import com.example.guitapp.auth.data.remote.AuthApi
import com.example.guitapp.core.Api
import com.example.guitapp.guitar.data.remote.GuitarApi
import java.util.*

object AuthRepository {

    var user: User? = null
        private set

    val storedToken: LiveData<TokenHolder>
        get() {
            if (tokenDao != null) {
                return tokenDao!!.getOne()
            }
            return MutableLiveData<TokenHolder>(null)
        }

    var tokenDao: TokenDao? = null
        set(value) {
            field = value
        }

    init {
        user = null
    }

    suspend fun logout() {
        user = null
        Api.tokenInterceptor.token = null
        tokenDao?.deleteAll()
    }

    suspend fun login(username: String, password: String): TokenHolder {
        val user = User(username, password)
        val tokenHolder = AuthApi.login(user)
        setLoggedInUser(tokenHolder, user)
        tokenDao?.insert(tokenHolder)
        return tokenHolder
    }

    private fun setLoggedInUser(tokenHolder: TokenHolder, user: User) {
        this.user = user
        Api.tokenInterceptor.token = tokenHolder.token
        GuitarApi.connectWs()
    }

    fun login(tokenHolder: TokenHolder) {
        this.setLoggedInUser(tokenHolder, User("", ""))
    }
}
