package com.example.rofaida.saydaliyati

import android.annotation.SuppressLint
import java.util.regex.Matcher
import java.util.regex.Pattern
import android.view.ViewGroup
import android.view.LayoutInflater
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.RuntimeExecutionException
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SignUp_Fragment : Fragment(), View.OnClickListener {

    private lateinit var firstName: EditText
    private lateinit var lastName: EditText
    private lateinit var NSS:EditText
    private lateinit var mobileNumber:EditText
    private lateinit var location:EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword:EditText
    private lateinit var login: TextView
    private lateinit var signUpButton: Button
    private lateinit var terms_conditions: CheckBox
    private lateinit var view1:View

    val ACCOUNT_SID = "AC73955cfb33b8e4a118b39a0afe69182a"
    val AUTH_TOKEN = "ef7ba53374e40890a3177fddb24b8cf6"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //initTokenServerApi()
        //twilioVerification = TwilioVerification(this.context!!)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.signUpBtn ->

                // Call checkValidation method
                checkValidation()

            R.id.already_user ->

                // Replace login fragment
                (activity as MainActivity).replaceLoginFragment()
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view1 = inflater.inflate(R.layout.signup_layout, container, false)
        initViews()
        setListeners()
        return view1
    }


    // Initialize all views
    @SuppressLint("ResourceType")
    private fun initViews() {
        firstName = view1.findViewById<View>(R.id.firstName) as EditText
        lastName = view1.findViewById<View>(R.id.lastName) as EditText
        NSS = view1.findViewById<View>(R.id.NSS) as EditText
        mobileNumber = view1.findViewById<View>(R.id.mobileNumber) as EditText
        location = view1.findViewById<View>(R.id.location) as EditText
        password = view1.findViewById<View>(R.id.password) as EditText
        confirmPassword = view1.findViewById<View>(R.id.confirmPassword) as EditText
        signUpButton = view1.findViewById<View>(R.id.signUpBtn) as Button
        login = view1.findViewById<View>(R.id.already_user) as TextView
        terms_conditions = view1.findViewById<View>(R.id.terms_conditions) as CheckBox

        // Setting text selector over textviews
        val xrp = getResources().getXml(R.drawable.text_selector)
        try {
            val csl = ColorStateList.createFromXml(
                getResources(),
                xrp
            )

            login.setTextColor(csl)
            terms_conditions.setTextColor(csl)
        } catch (e: Exception) {
        }

    }

    // Set Listeners
    private fun setListeners() {
        signUpButton.setOnClickListener(this)
        login.setOnClickListener(this)
    }


    // Check Validation Method
    private fun checkValidation() {

        // Get all edittext texts
        val getFirstName = firstName.text.toString()
        val getLastName = lastName.text.toString()
        val getNSS = NSS.text.toString()
        val getMobileNumber = mobileNumber.text.toString()
        val getLocation = location.text.toString()
        val getPassword = password.text.toString()
        val getConfirmPassword = confirmPassword.text.toString()

        // Pattern match for email id
        val p = Pattern.compile(Utils.regEx)
        val m = p.matcher(getMobileNumber)

        // Check if all strings are null or not
        if (getFirstName == "" || getFirstName.length == 0
            || getLastName == "" || getLastName.length == 0
            || getNSS == "" || getNSS.length == 0
            || getMobileNumber == "" || getMobileNumber.length == 0
            || getLocation == "" || getLocation.length == 0
            || getPassword == "" || getPassword.length == 0
            || getConfirmPassword == ""
            || getConfirmPassword.length == 0
        )

            CustomToast().Show_Toast(
                activity!!, view1,
                "All fields are required.")
        else if (!m.find())
            CustomToast().Show_Toast(
                this.context!!, view1,
                "Votre Numéro de Téléphone est invalide.")
        else if (getConfirmPassword != getPassword)
            CustomToast().Show_Toast(
                this.context!!, view1,
                "Les deux Mots de Passe ne correspondent pas.")
        else if (!terms_conditions.isChecked)
            CustomToast().Show_Toast(
                this.context!!, view1,
                "Sélectionnez les termes et conditions")
        else {
            Toast.makeText(activity, "Do SignUp.", Toast.LENGTH_SHORT)
                .show()// Else do signup or do your stuff
            // Make sure user should check Terms and Conditions checkbox
            // Check if both password should be equal
            // Check if email id valid or not

            sendMessage()


        }

    }


    /***************************************** Send Message ******************************************************/

    private fun sendMessage()
    {
        val body = "Hello test"
        val from = "+18162826468"
        val to = "+213793740560"

        val base64EncodedCredentials = "Basic " + Base64.encodeToString((ACCOUNT_SID + ":" + AUTH_TOKEN).toByteArray(), Base64.NO_WRAP)
        val smsData = mutableMapOf<String, String>()

        smsData.put("From", from);
        smsData.put("To", to);
        smsData.put("Body", body);

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.twilio.com/2010-04-01/")
            .build()
        val api = retrofit.create(TwilioApi::class.java)
        api.sendMessage(ACCOUNT_SID, base64EncodedCredentials, smsData).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("TAG", "onFailure")
                throw RuntimeExecutionException(t) //Woops!
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful()) Log.d("TAG", "onResponse->success")
                else Log.d("TAG", response.message())
            }

    })

    }

}
