package com.example.guitapp.guitar.data

import androidx.lifecycle.LiveData
import com.example.guitapp.MainActivity
import com.example.guitapp.guitar.data.local.GuitarDao
import com.example.guitapp.guitar.data.remote.GuitarApi


class GuitarRepository(private val guitarDao: GuitarDao) {

    val items = guitarDao.getAll()

    init {
        GuitarApi.myWebSocketListener.guitarDao = guitarDao
    }

    suspend fun refresh() {
        if (MainActivity.networkStatusIsConnected) {
            val items = GuitarApi.service.find()
            for (item in items) {
                guitarDao.insert(item)
            }
        }
    }

    fun getById(itemId: String): LiveData<Guitar> {
        return guitarDao.getById(itemId)
    }

    suspend fun save(guitar: Guitar): Guitar {
        var saveGuitar = guitar
        if (MainActivity.networkStatusIsConnected) {
            saveGuitar = GuitarApi.service.create(guitar)
        }
        guitarDao.insert(saveGuitar)
        return saveGuitar
    }

    suspend fun update(guitar: Guitar): Guitar {
        var saveGuitar = guitar
        if (MainActivity.networkStatusIsConnected) {
            saveGuitar =  GuitarApi.service.update(guitar._id, guitar)
        }
        guitarDao.update(saveGuitar)
        return saveGuitar
    }

    suspend fun delete(guitar: Guitar): Guitar {
        var saveGuitar = guitar
        if (MainActivity.networkStatusIsConnected) {
            saveGuitar = GuitarApi.service.delete(guitar._id)
        }
        guitarDao.delete(saveGuitar)
        return saveGuitar
    }
}