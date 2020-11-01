package com.example.guitapp.guitar.data

import androidx.lifecycle.LiveData
import com.example.guitapp.guitar.data.local.GuitarDao
import com.example.guitapp.guitar.data.remote.GuitarApi

class GuitarRepository(private val guitarDao: GuitarDao) {

    val items = guitarDao.getAll();

    suspend fun refresh() {
        val items = GuitarApi.service.find()
        for (item in items) {
            guitarDao.insert(item)
        }
    }

    fun getById(itemId: String): LiveData<Guitar> {
        return guitarDao.getById(itemId)
    }

    suspend fun save(guitar: Guitar, local:Boolean = false): Guitar {
        var savedGuitar = guitar
        if(!local) {
            savedGuitar=GuitarApi.service.create(guitar)
        }
        guitarDao.insert(savedGuitar)
        return savedGuitar
    }

    suspend fun update(guitar: Guitar, local:Boolean = false): Guitar {
        var updatedItem = guitar
        if(!local) {
            updatedItem = GuitarApi.service.update(guitar._id, guitar)
        }
        guitarDao.update(updatedItem)
        return updatedItem
    }

    suspend fun delete(guitar: Guitar, local:Boolean = false): Guitar {
        if (!local){
            GuitarApi.service.delete(guitar._id)
        }
        guitarDao.delete(guitar)
        return guitar
    }
}