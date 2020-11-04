package com.example.guitapp.guitar.data.local

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.guitapp.guitar.data.Guitar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Guitar::class], version = 1)
abstract class GuitarDatabase : RoomDatabase() {

    abstract fun guitarDao(): GuitarDao

    companion object {

        @Volatile
        private var INSTANCE: GuitarDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): GuitarDatabase {
            val inst = INSTANCE
            if (inst != null) {
                return inst
            }
            val instance =
                Room.databaseBuilder(
                    context.applicationContext,
                    GuitarDatabase::class.java,
                    "guitar_db"
                )
                    .addCallback(WordDatabaseCallback(scope))
                    .build()
            INSTANCE = instance
            return instance
        }

        private class WordDatabaseCallback(private val scope: CoroutineScope) :
            RoomDatabase.Callback() {

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        clearDatabase(database.guitarDao())
                    }
                }
            }
        }

        suspend fun clearDatabase(guitarDao: GuitarDao) {
            guitarDao.deleteAll()
        }
    }
}
