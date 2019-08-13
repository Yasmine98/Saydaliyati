package com.example.rofaida.saydaliyati.Interfaces

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.example.rofaida.saydaliyati.MainActivity
import com.google.android.material.internal.ContextUtils.getActivity

class UserSessionManager {
    private var pref: SharedPreferences
    private var editor: SharedPreferences.Editor
    private var context: Context

    val PRIVATE_MOOD = 0
    val PREFER_NAME = "AndroidExamplePref"
    val IS_USER_LOGIN = "IsUserLoggedIn"
    public var KEY_TEL:String = "tel"
    public var KEY_NSS:String = "nss"
    public var KEY_NOM:String = "nom"
    public var KEY_PRENOM:String = "prenom"
    public var KEY_ADR:String = "adr"
    public var KEY_IMG:String = "img"

    constructor(context_: Context) {
        this.context = context_
        pref = context.getSharedPreferences(PREFER_NAME , PRIVATE_MOOD)
        editor = pref.edit()
    }

    public fun createUserLoginSessio(nss:String, tel:String, nom:String, prenom:String, adr:String, img:String)
    {
        //storing login boolean as true
        editor.putBoolean(IS_USER_LOGIN, true)

        //Storing user infos in preferences
        editor.putString(KEY_TEL, tel)
        editor.putString(KEY_NSS, nss)
        editor.putString(KEY_NOM, nom)
        editor.putString(KEY_PRENOM, prenom)
        editor.putString(KEY_ADR, adr)
        editor.putString(KEY_IMG, img)

        //commit changes
        editor.commit()
    }

    //Check Login status, if no so we weill redirect the user to the login activity
    //else do else

    public fun checkLogin():Boolean
    {
        if(!(isUserLoggedIn()))
        {
            //user is not logged in redirect him to login activity
            val intent: Intent = Intent(this.context, MainActivity::class.java)

            //closing all the activities from stack
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            //add new flag to start new activity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            //starting login activity
            context.startActivity(intent)

            return true
        }
        return false
    }

    public fun getUserDetails(): HashMap<String, String> {
        //Use hashMap to store user credentials
        var user : HashMap<String, String> = HashMap()
        user.put("KEY_TEL", pref.getString(KEY_TEL, null))
        user.put("KEY_NSS", pref.getString(KEY_NSS, null))
        user.put("KEY_NOM", pref.getString(KEY_NOM, null))
        user.put("KEY_PRENOM", pref.getString(KEY_PRENOM, null))
        user.put("KEY_ADR", pref.getString(KEY_ADR, null))
        user.put("KEY_IMG", pref.getString(KEY_IMG, null))
        return user
    }

    public fun logoutUser()
    {
        //Clearing all user data from Shared Preferences
        editor.clear()
        editor.commit()

        //After logout redirect user to Login Activity
        val intent: Intent = Intent(this.context, MainActivity::class.java)

        //closing all the activities from stack
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        //add new flag to start new activity
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        //starting login activity
        context.startActivity(intent)
    }

    fun isUserLoggedIn(): Boolean {
        return pref.getBoolean(IS_USER_LOGIN, false)
    }
}