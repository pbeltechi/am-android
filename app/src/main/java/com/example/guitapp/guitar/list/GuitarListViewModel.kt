package com.example.guitapp.guitar.list

import android.app.Application
import android.net.ConnectivityManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.guitapp.auth.data.AuthRepository
import com.example.guitapp.auth.data.local.TokenDatabase
import com.example.guitapp.guitar.data.Guitar
import com.example.guitapp.guitar.data.GuitarRepository
import com.example.guitapp.guitar.data.local.GuitarDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GuitarListViewModel(application: Application) : AndroidViewModel(application) {

    private val mutableLoading = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val items: LiveData<List<Guitar>>
    val loading: LiveData<Boolean> = mutableLoading
    val loadingError: LiveData<Exception> = mutableException

    private val guitarRepository: GuitarRepository

    init {
        val tokenDao = TokenDatabase.getDatabase(application, viewModelScope).tokenDao()
        AuthRepository.tokenDao = tokenDao
        val guitarDao = GuitarDatabase.getDatabase(application, viewModelScope).guitarDao()
        guitarRepository = GuitarRepository(guitarDao)
        items = guitarRepository.items
    }

    fun refresh() {
        viewModelScope.launch {
            Log.v(javaClass.name, "refresh...");
            mutableLoading.value = true
            mutableException.value = null
            try {
                guitarRepository.refresh()
            } catch (e: Exception) {
                Log.e(javaClass.name, e.toString())
                mutableException.value = e
            }
            mutableLoading.value = false
        }
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            AuthRepository.logout()
        }
    }
}