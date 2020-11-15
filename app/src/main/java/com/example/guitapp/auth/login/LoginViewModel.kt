package com.example.guitapp.auth.login

import android.app.Application
import androidx.lifecycle.*
import com.example.guitapp.R
import com.example.guitapp.auth.data.AuthRepository
import com.example.guitapp.auth.data.User
import com.example.guitapp.auth.data.local.TokenDatabase
import com.example.guitapp.core.Api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val mutableLoginFormState = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = mutableLoginFormState

    private val mutableLoginResult = MutableLiveData<Any>()
    val loginResult: LiveData<Any> = mutableLoginResult

    init {
        val tokenDao = TokenDatabase.getDatabase(application, viewModelScope).tokenDao();
        AuthRepository.tokenDao = tokenDao
        mutableLoginResult.value = tokenDao.getAll()
    }

    fun login(username: String, password: String) {
        println(username)
        viewModelScope.launch {
            try {
                mutableLoginResult.value = AuthRepository.login(username, password)
            } catch (e: Exception) {
                mutableLoginResult.value = e
            }
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            mutableLoginFormState.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            mutableLoginFormState.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            mutableLoginFormState.value = LoginFormState(isDataValid = true)
        }
    }

    private fun isUserNameValid(username: String): Boolean {
        return username.isNotBlank()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 1
    }
}