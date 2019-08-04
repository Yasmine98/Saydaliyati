package com.example.rofaida.saydaliyati

import android.annotation.SuppressLint
import java.util.regex.Pattern

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import android.widget.CompoundButton.OnCheckedChangeListener
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView

class Login_Fragment : Fragment(), OnClickListener {

    private lateinit var phoneid: EditText
    private lateinit var password:EditText
    private lateinit var loginButton: Button
    private lateinit var forgotPassword: TextView
    private lateinit var signUp:TextView
    private lateinit var show_hide_password: CheckBox
    private lateinit var loginLayout: LinearLayout
    private lateinit var shakeAnimation: Animation
    private lateinit var fragmentManager1: FragmentManager
    private lateinit var view1:View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view1 = inflater.inflate(R.layout.login_layout, container, false)
        initViews()
        setListeners()
        return view1
    }

    // Initiate Views
    @SuppressLint("ResourceType")
    private fun initViews() {
        fragmentManager1 = activity!!.getSupportFragmentManager()
        phoneid = view1.findViewById<View>(R.id.login_phoneid) as EditText
        password = view1.findViewById<View>(R.id.login_password) as EditText
        loginButton = view1.findViewById<View>(R.id.loginBtn) as Button
        forgotPassword = view1.findViewById<View>(R.id.forgot_password) as TextView
        signUp = view1.findViewById<View>(R.id.createAccount) as TextView
        show_hide_password = view1.findViewById<View>(R.id.show_hide_password) as CheckBox
        loginLayout = view1.findViewById<View>(R.id.login_layout) as LinearLayout

        // Load ShakeAnimation
        shakeAnimation = AnimationUtils.loadAnimation(
            activity,
            R.anim.shake
        )

        // Setting text selector over textviews
        val xrp = resources.getXml(R.drawable.text_selector)
        try {
            val csl = ColorStateList.createFromXml(
                resources,
                xrp
            )

            forgotPassword.setTextColor(csl)
            show_hide_password.setTextColor(csl)
            signUp.setTextColor(csl)
        } catch (e: Exception) {
        }

    }

    // Set Listeners
    private fun setListeners() {
        loginButton.setOnClickListener(this)
        forgotPassword.setOnClickListener(this)
        signUp.setOnClickListener(this)

        // Set check listener over checkbox for showing and hiding password
        show_hide_password.setOnCheckedChangeListener(OnCheckedChangeListener { button, isChecked ->
                // If it is checkec then show password else hide
                // password
                if (isChecked) {

                    show_hide_password.setText(R.string.hide_pwd)// change
                    // checkbox
                    // text

                    password.setInputType(InputType.TYPE_CLASS_TEXT)
                    password.setTransformationMethod(
                        HideReturnsTransformationMethod
                            .getInstance()
                    )// show password
                } else {
                    show_hide_password.setText(R.string.show_pwd)// change
                    // checkbox
                    // text

                    password.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
                    password.setTransformationMethod(
                        PasswordTransformationMethod.getInstance()
                    )// hide password

                }
            })
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.loginBtn -> checkValidation()

            R.id.forgot_password ->

                // Replace forgot password fragment with animation
                fragmentManager!!.beginTransaction()
                    .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                    .replace(
                        R.id.frameContainer,
                        ForgotPassword_Fragment(),
                        Utils.ForgotPassword_Fragment
                    ).commit()
            R.id.createAccount ->

                // Replace signup frgament with animation
                fragmentManager!!.beginTransaction()
                    .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                    .replace(
                        R.id.frameContainer, SignUp_Fragment(),
                        Utils.SignUp_Fragment
                    ).commit()
        }

    }

    // Check Validation before login
    private fun checkValidation() {
        // Get email id and password
        val getPhoneId = phoneid.getText().toString()
        val getPassword = password.getText().toString()

        // Check patter for email id
        val p = Pattern.compile(Utils.regEx)

        val m = p.matcher(getPhoneId)

        // Check for both field is empty or not
        if (getPhoneId == "" || getPhoneId.length == 0
            || getPassword == "" || getPassword.length == 0
        ) {
            loginLayout.startAnimation(shakeAnimation)

            CustomToast().Show_Toast(
                this.context!!, view1,
                "Complétez les champs."
            )

        } else if (!m.find())

            CustomToast().Show_Toast(
                this.context!!, view1,
                "Votre Numéro de Téléphone est invalide."
            )
        else
            Toast.makeText(this.context, "Do Login.", Toast.LENGTH_SHORT)
                .show()// Else do login and do your stuff
        // Check if email id is valid or not

    }

}