package com.example.recall_dialogue;

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class MainActivity : AppCompatActivity(){
    lateinit var webStocketListener: Eachitem
    lateinit var mainViewHolder: Custom_dailogue
    private val okHttpClient= OkHttpClient()
    private var webSocket: WebSocket?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnConnect=findViewById<Button>(R.id.connect)
        val btnDisconnnect=findViewById<Button>(R.id.disconnect)
        val tvMessage=findViewById<TextView>(R.id.message)
        val editMessage=findViewById<EditText>(R.id.editMessage)
        val btnSend=findViewById<Button>(R.id.send)

        mainViewHolder= ViewModelProvider(this)[Custom_dailogue::class.java]
        webStocketListener= Eachitem(mainViewHolder)

        mainViewHolder.socketStatus.observe(this, Observer{isConnect ->
            tvMessage.text=if (isConnect)"Connect" else "Disconnected"
        })
        var chatHistory=""
      mainViewHolder.message.observe(this, Observer{pair ->
          chatHistory +="${if(pair.first)"other:" else "You: "}${pair.second}\n"
          tvMessage.text=chatHistory
      })
        btnConnect.setOnClickListener {
            if (webSocket==null){
            webSocket=okHttpClient.newWebSocket(createRequest(), webStocketListener)
            }
        }
        btnDisconnnect.setOnClickListener {
            webSocket?.close(1000,"Cancelled Manually")
            webSocket=null
        }
        btnSend.setOnClickListener {
            val message=editMessage.text.toString()
            if (message.isNotEmpty()&& webSocket !=null){
                webSocket?.send(message)
                chatHistory+="You:$message\n"
                tvMessage.text=chatHistory
                editMessage.text.clear()
            }
        }



    }

    fun createRequest(): Request {
        val webSocketUrl = "wss://s18475.nyc1.piesocket.com/v3/1?api_key=h7inTrnho3OthWRSNMoKOYT36P56kNBLyrCY8MIt&notify_self=1"
        return Request.Builder()
            .url(webSocketUrl)
            .build()
    }

}
