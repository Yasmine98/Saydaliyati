package com.example.saydaliyati

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)
        val editText = findViewById(R.id.edit) as MyEditText
        editText.setEnabled(false);
        editText.setClickable(false);

    }
}
