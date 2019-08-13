package com.example.rofaida.saydaliyati

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.rofaida.saydaliyati.Interfaces.UserSessionManager
import com.example.rofaida.saydaliyati.Models.User_details
import com.google.android.material.navigation.NavigationView


class Menu_Activity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    private lateinit var AddCmdBtn_: Button
    private lateinit var ShowCmdBtn_: Button

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    private var user: User_details? = null

    private lateinit var fragmentManager: FragmentManager

    private var session : UserSessionManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_)

        //Session class instance
        session = UserSessionManager(this@Menu_Activity.applicationContext)

        if(session!!.checkLogin()) finish()

        val user_:HashMap<String, String>? = session!!.getUserDetails()

        if(user_!!.get("KEY_NSS")!=null) {

            Toast.makeText(this, "nss = " + user_!!.size, Toast.LENGTH_LONG).show()
            user = User_details(user_.get("KEY_NSS")!!.toInt(), user_.get("KEY_NOM").toString(),
            user_.get("KEY_PRENOM").toString(), user_.get("KEY_ADR").toString(),
            user_.get("KEY_TEL").toString(), "", user_.get("KEY_IMG").toString())
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        // If savedinstnacestate is null then replace login fragment
        if (savedInstanceState == null) {
            //user = intent.getSerializableExtra("user") as User_details
            fragmentManager = supportFragmentManager
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_item_one ->
            {
                val intent: Intent = Intent(this@Menu_Activity, Add_Commande::class.java)
                startActivity(intent)
            }
            R.id.nav_item_two ->
            {
                var bundle:Bundle = Bundle()
                bundle.putSerializable("user", user!!)
                val fragment_new:Fragment = Ordonnances_Fragment()
                fragment_new.arguments = bundle
                getSupportFragmentManager()!!.beginTransaction()
                    .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                    .replace(
                        R.id.frameContainer,
                        fragment_new,
                        Utils.Ordonnances_Fragment
                    ).addToBackStack(null).commit()
            }
            R.id.nav_item_three -> Toast.makeText(this, "Pharmacies", Toast.LENGTH_SHORT).show()
            R.id.nav_item_seven -> session!!.logoutUser()
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
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
                ).addToBackStack(null).commit()
        }else
            Toast.makeText(
                this.applicationContext, "Destroyed",
                Toast.LENGTH_SHORT
            ).show()
    }

    override fun onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }


}
