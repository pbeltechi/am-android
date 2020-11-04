package com.example.guitapp.guitar.data

import androidx.lifecycle.LiveData
import com.example.guitapp.guitar.data.local.GuitarDao
import com.example.guitapp.guitar.data.remote.GuitarApi

class GuitarRepository(private val guitarDao: GuitarDao) {

    val items = guitarDao.getAll()

    init {
        GuitarApi.myWebSocketListener.guitarDao = guitarDao
    }

    suspend fun refresh() {
        val items = GuitarApi.service.find()
        for (item in items) {
            guitarDao.insert(item)
        }
    }

    fun getById(itemId: String): LiveData<Guitar> {
        return guitarDao.getById(itemId)
    }

    suspend fun save(guitar: Guitar): Guitar {
        return GuitarApi.service.create(guitar)
    }

    suspend fun update(guitar: Guitar): Guitar {
        return GuitarApi.service.update(guitar._id, guitar)
    }

    suspend fun delete(guitar: Guitar): Guitar {
        return GuitarApi.service.delete(guitar._id)
    }
}