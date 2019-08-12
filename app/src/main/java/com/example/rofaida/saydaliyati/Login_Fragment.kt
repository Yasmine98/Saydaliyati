package com.example.rofaida.saydaliyati

import android.annotation.SuppressLint
import android.content.Intent
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
import com.example.rofaida.saydaliyati.Interfaces.RetrofitService
import com.example.rofaida.saydaliyati.Models.User_details
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
    private lateinit var user:User_details
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
                /*fragmentManager!!.beginTransaction()
                    .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                    .replace(
                        R.id.frameContainer,
                        ForgotPassword_Fragment(),
                        Utils.ForgotPassword_Fragment
                    ).commit()*/

                /*fragmentManager!!.beginTransaction()
                    .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                    .replace(
                        R.id.frameContainer,
                        Add_command_Fragment(),
                        Utils.Add_command_Fragment
                    ).commit() */
                    {
                        fragmentManager!!.beginTransaction()
                            .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                            .replace(
                                R.id.frameContainer,
                                Commande_accepted_details(),
                                Utils.Commande_accepted_details
                            ).commit()
                //val intent: Intent = Intent(getActivity(), Add_Commande::class.java)
                //startActivity(intent)
            }

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
        else {
            // Check if email id is valid or not
            var credentials: User_details = User_details(0, "", "", "", getPhoneId, getPassword, "")
            Login_method(credentials)
        }

    }

    private fun Login_method(credentials:User_details)
    {
        val call = RetrofitService.endpoint.getUserLogin(credentials)
        call.enqueue(object : Callback<List<User_details>> {
            override fun onFailure(call: Call<List<User_details>>, t: Throwable) {
                Toast.makeText(this@Login_Fragment.context, "erreur retrofit", Toast.LENGTH_LONG).show()
                t.printStackTrace()
            }

            override fun onResponse(call: Call<List<User_details>>, response: Response<List<User_details>>) {
                if(response.body()!!.size == 0)
                {
                    Toast.makeText(
                        this@Login_Fragment.context,
                        "Wrong Credentials.",
                        Toast.LENGTH_LONG
                    ).show()
                }
                else {
                    user = response.body()!!.get(0)
                    Toast.makeText(
                        this@Login_Fragment.context,
                        "Welcome to Saydaliyati : " + user.prenom,
                        Toast.LENGTH_LONG
                    ).show()
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
                        ).commit()
                }
            }

        })
    }

}