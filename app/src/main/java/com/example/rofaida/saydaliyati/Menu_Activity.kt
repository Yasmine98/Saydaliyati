package com.example.rofaida.saydaliyati

import android.annotation.SuppressLint
import android.app.PendingIntent.getActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.rofaida.saydaliyati.Models.User_details

class Menu_Activity : AppCompatActivity() {

    private lateinit var AddCmdBtn_: Button
    private lateinit var ShowCmdBtn_: Button

    private lateinit var user: User_details

    private lateinit var fragmentManager: FragmentManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_)
        user = intent.getSerializableExtra("user") as User_details
        fragmentManager = supportFragmentManager
        // If savedinstnacestate is null then replace login fragment
        if (savedInstanceState == null) {
            var bundle:Bundle = Bundle()
            bundle.putSerializable("user", user)
            val fragment_new:Fragment = Menu_fragment()
            fragment_new.arguments = bundle
            fragmentManager!!.beginTransaction()
                .replace(R.id.frameContainer, fragment_new,
                    Utils.Menu_fragment
                ).commit()
        }
    }

    public fun replaceMenuFragment() {
        if (!isFinishing && !isDestroyed) {
            fragmentManager = supportFragmentManager
            var bundle:Bundle = Bundle()
            bundle.putSerializable("user", user)
            val fragment_new:Fragment = Menu_fragment()
            fragment_new.arguments = bundle
            fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(
                    R.id.frameContainer, fragment_new,
                    Utils.Menu_fragment
                ).commit()
        }else
            Toast.makeText(
                this.applicationContext, "Destroyed",
                Toast.LENGTH_SHORT
            ).show()
    }

    override fun onBackPressed() {

        val Menu_Fragment_ = fragmentManager.findFragmentByTag(Utils.Menu_fragment)

        // Check if both are null or not
        // If both are not null then replace login fragment else do backpressed
        // task

        if (Menu_Fragment_ != null)
            replaceMenuFragment()
        else
            super.onBackPressed()
    }


}
