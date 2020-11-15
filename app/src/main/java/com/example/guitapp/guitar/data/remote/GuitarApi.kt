package com.example.guitapp.guitar.data.remote

import android.util.Log
import com.example.guitapp.core.Api
import com.example.guitapp.core.Api.WS_URL
import com.example.guitapp.guitar.data.Guitar
import com.example.guitapp.guitar.data.WebSocketEvent
import com.example.guitapp.guitar.data.local.GuitarDao
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import okhttp3.*
import retrofit2.http.*
import retrofit2.http.Headers

object GuitarApi {

    interface Service {
        @Headers("Authorization: Bearer ")
        @GET("/api/guitars")
        suspend fun find(): List<Guitar>

        @GET("/api/guitars/{id}")
        suspend fun read(@Path("id") itemId: String): Guitar;

        @Headers("Content-Type: application/json")
        @POST("/api/guitars")
        suspend fun create(@Body item: Guitar): Guitar

        @Headers("Content-Type: application/json")
        @PUT("/api/guitars/{id}")
        suspend fun update(@Path("id") itemId: String, @Body item: Guitar): Guitar

        @Headers("Content-Type: application/json")
        @DELETE("/api/guitars/{id}")
        suspend fun delete(@Path("id") itemId: String): Guitar
    }

    val service: Service = Api.retrofit.create(Service::class.java)
    val myWebSocketListener: MyWebSocketListener

    init {
        val request = Request.Builder().url(WS_URL).build()
        myWebSocketListener =  MyWebSocketListener()
        OkHttpClient().newWebSocket(request, myWebSocketListener)
    }

    class MyWebSocketListener : WebSocketListener() {

        lateinit var guitarDao: GuitarDao

        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.d("WebSocket", "onOpen")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            if (::guitarDao.isInitialized) {
                runBlocking { collectWebSocketEvent(text) }
            } else {
                Log.w("WebSocket", "received $text but Dao is not initialized")
            }
        }

        suspend fun collectWebSocketEvent(eventText: String) {
            val event = Gson().fromJson<WebSocketEvent>(eventText, WebSocketEvent::class.java)
            Log.i("WebSocket", "received $event")
            when (event.type) {
                "created" -> {
                    guitarDao.insert(event.payload)
                }
                "updated" -> {
                    guitarDao.update(event.payload)
                }
                "deleted" -> {
                    guitarDao.delete(event.payload)
                }
            }
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            Log.d("WebSocket", "onClosing")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.e("WebSocket", "onFailure", t)
            t.printStackTrace()
        }
    }
}