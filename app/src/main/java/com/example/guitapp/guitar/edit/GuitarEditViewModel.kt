package com.example.guitapp.guitar.edit

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.guitapp.guitar.data.Guitar
import com.example.guitapp.guitar.data.GuitarRepository
import com.example.guitapp.guitar.data.local.GuitarDatabase
import com.example.guitapp.guitar.data.local.GuitarDatabase.Companion.getDatabase
import kotlinx.coroutines.launch

class GuitarEditViewModel(application: Application) : AndroidViewModel(application) {

    private val mutableFetching = MutableLiveData<Boolean>().apply { value = false }
    private val mutableCompleted = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val fetching: LiveData<Boolean> = mutableFetching
    val fetchingError: LiveData<Exception> = mutableException
    val completed: LiveData<Boolean> = mutableCompleted

    val guitarRepository: GuitarRepository


    init {
        val guitarDao = GuitarDatabase.getDatabase(application, viewModelScope).guitarDao();
        guitarRepository = GuitarRepository(guitarDao)
    }

    fun getItemById(itemId: String): LiveData<Guitar> {
        Log.v(javaClass.name, "getItemById...")
        return guitarRepository.getById(itemId)
    }

    fun saveOrUpdate(item: Guitar) {
        viewModelScope.launch {
            Log.v(javaClass.name, "saveOrUpdateItem...");
            mutableFetching.value = true
            mutableException.value = null
            try {
                if (item._id.isNotEmpty()) {
                    guitarRepository.update(item)
                } else {
                    guitarRepository.save(item)
                }
            }
            catch (e: Exception) {
                Log.w(javaClass.name, "saveOrUpdateItem failed", e);
                mutableException.value = e
            }
            mutableCompleted.value = true
            mutableFetching.value = false
        }
    }

    fun delete(guitar: Guitar) {
        viewModelScope.launch {
            Log.v(javaClass.name, "delete...");
            mutableFetching.value = true
            mutableException.value = null
            try {
                guitarRepository.delete(guitar);
            }
            catch (e: Exception) {
                Log.w(javaClass.name, "delete failed", e);
                mutableException.value = e
            }
            mutableCompleted.value = true
            mutableFetching.value = false
        }
    }
}