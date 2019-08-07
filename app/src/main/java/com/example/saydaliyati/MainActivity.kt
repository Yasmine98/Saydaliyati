package com.example.saydaliyati

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity(), pharmacieFragment.OnListFragmentInteractionListener,
    mapFragment.OnFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val i : Int
        val u : Int
      //  R.id.frame_container.getMapAsync(this)
        if (savedInstanceState == null) {
            // 2
            supportFragmentManager
                // 3
                .beginTransaction()
                // 4
                .add(R.id.frame_container, pharmacieFragment.newInstance(1, "Alger"), "pharmaList")
                // 5
                .commit()
        }

    }

    override fun onListFragmentInteraction(item: pharmacie?){
        Log.e("erreur frag ", "enter")
            // 2
        Toast.makeText(this.applicationContext, "helloo"+item?.id, Toast.LENGTH_LONG).show()
            val f = pharmaDetailFragment.newInstance(item) as Fragment
           replaceFragment(f)


    }

    override fun onFragmentInteraction(uri: String){
        Log.e("erreur frag ", "enter")

    }
    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment)
        transaction.commit()
    }
}
