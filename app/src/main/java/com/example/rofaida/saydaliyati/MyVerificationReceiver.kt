package com.example.rofaida.saydaliyati

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.twilio.verification.TwilioVerification
import com.twilio.verification.external.VerificationStatus

public class MyVerificationReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val verificationStatus:VerificationStatus = TwilioVerification.getVerificationStatus(intent!!)
        val stat = verificationStatus.state
        Toast.makeText(context, stat.toString(), Toast.LENGTH_SHORT)
            .show()

    }
}