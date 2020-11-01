package com.example.guitapp.guitar.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.guitapp.guitar.data.Guitar
import retrofit2.http.DELETE

@Dao
interface GuitarDao {

    @Query("SELECT * from guitars ORDER BY model ASC")
    fun getAll(): LiveData<List<Guitar>>

    @Query("SELECT * FROM guitars WHERE _id=:id ")
    fun getById(id: String): LiveData<Guitar>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(guitar: Guitar)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(guitar: Guitar)

    @Delete
    suspend fun delete(guitar: Guitar)

    @Query("DELETE FROM guitars")
    suspend fun deleteAll()
}