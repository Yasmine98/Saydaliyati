package com.example.rofaida.saydaliyati

import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_get_started.*
import kotlinx.android.synthetic.main.fragment_get_started.view.*
import android.content.Intent
import androidx.core.view.isVisible
import com.google.android.material.button.MaterialButton
import com.rey.material.widget.Button
import com.rey.material.widget.TextView


class GetStarted : AppCompatActivity() {

    /**
     * The [androidx.viewpager.widget.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * androidx.fragment.app.FragmentStatePagerAdapter.
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_started)

        setSupportActionBar(toolbarS)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter


        val tabLayout = findViewById(R.id.tab_layout) as TabLayout
        tabLayout.setupWithViewPager(container)

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_get_started, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1)
        }

        override fun getCount(): Int {
            // Show 5 total pages.
            return 5
        }

        override fun getPageTitle(position: Int): CharSequence {
            return "${(position + 1)}"
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    class PlaceholderFragment : Fragment() {

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {

            val rootView = inflater.inflate(R.layout.fragment_get_started, container, false)
           // rootView.constraintLayout.section_label.text = getString(R.string.section_format, arguments?.getInt(ARG_SECTION_NUMBER))
        // val textS = rootView.findViewById(R.id.section_label) as android.widget.TextView
            val imageS = rootView.findViewById(R.id.imageS) as android.widget.ImageView
            val but = rootView.findViewById(R.id.button) as android.widget.Button
            but.setOnClickListener(View.OnClickListener { view ->
                val intent = Intent(view.context, Master::class.java)
                view.context.startActivity(intent)
            })

            val i: Int? = arguments?.getInt(ARG_SECTION_NUMBER)
            if (i == 1)
            {
              //  textS.text= "Bienvenu dans "
                //textS.setTextSize(1, 20.0f)
                imageS.setImageResource(R.drawable.sayda)
                but.isVisible = false

            }
            else if(i == 3){
             //   textS.text= " passer vos commandes de chez vous et suivez leurs états! "
                imageS.setImageResource(R.drawable.commande)
            }
            else if (i == 2){
               // textS.text= "Notre application vous permet de trouver les pharmacies les plus proches de vous "
                imageS.setImageResource(R.drawable.maping)

            }
            else if (i==4){
               // textS.text= "et meme payer vos factures! ...  "
                imageS.setImageResource(R.drawable.pay)

            }
            else{
                //textS.text= "et encore pleines d'autres fonctionalités! "
                imageS.setImageResource(R.drawable.now)
                but.isVisible = true
            }
            return rootView
        }


        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private val ARG_SECTION_NUMBER = "section_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }
}
