package com.evans.technologies.conductor.ui.main.view.referidos

import android.content.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.evans.technologies.conductor.R
import com.evans.technologies.conductor.Retrofit.RetrofitClient
import com.evans.technologies.conductor.Utils.getDriverId_Prefs
import com.evans.technologies.conductor.Utils.getIsReferred
import com.evans.technologies.conductor.Utils.setReferido
import com.evans.technologies.conductor.Utils.toastLong
import com.evans.technologies.conductor.model.LoginResponse
import com.evans.technologies.conductor.model.Referido
import com.evans.technologies.conductor.model.infoDriver
import kotlinx.android.synthetic.main.referidos_dialog_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.Exception

class ReferidosDialogFragment:DialogFragment() {
    val facebook="com.facebook.katana"
    val twitter="com.twitter.android"
    val instagram="com.instagram.android"
    val whatsapp="com.whatsapp"
    lateinit var referido: Referido
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

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(getIsReferred(prefs)!!){
            rdf_cons.visibility=View.GONE
        }

        var getReferido= RetrofitClient.getInstance().api.getCodeReferido(getDriverId_Prefs(prefs)!!)
        getReferido.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("error", t.message)
                Toast.makeText(context,"No se pudo referir, revise su conexion",Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if ( (response.isSuccessful)){
                    if (response.body()!=null){
                        referido=response.body()!!.referido
                        try{
                            rdf_txt_codigo.text= referido.key
                        }catch (e:Exception){

                        }
                    }else{
                        Toast.makeText(context,"No se pudo traer correctamente los datos, null",Toast.LENGTH_LONG).show()
                    }
                }else{
                    Toast.makeText(context,"No se pudo referir, revise su conexion ${response.code()}",Toast.LENGTH_LONG).show()
                }

            }

        })

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
            sharingtoSocialMedia(twitter)
        }
        rdf_imgbtn_wsp.setOnClickListener {
            sharingtoSocialMedia(whatsapp)
        }
        rdf_imgbtn_send_code.setOnClickListener {
            val dato= rdf_edtxt_code.text.toString().trim()
            if (dato.isNotEmpty()){
                registrarCode(dato)
            }else{
                rdf_edtxt_code.error="El codigo esta vacio"
            }


        }
    }
    private fun registrarCode(dato: String) {
        val id=getDriverId_Prefs(prefs)!!
        val statusTrip= RetrofitClient.getInstance().api.putReferido(id,dato)
        statusTrip.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if ( response.isSuccessful){
                    setReferido(prefs,true)
                    rdf_cons.visibility=View.GONE
                    Toast.makeText(context,"Refirio correctamente..",Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(context,"No se pudo referir, revise su conexion ${response.code()}",Toast.LENGTH_LONG).show()
                }

            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("register", t.message +"  "+id+"  "+dato)
                Toast.makeText(context,"No se pudo referir, revise su conexion",Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun copyText(mensaje: String): Boolean {
        val dato:ClipboardManager= activity!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("text",mensaje)
        dato.setPrimaryClip(clip)
        activity!!.toastLong("Texto Copiado Correctamente")
        return false
    }

    fun sharingtoSocialMedia(paquete:String){
        val intent= Intent()
        intent.action = Intent.ACTION_SEND
        intent.type= "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Evans disfruta benefios en tus viajes registrate con este codigo ${referido.key}y gana un cupon de 1 PEN para tu siguiente viaje. http://www.proevans.com")
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