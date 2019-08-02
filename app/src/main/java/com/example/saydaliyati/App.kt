package com.example.saydaliyati

import android.app.Application

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        RoomService.context = applicationContext

    }
}