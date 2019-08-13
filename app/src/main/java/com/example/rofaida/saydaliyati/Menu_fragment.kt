package com.example.rofaida.saydaliyati


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.rofaida.saydaliyati.Models.User_details


class Menu_fragment : Fragment(), View.OnClickListener {

    private lateinit var view1:View

    private lateinit var AddCmdBtn_: Button
    private lateinit var ShowCmdBtn_: Button

    private var user:User_details? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view1 = inflater.inflate(R.layout.fragment_menu_fragment, container, false)
        user = arguments!!.getSerializable("user") as User_details?
        initViews()
        setListeners()
        return view1
    }

    private fun initViews()
    {
        AddCmdBtn_ = view1.findViewById<View>(R.id.AddCmdBtn) as Button
        ShowCmdBtn_ = view1.findViewById<View>(R.id.ShowCmdBtn) as Button
    }

    private fun setListeners() {
        AddCmdBtn_.setOnClickListener(this)
        ShowCmdBtn_.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.AddCmdBtn ->
            {
                val intent: Intent = Intent(getActivity(), Add_Commande::class.java)
                startActivity(intent)
            }

                R.id.ShowCmdBtn ->
                {
                    var bundle:Bundle = Bundle()
                    bundle.putSerializable("user", user)
                    val fragment_new:Fragment = Ordonnances_Fragment()
                    fragment_new.arguments = bundle
                    fragmentManager!!.beginTransaction()
                        .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                        .replace(
                            R.id.frameContainer,
                            fragment_new,
                            Utils.Ordonnances_Fragment
                        ).addToBackStack(null).commit()
                }
        }
    }
}
