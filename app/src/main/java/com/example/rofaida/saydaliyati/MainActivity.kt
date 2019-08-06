package com.example.rofaida.saydaliyati

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit



class MainActivity : AppCompatActivity() {

    private lateinit var fragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragmentManager = supportFragmentManager
        // If savedinstnacestate is null then replace login fragment
        if (savedInstanceState == null) {
            fragmentManager!!.beginTransaction()
                .replace(R.id.frameContainer, Login_Fragment(),
                    Utils.Login_Fragment
                ).commit()
        }

    }

    // Replace Login Fragment with animation
    public fun replaceLoginFragment() {
       if (!isFinishing && !isDestroyed) {
            fragmentManager = supportFragmentManager
            fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(
                    R.id.frameContainer, Login_Fragment(),
                    Utils.Login_Fragment
                ).commit()
        }else
            Toast.makeText(
                this.applicationContext, "Destroyed",
                Toast.LENGTH_SHORT
            ).show()
    }

    override fun onBackPressed() {

        // Find the tag of signup and forgot password fragment
        val SignUp_Fragment = fragmentManager.findFragmentByTag(Utils.SignUp_Fragment)
        val ForgotPassword_Fragment = fragmentManager.findFragmentByTag(Utils.ForgotPassword_Fragment)

        // Check if both are null or not
        // If both are not null then replace login fragment else do backpressed
        // task

        if (SignUp_Fragment != null)
            replaceLoginFragment()
        else if (ForgotPassword_Fragment != null)
            replaceLoginFragment()
        else
            super.onBackPressed()
    }


}
