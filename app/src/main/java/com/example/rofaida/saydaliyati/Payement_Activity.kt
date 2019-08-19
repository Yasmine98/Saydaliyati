package com.example.rofaida.saydaliyati

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent.getIntent
import android.view.View
import com.braintreepayments.api.dropin.DropInRequest
import com.braintreepayments.api.dropin.DropInActivity
import com.braintreepayments.api.dropin.DropInResult
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlinx.android.synthetic.main.signup_layout.*
import com.braintreepayments.api.models.PaymentMethodNonce
import android.R.attr.data
import android.util.Log
import android.widget.Toast
import com.braintreepayments.api.models.BraintreeApiConfiguration
import com.example.rofaida.saydaliyati.Interfaces.RetrofitService
import com.example.rofaida.saydaliyati.Interfaces.RetrofitServicePayement
import com.example.rofaida.saydaliyati.Models.BrainTreeToken
import com.example.rofaida.saydaliyati.Models.BraintreeTransaction
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_payement_.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Payement_Activity : AppCompatActivity() {

    val REQUEST_CODE:Int = 1234
    var token: String? = null
    var amount:String? = null
    val compositeDisposable:CompositeDisposable = CompositeDisposable()
    private lateinit var mamount_: EditText
    private lateinit var payeBtn_: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payement_)
        payeBtn_ = findViewById(R.id.btnPay) as Button
        mamount_ = findViewById(R.id.etPrice) as EditText

        /*val call = RetrofitServicePayement.ibrainTree.getToken()
        call.enqueue(object : Callback<BrainTreeToken> {
            override fun onFailure(call: Call<BrainTreeToken>, t: Throwable) {
                Toast.makeText(this@Payement_Activity, "Failure", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }

            override fun onResponse(call: Call<BrainTreeToken>, response: Response<BrainTreeToken>) {
                if(response.isSuccessful)
                {
                    token = response.body()!!.clientToken.toString()
                    Toast.makeText(this@Payement_Activity, "Token, "+token, Toast.LENGTH_SHORT).show()

                }
                else
                {
                    Toast.makeText(this@Payement_Activity, "Erreur, "+response.message(), Toast.LENGTH_SHORT).show()

                }
            }


        })*/

        btnPay.setOnClickListener {
            onBraintreeSubmit()
        }

       compositeDisposable.add(RetrofitServicePayement.ibrainTree.getToken().subscribeOn
            (Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Consumer<BrainTreeToken> {
                override fun accept(t: BrainTreeToken?) {
                    if(t!!.success)
                    {
                        token = t.clientToken
                    }
                }
            },
                object : Consumer<Throwable> {
                    override fun accept(t: Throwable?) {
                        Toast.makeText(this@Payement_Activity, "Erreur, "+t!!.message, Toast.LENGTH_LONG).show()
                    }

                }))



    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    fun onBraintreeSubmit() {
        val dropInRequest = DropInRequest().clientToken(token)
        startActivityForResult(dropInRequest.getIntent(this), REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val result = data!!.getParcelableExtra<DropInResult>(DropInResult.EXTRA_DROP_IN_RESULT)
                val nonce = result.getPaymentMethodNonce()
                val stringNonce = nonce!!.getNonce()
                Log.d("mylog", "Result: $stringNonce")
                // Send payment price with the nonce
                // use the result to update your UI and send the payment method nonce to your serve
                if (!mamount_.getText().toString().isEmpty()) {
                    amount = mamount_.getText().toString()
                    compositeDisposable.add(RetrofitServicePayement.ibrainTree.submitPayement(amount!!, stringNonce).subscribeOn
                        (Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Consumer<BraintreeTransaction> {
                            override fun accept(t: BraintreeTransaction?) {
                                if(t!!.success)
                                {
                                    Toast.makeText(this@Payement_Activity, "Paiement Effectué avec succès", Toast.LENGTH_LONG).show()
                                    //Etat to already payed
                                    onDestroy()

                                }
                                else
                                {
                                    Toast.makeText(this@Payement_Activity, "payement failed", Toast.LENGTH_LONG).show()

                                }
                            }
                        },
                            object : Consumer<Throwable> {
                                override fun accept(t: Throwable?) {
                                    Toast.makeText(this@Payement_Activity, "Erreur, "+t!!.message, Toast.LENGTH_LONG).show()
                                }

                            }))
                } else
                    Toast.makeText(this@Payement_Activity, "Please enter a valid amount.", Toast.LENGTH_LONG).show()
            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                // the user canceled
                Log.d("mylog", "user canceled")
            }
            else {
                // handle errors here, an exception may be available in
                val error = data!!.getSerializableExtra(DropInActivity.EXTRA_ERROR) as Exception
                Log.d("mylog", "Error : " + error.toString())
            }
        }
    }

}
