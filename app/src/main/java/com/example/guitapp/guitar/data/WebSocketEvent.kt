package com.example.guitapp.guitar.data

import com.example.guitapp.auth.data.TokenHolder

class WebSocketEvent(val type: String, val payload: Guitar)
class WsAuth(val type: String, val payload: TokenHolder)