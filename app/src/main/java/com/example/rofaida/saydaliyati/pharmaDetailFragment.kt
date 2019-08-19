package com.example.rofaida.saydaliyati

import android.icu.text.SimpleDateFormat
import android.location.Location
import android.location.LocationManager
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.example.rofaida.saydaliyati.Interfaces.RetrofitService
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.app_bar_master.*
import kotlinx.android.synthetic.main.pharma_detail_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*


class pharmaDetailFragment : Fragment(), OnMapReadyCallback {
    lateinit var pharm : pharmacie

    /** for map **/
    var  mMap  : GoogleMap? = null
    private var mLatitudeTextView: TextView? = null
    private var mLongitudeTextView: TextView? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLocation: Location? = null
    private var mLocationManager: LocationManager? = null

    private var mLocationRequest: LocationRequest? = null
    private val listener2: com.google.android.gms.location.LocationListener? = null
    private val UPDATE_INTERVAL = (2 * 1000).toLong()  /* 10 secs */
    private val FASTEST_INTERVAL: Long = 2000 /* 2 sec */

    private var locationManager: LocationManager? = null



    companion object {
        fun newInstance(pharmacie: pharmacie?) = pharmaDetailFragment().apply {
            arguments = Bundle().apply {
                putSerializable("pharmacie", pharmacie)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pharm =  arguments?.getSerializable("pharmacie") as pharmacie
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val a = activity as Master
        a?.actualFrag = "detail"
       // prepareDrawer()

   /*     val toolbar = view?.findViewById(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_back_button)
        toolbar.setNavigationOnClickListener(View.OnClickListener { activity!!.onBackPressed() })*/
        return inflater.inflate(R.layout.pharma_detail_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textView1 = view?.findViewById(R.id.contentP) as TextView
        val textView2 = view?.findViewById(R.id.nomP) as TextView
        val textView3 = view?.findViewById(R.id.fbP) as TextView
        val etat = view?.findViewById(R.id.etatP) as Button
        val hour = view?.findViewById(R.id.hourP) as TextView
        val caisse = view?.findViewById(R.id.caisseP) as TextView
     //   val textView2 = view?.findViewById(R.id.nomP) as TextView
        textView1.setText(pharm.adrpost)
        textView2.setText(pharm.nom)  // find your view elements and do stuff here
        textView3.setText(pharm.fb)
        hour.setText("Ouverte de "+pharm.ho+" à "+pharm.hf)
        /*etat*/
        val string_date = "12-December-2012";

      //  val f : SimpleDateFormat=  SimpleDateFormat("dd-MMM-yyyy");

       // val d: Date = f.parse(string_date);
      //   val milliseconds :Long = d.getTime();

        val millis = System.currentTimeMillis()
        val date = java.sql.Date(millis)
        println(date)
        //val dtf = DateTimeFormatter.ofPattern("HH:mm")
       // val isbefore = LocalTime.parse(LocalTime.now().format(dtf)).isBefore(LocalTime.parse(pharm.hf))
        if(true){
        etat.setText("Ouvert")
        etat.setBackgroundColor(resources.getColor(R.color.colorPrimary))}
        else{
            etat.setText("Fermée")
            etat.setBackgroundColor(resources.getColor(R.color.colorAccent))
        }

        /*fb*/
        val util = Util()
        // The facebook page URL
        val url  = pharm.fb
        val urlweb  = "https://www.facebook.com/notfou"
        // Onclick of the first button
        fbP.setOnClickListener({
            util.openPage(activity!!.applicationContext,url, url)
        })

        /** caisse **/
        var caisseTexte = "Conventionnée avec : "
        val call = RetrofitService.endpoint.pharma_caisse(pharm.id)
        // progressBar.visibility = View.VISIBLE
        call.enqueue(object: Callback<List<pharmacaisse>> {
            override fun onResponse(call: Call<List<pharmacaisse>>?, response:
            Response<List<pharmacaisse>>?) {

                if(response?.isSuccessful!!){

                    Log.e("erreur pharma caisse", "caisse")
                    //  progressBar.visibility = View.INVISIBLE
                    val list:List<pharmacaisse> = response.body()!!
                    val list_caisse = MutableList<String>(list.size){""}
                    for(i in 0 until list.size){
                        list_caisse[i] = list[i].caisse
                        caisseTexte = caisseTexte+list[i].caisse+ ", "
                        Log.e("erreur pharma caisse", caisseTexte)
                    }
                    caisse.setText(caisseTexte)
                }
            }
            override fun onFailure(call: Call<List<pharmacaisse>>?, t: Throwable?) {
                //   progressBar.visibility = View.INVISIBLE
                Log.e("erreur pharma caisse", "nothing")
                //Toast.makeText(getActivity()?.getApplicationContext(), "Impossible d'accèder au serveur", Toast.LENGTH_LONG).show()

                val list:List<pharmacaisse> = AppDatabase.getInstance(activity!!.applicationContext).getPharmaCaisseDao().getcaisseBypharma(id = pharm.id)
                val list_caisse = MutableList<String>(list.size){""}
                for(i in 0 until list.size) {
                    list_caisse[i] = list[i].caisse
                    caisseTexte = caisseTexte +list[i]+ ", "
                }
                caisse.setText(caisseTexte)
            }
        })




        updateUser(pharm.id, pharm.nom, pharm.adrpost, pharm.ho, pharm.hf, pharm.tel, pharm.fb, pharm.loc, pharm.ville, pharm.longi, pharm.lat)



    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val fm = activity!!.supportFragmentManager/// getChildFragmentManager();
        // var supportMapFragment = fm.findFragmentById(R.id.map) as SupportMapFragment
        //  if (supportMapFragment == null) {
        var  supportMapFragment = SupportMapFragment.newInstance()
        fm.beginTransaction().replace(R.id.mapDetail_container, supportMapFragment).commit()
        supportMapFragment.getMapAsync(this)
    }

    fun updateUser( id: Int, nom: String, adrpost: String, ho: String, hf: String, tel: String, fb: String,loc: String,ville:String,longi: Double, lat: Double) {
        val worker = OneTimeWorkRequest.
            Builder(PharmaWorker::class.java).setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())

           // setConstraints(constraints.build()
        val data = Data.Builder()
        //Add parameter in Data class. just like bundle. You can also add Boolean and Number in parameter.
        data.putInt("ID", id)
        data.putString("NOM", nom)
        data.putString("ADRPOST", adrpost)
        data.putString("HO", ho)
        data.putString("HF", hf)
        data.putString("TEL", tel)
        data.putString("FB", fb)
        data.putString("LOC", loc)
        data.putString("VILLE", ville)
        data.putDouble("LONGI", longi)
        data.putDouble("LAT", lat)
        //Set Input Data
        worker.setInputData(data.build())
        //enque worker

        WorkManager.getInstance().enqueue(worker.build())
    }


    private fun prepareDrawer(){

        val  drawer_layout = activity?.findViewById(R.id.drawer_layout) as DrawerLayout
        var toggle = ActionBarDrawerToggle(
            activity, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )

        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        // Remove hamburger
        toggle.setDrawerIndicatorEnabled(false)
        // Show back button
        var a = activity as AppCompatActivity
        a.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        // when DrawerToggle is disabled i.e. setDrawerIndicatorEnabled(false), navigation icon
        // clicks are disabled i.e. the UP button will not work.
        // We need to add a listener, as in below, so DrawerToggle will forward
        // click events to this listener.
        toggle.setToolbarNavigationClickListener( {
            // Doesn't have to be onBackPressed
            activity?.supportFragmentManager?.popBackStack()
           // a.onBackPressed()

        }
        )

    }

    /**for the map ****/

    override fun onMapReady(googleMap: GoogleMap) {


        //Adding the created the marker on the map

        mMap = googleMap


                    mMap?.let {
                        it.addMarker(MarkerOptions().position(LatLng(pharm.lat, pharm.longi)).title("Pharmacie "+pharm.nom))

                        it.moveCamera(CameraUpdateFactory.newLatLng(LatLng(pharm.lat, pharm.longi)))
                        it.animateCamera(CameraUpdateFactory.zoomTo(14.0f), 2000, null);

                    }



    }

}
