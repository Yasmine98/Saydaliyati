package com.example.rofaida.saydaliyati


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.ProgressBar
import com.example.rofaida.saydaliyati.Models.User_details


class Verification_Code_Fragment() : Fragment(), View.OnClickListener {

    private lateinit var codeSaisi: EditText
    private lateinit var view1:View
    private lateinit var ConfirmCodeButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var client:User_details

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view1 = inflater.inflate(R.layout.fragment_verification__code_, container, false)
        initViews()
        setListeners()
        client = arguments!!.getSerializable("user") as User_details
        return view1
    }

    private fun initViews() {
        codeSaisi = view1.findViewById<View>(R.id.CodeReceived) as EditText
        ConfirmCodeButton = view1.findViewById<View>(R.id.confCodeBtn) as Button
        progressBar = view1.findViewById(R.id.progressbar) as ProgressBar
    }

    private fun setListeners() {
        ConfirmCodeButton.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.confCodeBtn ->
                validation_Code()
        }
    }

    private fun validation_Code()
    {
        val getCode:String = codeSaisi.text.toString()
        progressBar.setVisibility(View.VISIBLE);
        if(getCode.equals(client.mdp))
        {
            Toast.makeText(activity, "Matching to "+client.mdp, Toast.LENGTH_SHORT)
            .show()
            progressBar.setVisibility(View.GONE);
            // Replace forgot password fragment with animation
            var bundle:Bundle = Bundle()
            bundle.putSerializable("user", client)
            val fragment_new:Fragment = Initialize_Password_Fragment()
            fragment_new.arguments = bundle
            fragmentManager!!.beginTransaction()
                .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                .replace(
                    R.id.frameContainer,
                    fragment_new,
                    Utils.Initialize_Password_Fragment
                ).commit()
        }
        else
        {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(activity, "Not matching to "+client.mdp, Toast.LENGTH_SHORT)
                .show()
        }
    }

}
