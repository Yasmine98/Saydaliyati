package com.example.rofaida.saydaliyati


import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.rofaida.saydaliyati.Interfaces.RetrofitService
import com.example.rofaida.saydaliyati.Models.User_details
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern


class Initialize_Password_Fragment : Fragment(), View.OnClickListener {

    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var view1: View
    private lateinit var ConfirmPswButton: Button
    private lateinit var client: User_details
    private lateinit var progressBar_: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view1 = inflater.inflate(R.layout.fragment_initialize__password_, container, false)
        initViews()
        setListeners()
        client = arguments!!.getSerializable("user") as User_details
        return view1
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ConfirmPswBtn ->
                checkValidation()
        }
    }

        @SuppressLint("ResourceType")
        private fun initViews() {
            password = view1.findViewById<View>(R.id.password2) as EditText
            confirmPassword = view1.findViewById<View>(R.id.confirmPassword2) as EditText
            ConfirmPswButton = view1.findViewById<View>(R.id.ConfirmPswBtn) as Button
            progressBar_ = view1.findViewById<View>(R.id.progressBar4) as ProgressBar
            progressBar_.visibility = View.INVISIBLE
            // Setting text selector over textviews
            val xrp = getResources().getXml(R.drawable.text_selector)
            try {
                val csl = ColorStateList.createFromXml(
                    getResources(),
                    xrp
                )
            } catch (e: Exception) {
            }
        }

        private fun setListeners() {
            ConfirmPswButton.setOnClickListener(this)
            password.addTextChangedListener {
                checkValidationSyntax()
            }
            confirmPassword.addTextChangedListener{
                checkValidationSyntaxConfirm()
            }
        }

        private fun checkValidationSyntaxConfirm() {
            val getConfirmPassword = confirmPassword.text.toString()
            val getPassword = password.text.toString()
            if (!getConfirmPassword.equals(getPassword))
                CustomToast().Show_Toast(
                    this.context!!, view1,
                    "Les deux Mots de Passe ne correspondent pas."
                )
        }

        private fun checkValidationSyntax() {
            val getPassword = password.text.toString()
            val getConfirmPassword = confirmPassword.text.toString()
            val p1 = Pattern.compile(Utils.upperCase)
            val p2 = Pattern.compile(Utils.lowerCase)
            val p3 = Pattern.compile(Utils.digits)

            if (!p1.matcher(getPassword).find())
                CustomToast().Show_Toast(
                    this.context!!, view1,
                    "Le mot de passe doit contenir des lettres Majuscules"
                )
            else if (!p2.matcher(getPassword).find())
                CustomToast().Show_Toast(
                    this.context!!, view1,
                    "Le mot de passe doit contenir des lettres muniscules"
                )
            else if (!p3.matcher(getPassword).find())
                CustomToast().Show_Toast(
                    this.context!!, view1,
                    "Le mot de passe doit contenir des numéros."
                )
            else if(getPassword.length<8)
                CustomToast().Show_Toast(
                    this.context!!, view1,
                    "Le mot de passe doit etre de longueur supérieur à 8 caractères."
                )
            else {
                Toast.makeText(activity, "Do SignUp.", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        private fun checkValidation() {
            val getPassword = password.text.toString()
            val getConfirmPassword = confirmPassword.text.toString()
            val p1 = Pattern.compile(Utils.upperCase)
            val p2 = Pattern.compile(Utils.lowerCase)
            val p3 = Pattern.compile(Utils.digits)

                if (!getConfirmPassword.equals(getPassword))
                    CustomToast().Show_Toast(
                        this.context!!, view1,
                        "Les deux Mots de Passe ne correspondent pas."
                    )
            else if (!p1.matcher(getPassword).find())
                CustomToast().Show_Toast(
                    this.context!!, view1,
                    "Le mot de passe doit contenir des lettres Majuscules"
                )
            else if (!p2.matcher(getPassword).find())
                CustomToast().Show_Toast(
                    this.context!!, view1,
                    "Le mot de passe doit contenir des lettres muniscules"
                )
            else if (!p3.matcher(getPassword).find())
                CustomToast().Show_Toast(
                    this.context!!, view1,
                    "Le mot de passe doit contenir des numéros."
                )
            else if(getPassword.length<8)
                CustomToast().Show_Toast(
                    this.context!!, view1,
                    "Le mot de passe doit etre de longueur supérieur à 8 caractères."
                )
            else {
                    client.mdp = getPassword
                progressBar_.visibility = View.VISIBLE
                val call = RetrofitService.endpoint.updateUserPswd(client)
                call.enqueue(object : Callback<String> {
                    override fun onResponse(
                        call: Call<String>, response:
                        Response<String>
                    ) {
                        if (response.body().equals("SUCCESS")) {
                            progressBar_.visibility = View.INVISIBLE
                            val resp:String = response.body()!!
                            Toast.makeText(this@Initialize_Password_Fragment.context ,resp+ "Welcome to Saydaliyati", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<String>?, t: Throwable?) {
                        progressBar_.visibility = View.INVISIBLE
                        Log.e("erreur retrofit", t.toString())
                        Toast.makeText(this@Initialize_Password_Fragment.context, "Failure", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
    }
