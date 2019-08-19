package com.example.rofaida.saydaliyati

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest

import org.json.JSONException
import org.json.JSONObject

import java.util.HashMap

class MainActivityNotif : AppCompatActivity() {
   //lateinit internal var edtTitle: EditText
 //   lateinit internal var edtMessage: EditText
    private val FCM_API = "https://fcm.googleapis.com/fcm/send"
    private val serverKey = "key=" + "AAAAchI0EIs:APA91bEvHzqvkWQElCoHDrSXknS_LVR1T5A7GTfTmApzKCzKn388ELvTdsSVMl1SF0zULYO6A4Bg9Gh9v_-uV7om80E-EFsu7Y5nQK7UGbbNZLGjTnQyf3jQoZ0A4gN8nax5mlsg7z9A"
    private val contentType = "application/json"
    internal val TAG = "NOTIFICATION TAG"

    lateinit internal var NOTIFICATION_TITLE: String
    lateinit internal var NOTIFICATION_MESSAGE: String
    lateinit internal var TOPIC: String

    override fun  onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)


            TOPIC = "/topics/userABC" //topic has to match what the receiver subscribed to
            NOTIFICATION_TITLE = "hello"
            NOTIFICATION_MESSAGE = "congrats"

            val notification = JSONObject()
            val notifcationBody = JSONObject()
            try {
                notifcationBody.put("title", NOTIFICATION_TITLE)
                notifcationBody.put("message", NOTIFICATION_MESSAGE)

                notification.put("to", TOPIC)
                notification.put("data", notifcationBody)
            } catch (e: JSONException) {
                Log.e(TAG, "onCreate: " + e.message)
            }

            sendNotification(notification)


    }

    private fun sendNotification(notification: JSONObject) {
        val jsonObjectRequest = object : JsonObjectRequest(FCM_API, notification,
            Response.Listener { response ->
                Log.i(TAG, "onResponse: $response")

            },
            Response.ErrorListener {
                Toast.makeText(this@MainActivityNotif, "Request error", Toast.LENGTH_LONG).show()
                Log.i(TAG, "onErrorResponse: Didn't work")
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                params["Authorization"] = serverKey
                params["Content-Type"] = contentType
                return params
            }
        }
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest)
    }
}
