package com.example.rofaida.saydaliyati

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.Intent
import android.provider.MediaStore
import android.content.DialogInterface
import android.net.Uri
import android.os.Environment
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import com.example.rofaida.saydaliyati.Models.EtatCommande
import java.io.*
import androidx.appcompat.app.AlertDialog

import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.widget.*
import android.os.Build
import android.annotation.TargetApi
import android.app.Activity

import retrofit2.Retrofit
import android.content.ComponentName
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.database.Cursor
import android.graphics.Color
import android.util.Log
import com.example.rofaida.saydaliyati.Interfaces.RetrofitService
import com.example.rofaida.saydaliyati.Models.Commande
import kotlinx.android.synthetic.main.activity_add__commande.*
import kotlinx.android.synthetic.main.signup_layout.*
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.collections.ArrayList



class Add_Commande : AppCompatActivity(), View.OnClickListener {

    private var picUri:Uri? = null
    private lateinit var permissionsToRequest:ArrayList<String>
    private var permissionsRejected:ArrayList<String> = ArrayList<String>()
    private var permissions:ArrayList<String> = ArrayList<String>()
    private val ALL_PERMISSIONS_RESULT = 107
    private val IMAGE_RESULT = 200
    private lateinit var fabCamera:Button
    private lateinit var fabInsert:Button
    private var  mBitmap:Bitmap? = null
    private lateinit var  textView: TextView
    private lateinit var  msgPharma: EditText
    private lateinit var progressBar_: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add__commande)
        initViews()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        picUri = savedInstanceState!!.getParcelable("pic_uri")
    }

    private fun initViews() {

        fabCamera = findViewById(R.id.fab) as Button
        fabInsert = findViewById(R.id.display_) as Button
        textView = findViewById(R.id.textView3) as TextView
        msgPharma = findViewById(R.id.msg_pharma) as EditText
        fabCamera.setOnClickListener(this)
        fabInsert.setOnClickListener(this)
        progressBar_ = findViewById(R.id.progressBar5) as ProgressBar
        progressBar_.setVisibility(View.INVISIBLE)
        askPermissions()
    }

    override fun onClick(v:View) {
        when (v.getId()) {
            R.id.fab -> startActivityForResult(getPickImageChooserIntent(), IMAGE_RESULT)

            R.id.display_ -> send_commande()
        }
    }

    private fun send_commande()
    {
        if (mBitmap != null)
        {
            checkValidationAndSend()
        }

        else {
            Toast.makeText(this@Add_Commande, "Veuilez selectionnez votre ordonnance", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkValidationAndSend() {

        // Check if all strings are null or not
        if (mBitmap == null)

            CustomToast().Show_Toast(
                this@Add_Commande, this.view,
                "Veuilez selectionnez votre ordonnance."
            )

        else
        {
            var idCommande = 0
            val getIdClient = 123
            val getNomPharma = "Pharmacie 1"
            val getMsg = msg_pharma.text.toString()
            val etat = EtatCommande.pending.etat
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val dateCommande = sdf.format(Date())
            val Titre = "Ordonnance de : "+dateCommande
            progressBar_.visibility = View.VISIBLE
            val call = RetrofitService.endpoint.getMaxCommandeID()
            call.enqueue(object : Callback<Int> {
                override fun onFailure(call: Call<Int>, t: Throwable) {
                    progressBar_.visibility = View.INVISIBLE
                    Toast.makeText(this@Add_Commande, "Something went wrong", Toast.LENGTH_SHORT).show()
                    t.printStackTrace()
                }

                override fun onResponse(call: Call<Int>, response: Response<Int>) {
                    if(response.isSuccessful) {
                        if (response.body() != null) {
                            Toast.makeText(this@Add_Commande, "reponse : " + response.body()!!, Toast.LENGTH_SHORT)
                                .show()
                            idCommande = response.body()!! + 1
                        }

                        var file_name = ""
                        try
                        {
                            val filesDir = this@Add_Commande.applicationContext.getFilesDir()
                            val file = File(filesDir, "image" + ".png")
                            val bos = ByteArrayOutputStream()
                            mBitmap!!.compress(Bitmap.CompressFormat.PNG, 0, bos)
                            val bitmapdata = bos.toByteArray()
                            val fos = FileOutputStream(file)
                            fos.write(bitmapdata)
                            fos.flush()
                            fos.close()
                            val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
                            val body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile)
                            val name = RequestBody.create(MediaType.parse("text/plain"), "upload")
                            val req = RetrofitService.endpoint.postImage(body, name)
                            req.enqueue(object: Callback<String> {
                                override fun onResponse(
                                    call: Call<String>,
                                    response: Response<String>
                                ) {
                                    if (response.isSuccessful)
                                    {
                                        textView.setText("Uploaded Successfully!")
                                        textView.setTextColor(Color.BLUE)
                                        file_name = response.body()!!.toString()
                                        Toast.makeText(this@Add_Commande, "Uploaded Successfully! : "+file_name, Toast.LENGTH_LONG).show()

                                        if (!file_name.equals("")) {
                                            val commande: Commande =
                                                Commande(idCommande, Titre, etat, file_name, getIdClient, 0, getNomPharma, null, "", dateCommande)
                                            val call2 = RetrofitService.endpoint.addCommande(commande)
                                            call2.enqueue(object : Callback<String> {
                                                override fun onFailure(call: Call<String>, t: Throwable) {
                                                    Toast.makeText(this@Add_Commande, "Something went wrong", Toast.LENGTH_SHORT).show()
                                                    t.printStackTrace()
                                                }

                                                override fun onResponse(call: Call<String>, response: Response<String>) {
                                                    if (response.body().equals("SUCCESS")) {
                                                        progressBar_.visibility = View.INVISIBLE
                                                        Toast.makeText(this@Add_Commande, "Commande ajoutée avec succès", Toast.LENGTH_SHORT).show()
                                                    } else {
                                                        progressBar_.visibility = View.INVISIBLE
                                                        Toast.makeText(this@Add_Commande, "Commande non ajoutee", Toast.LENGTH_SHORT)
                                                            .show()
                                                    }
                                                }

                                            })
                                        }
                                        else
                                        {
                                            progressBar_.visibility = View.INVISIBLE
                                            Toast.makeText(this@Add_Commande, "Ordonnance non téléchargée", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(this@Add_Commande, "Request failed", Toast.LENGTH_SHORT).show()

                                    }
                                    //Toast.makeText(this@Add_Commande ,"bos:"+bos+", mBitmap:"+mBitmap.toString(), Toast.LENGTH_SHORT).show()
                                    Log.d("TAG", response.body()!!.toString())
                                }

                                override fun onFailure(call: Call<String>, t: Throwable) {
                                    textView.setText("Uploaded Failed!")
                                    textView.setTextColor(Color.RED)
                                    Toast.makeText(this@Add_Commande, "Request failed", Toast.LENGTH_SHORT).show()
                                    t.printStackTrace()
                                }

                            })
                        }
                        catch (e:FileNotFoundException) {
                            e.printStackTrace()
                        }
                        catch (e:IOException) {
                            e.printStackTrace()
                        }
                    }
                    else
                    {
                        progressBar_.visibility = View.INVISIBLE
                        Toast.makeText(this@Add_Commande, "probleme d'id commande"+response.message(), Toast.LENGTH_SHORT).show()
                    }
                }

            })
        }

    }

    private fun getCaptureImageOutputUri():Uri? {
        var outputFileUri:Uri? = null;
        var getImage:File  = this.getExternalFilesDir("")
        if (getImage != null) {
            outputFileUri = Uri.fromFile(File(getImage.getPath(), "profile.png"))
        }
        return outputFileUri
    }

    public fun getPickImageChooserIntent():Intent{
        val outputFileUri:Uri = this.getCaptureImageOutputUri()!!
        var allIntents:ArrayList<Intent> = ArrayList<Intent>()
        var packageManager: PackageManager = this.packageManager
        var captureIntent:Intent  = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        var listCam:List<ResolveInfo>  = packageManager.queryIntentActivities(captureIntent, 0)
        for (res in listCam) {
            var intent:Intent  =  Intent(captureIntent)
            intent.setComponent(ComponentName(res.activityInfo.packageName, res.activityInfo.name))
            intent.setPackage(res.activityInfo.packageName)
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
            }
            allIntents.add((intent))
        }

        var galleryIntent:Intent  = Intent(Intent.ACTION_GET_CONTENT)
        galleryIntent.setType("image/*")
        var listGallery:List<ResolveInfo>  = packageManager.queryIntentActivities(galleryIntent, 0)
        for (res in listGallery) {
            var intent:Intent  = Intent(galleryIntent)
            intent.setComponent(ComponentName(res.activityInfo.packageName, res.activityInfo.name))
            intent.setPackage(res.activityInfo.packageName)
            allIntents.add(intent)
        }
        var mainIntent:Intent = allIntents.get(allIntents.size - 1)
        for (intent in allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent
                break
            }
        }
        allIntents.remove(mainIntent)
        /*val bundle:Bundle = Bundle()
        bundle.putSerializable("allIntents", allIntents.toArray())
        val chooserIntent:Intent = Intent.createChooser(mainIntent, "Select source")
        chooserIntent.putExtra(Intent.ACTION_CHOOSER, bundle)*/
        var extraIntents = arrayOfNulls<Intent>(allIntents.size)
        extraIntents = allIntents.toArray(extraIntents)
        //var firstIntent:Intent = extraIntents.get(0) // assuming you will have at least one Intent
        var chooserIntent:Intent  = Intent.createChooser(mainIntent, "Select source")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents)
        return chooserIntent

    }


    private fun selectImage() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Add Photo!")
        builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
            if (options[item] == "Take Photo") {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                val f = File(Environment.getExternalStorageDirectory(), "temp.jpg")
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f))
                startActivityForResult(intent, 1)
            } else if (options[item] == "Choose from Gallery") {
                val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, 2)
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        })
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {

            //Toast.makeText(this@Add_Commande, data.toString(), Toast.LENGTH_LONG).show()

            var imageView = findViewById(R.id.IdProf) as ImageView

            if (requestCode == IMAGE_RESULT) {

                var filePath:String  = getImageFilePath(data!!)
                var f:File = File(filePath)
                //Toast.makeText(this@Add_Commande, "pic Uri : "+picUri.toString(), Toast.LENGTH_LONG).show()
                if (filePath != null) {
                    mBitmap = BitmapFactory.decodeFile(filePath)
                    //Toast.makeText(this@Add_Commande, "filePath : "+filePath, Toast.LENGTH_LONG).show()
                    //Toast.makeText(this@Add_Commande, mBitmap.generationId, Toast.LENGTH_LONG).show()
                    imageView.setImageBitmap(mBitmap)
                }
            }

        }
    }


    private fun getImageFromFilePath(data:Intent):String {
        var isCamera:Boolean = (data == null || data.getData() == null)

        if (isCamera) return getCaptureImageOutputUri()?.getPath().toString()
        else return getPathFromURI(data.getData())

    }

    public fun getImageFilePath(data:Intent):String {
        return getImageFromFilePath(data)
    }

    private fun getPathFromURI(contentUri:Uri):String {
        var proj:Array<String> = arrayOf(MediaStore.Audio.Media.DATA)
        var cursor:Cursor = this.contentResolver.query(contentUri, proj, null, null, null)
        var column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

    override fun onSaveInstanceState(outState:Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("pic_uri", picUri)
    }

    private fun showMessageOKCancel(message:String , okListener:DialogInterface.OnClickListener ) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    /********************************* Permission Manipulation ***********************************/

    private fun findUnAskedPermissions(wanted:ArrayList<String>):ArrayList<String>  {
        var result:ArrayList<String> = ArrayList<String>()

        for (perm in wanted) {
            if (!hasPermission(perm)) {
                result.add(perm)
            }
        }

        return result
    }

    private fun hasPermission(permission:String):Boolean {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (this.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private fun canMakeSmores():Boolean  {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            ALL_PERMISSIONS_RESULT -> {
                for (perms in permissionsToRequest)
                {
                    if (!hasPermission(perms))
                    {
                        permissionsRejected.add(perms)
                    }
                }
                if (permissionsRejected.size > 0)
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0)))
                        {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                object:DialogInterface.OnClickListener {
                                    override fun onClick(dialog:DialogInterface, which:Int) {
                                        requestPermissions(permissionsRejected.toArray(arrayOfNulls<String>(permissionsRejected.size)), ALL_PERMISSIONS_RESULT)
                                    }
                                })
                            return
                        }
                    }
                }
            }
        }
    }

    private fun askPermissions() {
        permissions.add(CAMERA)
        permissions.add(WRITE_EXTERNAL_STORAGE)
        permissions.add(READ_EXTERNAL_STORAGE)
        permissionsToRequest = findUnAskedPermissions(permissions)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size > 0)
                requestPermissions(
                    permissionsToRequest.toArray(arrayOfNulls<String>(permissionsToRequest.size)),
                    ALL_PERMISSIONS_RESULT
                )
        }
    }

}
