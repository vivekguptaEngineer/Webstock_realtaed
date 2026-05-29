package com.example.recall_dialogue

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket

class MainActivity : AppCompatActivity() {
    private lateinit var webSocketListener: Eachitem
    private lateinit var mainViewModel: MainViewModel
    private val okHttpClient = OkHttpClient()
    private var webSocket: WebSocket? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnConnect = findViewById<Button>(R.id.connect)
        val btnDisconnect = findViewById<Button>(R.id.disconnect)
        val tvMessage = findViewById<TextView>(R.id.message)
        val editMessage = findViewById<EditText>(R.id.editMessage)
        val btnSend = findViewById<Button>(R.id.send)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        webSocketListener = Eachitem(mainViewModel)

        mainViewModel.socketStatus.observe(this, Observer { isConnected ->
            tvMessage.text = if (isConnected) "Connected" else "Disconnected"
        })

        var chatHistory = ""
        mainViewModel.message.observe(this, Observer { pair ->
            chatHistory += "${if (pair.first) "Other: " else "You: "}${pair.second}\n"
            tvMessage.text = chatHistory
        })

        btnConnect.setOnClickListener {
            if (webSocket == null) {
                webSocket = okHttpClient.newWebSocket(createRequest(), webSocketListener)
            }
        }

        btnDisconnect.setOnClickListener {
            webSocket?.close(1000, "Cancelled Manually")
            webSocket = null
        }

        btnSend.setOnClickListener {
            val message = editMessage.text.toString()
            if (message.isNotEmpty() && webSocket != null) {
                webSocket?.send(message)
                chatHistory += "You: $message\n"
                tvMessage.text = chatHistory
                editMessage.text.clear()
            }
        }
    }

    private fun createRequest(): Request {
        val webSocketUrl = "wss://s18475.nyc1.piesocket.com/v3/1?api_key=h7inTrnho3OthWRSNMoKOYT36P56kNBLyrCY8MIt&notify_self=1"
        return Request.Builder()
            .url(webSocketUrl)
            .build()
    }
}
