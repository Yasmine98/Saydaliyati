package com.example.saydaliyati

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_master.*
import kotlinx.android.synthetic.main.app_bar_master.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng

class Master : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener , OnMapReadyCallback, pharmacieFragment.OnListFragmentInteractionListener, mapFragment.OnFragmentInteractionListener{
    lateinit private var toggle : ActionBarDrawerToggle
    private var mMap: GoogleMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_master)
       /* val mapFragment = supportFragmentManager
            .findFragmentById(R.id.frame_container) as SupportMapFragment
        mapFragment.getMapAsync(this)*/
        val i : Int
        val u : Int
        if (savedInstanceState == null) {
            // 2
            supportFragmentManager
                // 3
                .beginTransaction()
                // 4
               .add(R.id.frame_container, mapFragment.newInstance("1", "2"), "pharmaList")
                // 5
                .commit()
        }
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.master, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }




    override fun onListFragmentInteraction(item: pharmacie?){
        Log.e("erreur frag ", "enter")
        // 2
        Toast.makeText(this.applicationContext, "helloo"+item?.id, Toast.LENGTH_LONG).show()
      //  drawer_layout.removeDrawerListener()
        val f = pharmaDetailFragment.newInstance(item) as Fragment
        replaceFragment(f)
        enableViews(true)
     /*drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        // Remove hamburger
        toggle.setDrawerIndicatorEnabled(false)
        // Show back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.setToolbarNavigationClickListener( {
                // Doesn't have to be onBackPressed
                onBackPressed()
            enableViews(false)

            }
        )*/

    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment).addToBackStack("tag").commit();

    }

    private fun enableViews(enable: Boolean) {

        // To keep states of ActionBar and ActionBarDrawerToggle synchronized,
        // when you enable on one, you disable on the other.
        // And as you may notice, the order for this operation is disable first, then enable - VERY VERY IMPORTANT.
        if (enable) {
            //You may not want to open the drawer on swipe from the left in this case
            drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            // Remove hamburger
            toggle.setDrawerIndicatorEnabled(false)
            // Show back button
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            // when DrawerToggle is disabled i.e. setDrawerIndicatorEnabled(false), navigation icon
            // clicks are disabled i.e. the UP button will not work.
            // We need to add a listener, as in below, so DrawerToggle will forward
            // click events to this listener.
                toggle.setToolbarNavigationClickListener( {
                        // Doesn't have to be onBackPressed
                        onBackPressed()
                        enableViews(false)
                    }
                )

               // mToolBarNavigationListenerIsRegistered = true


        } else {
            //You must regain the power of swipe for the drawer.
            drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

            // Remove back button
            supportActionBar!!.setDisplayHomeAsUpEnabled(false)
            // Show hamburger
            toggle.setDrawerIndicatorEnabled(true)
            // Remove the/any drawer toggle listener
            toggle.setToolbarNavigationClickListener(null)
          //  mToolBarNavigationListenerIsRegistered = false
        }

        // So, one may think "Hmm why not simplify to:
        // .....
        // getSupportActionBar().setDisplayHomeAsUpEnabled(enable);
        // mDrawer.setDrawerIndicatorEnabled(!enable);
        // ......
        // To re-iterate, the order in which you enable and disable views IS important #dontSimplify.
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Dhaka, Bangladesh, and move the camera.
        val dhaka = LatLng(23.777176, 90.399452)
        mMap?.let {
            it.addMarker(MarkerOptions().position(dhaka).title("Marker in Dhaka"))
            it.moveCamera(CameraUpdateFactory.newLatLng(dhaka))
        }
    }

    override fun onFragmentInteraction(uri: String){
        Log.e("erreur frag ", "enter")

    }
}
