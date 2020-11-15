package com.example.guitapp.auth.data.local

import android.content.Context
import androidx.room.*
import com.example.guitapp.auth.data.TokenHolder
import kotlinx.coroutines.CoroutineScope

@Database(entities = [TokenHolder::class], version = 1)
abstract class TokenDatabase : RoomDatabase() {

    abstract fun tokenDao(): TokenDao

    companion object {

        @Volatile
        private var INSTANCE: TokenDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): TokenDatabase {
            val inst = INSTANCE
            if (inst != null) {
                return inst
            }
            val instance =
                Room.databaseBuilder(
                    context.applicationContext,
                    TokenDatabase::class.java,
                    "token_db"
                )
                    .build()
            INSTANCE = instance
            return instance
        }
    }
}
