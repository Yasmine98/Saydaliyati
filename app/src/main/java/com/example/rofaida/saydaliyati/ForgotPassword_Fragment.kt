package com.example.rofaida.saydaliyati

import android.annotation.SuppressLint
import java.util.regex.Matcher
import java.util.regex.Pattern

import android.content.res.ColorStateList
import android.content.res.XmlResourceParser
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class ForgotPassword_Fragment : Fragment(), OnClickListener {

    private lateinit var emailId: EditText
    private lateinit var submit: TextView
    private lateinit var back: TextView
    private lateinit var view1:View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view1 = inflater.inflate(R.layout.forgotpassword_layout, container, false)
        initViews()
        setListeners()
        return view1
    }

    // Initialize the views
    @SuppressLint("ResourceType")
    private fun initViews() {
        emailId = view1.findViewById<View>(R.id.registered_emailid) as EditText
        submit = view1.findViewById<View>(R.id.forgot_button) as TextView
        back = view1.findViewById<View>(R.id.backToLoginBtn) as TextView

        // Setting text selector over textviews
        val xrp = resources.getXml(R.drawable.text_selector)
        try {
            val csl = ColorStateList.createFromXml(
                resources,
                xrp
            )

            back.setTextColor(csl)
            submit.setTextColor(csl)

        } catch (e: Exception) {
        }

    }

    // Set Listeners over buttons
    private fun setListeners() {
        back.setOnClickListener(this)
        submit.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.backToLoginBtn ->

                // Replace Login Fragment on Back Presses
                (activity as MainActivity).replaceLoginFragment()

            R.id.forgot_button ->

                // Call Submit button task
                submitButtonTask()
        }

    }

    private fun submitButtonTask() {
        val getEmailId = emailId.getText().toString()

        // Pattern for email id validation
        val p = Pattern.compile(Utils.regEx)

        // Match the pattern
        val m = p.matcher(getEmailId)

        // First check if email id is not null else show error toast
        if (getEmailId == "" || getEmailId.length == 0)

                    CustomToast().Show_Toast(
                        this.context!!, view1,
                        "Please enter your Email Id."
                    )

        else if (!m.find())

                    CustomToast().Show_Toast(
                        this.context!!, view1,
                        "Your Email Id is Invalid."
                    )
        else
            Toast.makeText(
                activity, "Get Forgot Password.",
                Toast.LENGTH_SHORT
            ).show()// Else submit email id and fetch passwod or do your stuff
        // Check if email id is valid or not
    }
}