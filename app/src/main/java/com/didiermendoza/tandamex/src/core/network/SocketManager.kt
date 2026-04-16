package com.didiermendoza.tandamex.core.network

import android.util.Log
import com.didiermendoza.tandamex.BuildConfig
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.json.JSONObject
import java.net.URI

class SocketManager {
    private var socket: Socket? = null

    private val _countdownEvent = MutableSharedFlow<Int>(extraBufferCapacity = 1)
    val countdownEvent = _countdownEvent.asSharedFlow()

    private val _sorteoResultEvent = MutableSharedFlow<JSONObject>(extraBufferCapacity = 1)
    val sorteoResultEvent = _sorteoResultEvent.asSharedFlow()

    fun connectToTanda(tandaId: Int) {
        try {
            val uri = URI(BuildConfig.BASE_URL)
            val portString = if (uri.port != -1) ":${uri.port}" else ""
            val socketUrl = "${uri.scheme}://${uri.host}$portString"

            val options = IO.Options().apply {
                forceNew = true
                reconnection = true
                transports = arrayOf("websocket")
            }

            Log.d("SOCKET_TANDA", "Intentando conectar a: $socketUrl")
            socket = IO.socket(socketUrl, options)

            socket?.on(Socket.EVENT_CONNECT) {
                Log.d("SOCKET_TANDA", "¡Conectado exitosamente al servidor Socket.IO!")
                socket?.emit("joinTanda", tandaId)
            }

            socket?.on(Socket.EVENT_CONNECT_ERROR) { args ->
                Log.e("SOCKET_TANDA", "Error al conectar Socket.IO: ${args.firstOrNull()}")
            }

            socket?.on("sorteoIniciado") { args ->
                Log.d("SOCKET_TANDA", "Evento recibido: sorteoIniciado")
                if (args.isNotEmpty()) {
                    val data = args[0] as JSONObject
                    val countdown = data.optInt("countdown", 30)
                    _countdownEvent.tryEmit(countdown)
                }
            }

            socket?.on("sorteoFinalizado") { args ->
                Log.d("SOCKET_TANDA", "Evento recibido: sorteoFinalizado")
                if (args.isNotEmpty()) {
                    val data = args[0] as JSONObject
                    _sorteoResultEvent.tryEmit(data)
                }
            }

            socket?.connect()
        } catch (e: Exception) {
            Log.e("SOCKET_TANDA", "Excepción al configurar Socket.IO: ${e.message}")
            e.printStackTrace()
        }
    }

    fun disconnect() {
        Log.d("SOCKET_TANDA", "Desconectando Socket.IO")
        socket?.disconnect()
        socket?.off()
        socket = null
    }
}