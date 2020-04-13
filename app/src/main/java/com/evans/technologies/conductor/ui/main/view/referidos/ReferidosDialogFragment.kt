package com.evans.technologies.conductor.ui.main.view.referidos

import android.content.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.evans.technologies.conductor.R
import kotlinx.android.synthetic.main.referidos_dialog_fragment.*
import java.lang.Exception

class ReferidosDialogFragment:DialogFragment() {
    val facebook="com.facebook.katana"
    val twitter="com.twitter.android"
    val instagram="com.instagram.android"
    val whatsapp="com.whatsapp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL,R.style.fullScreenDialog)
    }
    private lateinit var prefs: SharedPreferences
    //open dialog
    //val dialog= ReferidosDialogFragment()
    //dialog.show(getSupportFragmentManager(),"")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prefs = context!!.getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        val view= inflater.inflate(R.layout.referidos_dialog_fragment,container,false)
//        if(getReferido(prefs)!!){
//            rdf_cons.visibility=View.GONE
//        }
        rdf_txt_codigo.setOnLongClickListener {
            val mensaje= rdf_txt_codigo.text.toString()
            copyText(mensaje)
        }
        rdf_imgbtn_close.setOnClickListener {
            this.dismiss()
        }
        rdf_imgbtn_facebook.setOnClickListener {
            sharingtoSocialMedia(facebook)
        }
        rdf_imgbtn_msg.setOnClickListener {
            sharingtoSocialMedia(instagram)
        }
        rdf_imgbtn_wsp.setOnClickListener {
            sharingtoSocialMedia(whatsapp)
        }
        rdf_imgbtn_send_code.setOnClickListener {
            val dato= rdf_edtxt_code.text.toString().trim()
            if (dato.isNotEmpty()){
                registrarCode()
            }else{
                rdf_edtxt_code.error="El codigo esta vacio"
            }


        }
        return view
    }

    private fun registrarCode() {
//        var statusTrip= RetrofitClient.getInstance().api.putReferido(getDriverId_Prefs(prefs)!!)
//        statusTrip.enqueue(object : Callback<infoDriver> {
//            override fun onFailure(call: Call<infoDriver>, t: Throwable) {
//                Log.e("error", t.message)
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//            override fun onResponse(call: Call<infoDriver>, response: Response<infoDriver>) {
//                if ( !(response.isSuccessful)){
//                    setReferido(prefs,true)
//                }else{
//                    Toast.makeText(context,"No se pudo referir, revise su conexion",Toast.LENGTH_LONG).show()
//                }
//
//            }
//
//        })
    }

    private fun copyText(mensaje: String): Boolean {
        val dato:ClipboardManager= activity!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("text",rdf_txt_codigo.text.toString())
        dato.setPrimaryClip(clip)
        return false
    }

    fun sharingtoSocialMedia(paquete:String){
        val intent= Intent()
        intent.action = Intent.ACTION_SEND
        intent.type= "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"")
        val installed= checkAppInstalled(paquete)
        if (installed){
            intent.setPackage(paquete)
            startActivity(intent)
        }else{
            Toast.makeText(context,"La aplicacion no esta instalada.",Toast.LENGTH_LONG).show()
        }
    }

    private fun checkAppInstalled(paquete: String): Boolean {
            val pm= context!!.packageManager
        return try{
            pm.getPackageInfo(paquete,PackageManager.GET_ACTIVITIES)
            true
        }catch (e:Exception){
            false
        }
    }
}