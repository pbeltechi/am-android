package com.example.guitapp.auth.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.guitapp.auth.data.TokenHolder

@Dao
interface TokenDao {
    @Query("SELECT * from tokens")
    fun getAll(): LiveData<List<TokenHolder>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(token: TokenHolder)

    @Query("DELETE FROM tokens")
    suspend fun deleteAll()
}