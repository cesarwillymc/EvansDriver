package com.evans.technologies.conductor.fragments.auth

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.evans.technologies.conductor.Activities.MainActivity
import com.evans.technologies.conductor.Activities.recuperar_account
import com.evans.technologies.conductor.R
import com.evans.technologies.conductor.Retrofit.RetrofitClient
import com.evans.technologies.conductor.model.Driver
import com.evans.technologies.conductor.model.LoginResponse
import com.evans.technologies.conductor.model.RegistroInicioSesion
import com.evans.technologies.conductor.model.infoDriver
import com.evans.technologies.conductor.Utils.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.support.v4.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class LoginFragment : Fragment(), View.OnClickListener {

    private lateinit var prefs: SharedPreferences
    private lateinit var dataDriver: SharedPreferences
    private lateinit var navFragment: SharedPreferences
    lateinit var data: Driver
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = activity!!.getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
        dataDriver = activity!!.getSharedPreferences("datadriver", Context.MODE_PRIVATE)
        dataDriver.edit().clear().apply()
        setCredentialIfExist()
        navFragment = activity!!.getSharedPreferences("navFragment", Context.MODE_PRIVATE)
        navFragment.edit().clear().apply()
        ActivityCompat.requestPermissions(
            activity!!,
            arrayOf(

                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            30
        )
        login_button_registrar.isEnabled=true
        login_button_logeo.setOnClickListener(this)
        login_button_registrar.setOnClickListener(this)
        al_txt_olvidaste.setOnClickListener(this)

    }

    override fun onStart() {
        super.onStart()

    }

    private fun userLogin() {

        if (!login_edit_text_usuario.userValido())
        {
            login_edit_text_usuario.error = "Complete el campo con un correo"
            login_edit_text_usuario.requestFocus()
            login_progressbar.visibility= View.GONE
            login_button_logeo.isEnabled=true
            login_button_registrar.isEnabled=true
            return
        }
        if (!login_edit_text_contraseña.passwordvalido())
        {
            login_edit_text_contraseña.error = "Contraseña no valida"
            login_edit_text_contraseña.requestFocus()
            login_progressbar.visibility= View.GONE
            login_button_logeo.isEnabled=true
            login_button_registrar.isEnabled=true
            return
        }
        login_progressbar.visibility= View.VISIBLE
        login_button_logeo.isEnabled=false
        login_button_registrar.isEnabled=false
        val call = RetrofitClient
            .getInstance()
            .api
            .loginUser(login_edit_text_usuario.text.toString(), login_edit_text_contraseña.text.toString())

        call.enqueue(object: Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.e("datosenviados","" +response.code())
                Log.e("datosenviados",login_edit_text_usuario.text.toString() +"  "+ login_edit_text_contraseña.text.toString())
                when(response.code()){
                    200->{
                        goToMain(response.body()!!.user.id, response.body()!!.user.token)
                    }
                    400->{
                        login_button_logeo.isEnabled=true
                        login_button_registrar.isEnabled=true
                        login_progressbar.visibility= View.GONE
                        activity!!.toastLong("Email o contraseña incorrectos")
                    }
                    500->{
                        login_button_logeo.isEnabled=true
                        login_button_registrar.isEnabled=true
                        login_progressbar.visibility= View.GONE
                        activity!!.toastLong("Error al realizar la petición.")
                    }
                    404->{
                        login_button_logeo.isEnabled=true
                        login_button_registrar.isEnabled=true
                        login_progressbar.visibility= View.GONE
                        activity!!.toastLong("El usuario no existe.")
                    }
                }


            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                login_button_logeo.isEnabled=true
                login_button_registrar.isEnabled=true
                login_progressbar.visibility= View.GONE
                activity!!.toastLong("Revise su conexion a internet")
            }
        })
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.login_button_registrar -> {
                findNavController().navigate(R.id.action_loginFragment_to_correo)
//                login_button_registrar.isEnabled=false
//                startActivity(Intent(activity!!, RegisterFragment::class.java))
//                login_button_registrar.isEnabled=true

            }
            R.id.login_button_logeo -> {
                try{
                    var view = activity!!.currentFocus
                    view!!.clearFocus()
                    if (view != null) {
                        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
                    }
                }catch (e :Exception){

                }

                userLogin()
            }
            R.id.al_txt_olvidaste->{
                findNavController().navigate(R.id.action_loginFragment_to_correo)
//                startActivity(Intent(activity!!,
//                    recuperar_account::class.java))
            }
        }
    }
    private fun comprobarRegistroIniciodeSesion() {
        // Funcion para llenado de datos
        var llamada: Call<RegistroInicioSesion> = RetrofitClient.getInstance().api
            .getDataInicioSesion(getDriverId_Prefs(prefs)!!)
        llamada.enqueue(object : Callback<RegistroInicioSesion> {
            override fun onFailure(call: Call<RegistroInicioSesion>, t: Throwable) {
                activity!!.toastLong("Revise su conexion a internet")
            }

            override fun onResponse(
                call: Call<RegistroInicioSesion>,
                response: Response<RegistroInicioSesion>
            ) {
                try{
                    Log.e("ErrorLogin",response.code().toString())
                    setDatosLlenados(prefs,response.body()!!.data!!.policeRecord,response.body()!!.data!!.criminalRecod,response.body()!!.data!!.driverLicence,
                        response.body()!!.data!!.soatFront,response.body()!!.data!!.tarjetaPropFront,response.body()!!.data!!.tarjetaPropReverse)
                }catch (e:Exception){
                    Log.e("ErrorLogin",e.message)
                }

                // setTipoSocio_init(prefs,response.body()!!.tipoSocio)
            }

        })
    }
    fun goToMain(id:String,token: String) {
        val getInfoDriver = RetrofitClient.getInstance()
            .api.getInfoDriver(id)
        getInfoDriver.enqueue(object : Callback<infoDriver> {
            override fun onResponse(call: Call<infoDriver>, response: Response<infoDriver>) {
                if (response.isSuccessful) {
                    if (response.body()!!.information.licenseCar!=null&&!(response.body()!!.information.licenseCar.contains("null"))){
                        setTieneInfo(prefs,true)
                        setVehiculoInfo(prefs,response.body()!!.information.brandCar,response.body()!!.information.colorCar,
                            response.body()!!.information.modelCar,response.body()!!.information.licenseCar)
                    }else{
                        setTieneInfo(prefs,false)
                    }
                } else {
                    setTieneInfo(prefs,false)
                }
                Log.e("error Retrofit22", response.code().toString() + "")
            }

            override fun onFailure(call: Call<infoDriver>, t: Throwable) {
                activity!!.toastLong("Revise su conexion a internet")
            }
        })
        val getInfo=RetrofitClient.getInstance()
            .api.getUserInfo(token,id)
        getInfo.enqueue(object: Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                login_progressbar.visibility= View.GONE
                login_button_logeo.isEnabled=true
                login_button_registrar.isEnabled=true
                activity!!.toastLong("Revise su conexion a internet")
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful){
                    data= response.body()!!.user
                    FirebaseInstanceId.getInstance().instanceId
                        .addOnCompleteListener(
                            OnCompleteListener {
                                    task ->
                                if (task.isSuccessful){
                                    val Firebasetoken = task.result!!.token
                                    val call = RetrofitClient.getInstance()
                                        .api.tokenFCM(token,id,token,Firebasetoken)
                                    Log.e("tokenid",Firebasetoken)
                                    call.enqueue(object: Callback<Driver> {
                                        override fun onResponse(call: Call<Driver>, response: Response<Driver>) {


                                            if (response.isSuccessful){

                                                saveOnPreferences(id,token,Firebasetoken,
                                                    data.email?:"Desconocido",data.accountActivate,data.name?:"Non",
                                                    data.surname?:"Desc", data.city?:"Puno",
                                                    data.cellphone?:"999999999", data.numDocument,data.isReferred)

                                                comprobarRegistroIniciodeSesion()
                                                var data_prueba=
                                                    File("/storage/emulated/0/evansTaxy/evanstechnologiesdriver"+ getDriverId_Prefs(prefs) +".jpg")
                                                if (data_prueba.exists()){
                                                    login_button_logeo.isEnabled=true
                                                    login_button_registrar.isEnabled=true
                                                    login_progressbar.visibility= View.GONE
                                                    setRutaImagen(prefs,data_prueba.path)
                                                    startActivity<MainActivity>()
                                                    activity!!.finish()
                                                }else if (data.imageProfile!=null||!(data.imageProfile.contains("null"))){
                                                    setImgUrlProfile(prefs,"https://evans-img.s3.us-east-2.amazonaws.com/"+data.imageProfile)
                                                    guardar_foto("https://evans-img.s3.us-east-2.amazonaws.com/"+data.imageProfile)
                                                }
                                                else{
                                                    login_button_logeo.isEnabled=true
                                                    login_button_registrar.isEnabled=true
                                                    login_progressbar.visibility= View.GONE
                                                    startActivity<MainActivity>("tokensend" to token)
                                                    activity!!.finish()
                                                }


                                            }else{
                                                login_button_logeo.isEnabled=true
                                                login_button_registrar.isEnabled=true
                                                login_progressbar.visibility= View.GONE
                                            }

                                        }
                                        override fun onFailure(call: Call<Driver>, t: Throwable) {
                                            login_button_logeo.isEnabled=true
                                            login_button_registrar.isEnabled=true
                                            login_progressbar.visibility= View.GONE
                                            activity!!.toastLong("Revise su conexion a internet")
                                        }
                                    })

                                }else{
                                    login_button_logeo.isEnabled=true
                                    login_button_registrar.isEnabled=true
                                    login_progressbar.visibility= View.GONE
                                    activity!!.toastLong("Error al obtener Instancia ${task.result}")
                                    Log.e("Hola", "${task.result} getInstanceId failed ${task.exception}")
                                }
                            }
                        )
                }else{
                    login_button_logeo.isEnabled=true
                    login_button_registrar.isEnabled=true
                    login_progressbar.visibility= View.GONE
                    activity!!.toastLong("Error: "+ response.code())
                    return
                }
            }

        })



    }



    private fun setCredentialIfExist(){
        val email = getUserEmail(prefs)
        val password = getUserPassword(prefs)
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            login_edit_text_usuario.setText(email)
            login_edit_text_contraseña.setText(password)
        }
    }
    fun guardar_foto(url:String){
        Log.e("LifecycleObserver","entro "+url)
        Glide.with(activity!!)
            .asBitmap()
            .load(url)
            .into(object : SimpleTarget<Bitmap>(100, 100) {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                ) {

                    var archivo= File(saveToInternalStorage(resource, getDriverId_Prefs(prefs)!!))
                    Log.e("LifecycleObserver",archivo.path)
                    if(archivo.exists()){
                        setRutaImagen(prefs,archivo.path)
                        Log.e("imagen","guardado")
                    }

                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    login_button_logeo.isEnabled=true
                    login_button_registrar.isEnabled=true
                    login_progressbar.visibility= View.GONE
                    startActivity<MainActivity>()
                    activity!!.finish()
                }
            })

    }

    /*
      private fun saveToInternalStorage(bitmapImage: Bitmap):String?{
         /* var cw: ContextWrapper =  ContextWrapper(getApplicationContext())
          var directory = cw.getDir("imageDir", Context.MODE_PRIVATE)
          var mypath=File(directory,"profile.jpg")*/

         val path =  Environment.getExternalStorageDirectory().path+ "/" + "evanstechnologiesdriver" + "/"

         val file =
             File(path, "evansimageprofile" + getDriverId_Prefs(prefs) + ".jpg")

         var fos: FileOutputStream
         try {
             fos =  FileOutputStream(file)
             // Use the compress method on the BitMap object to write image to the OutputStream
             bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos)
             fos.close()
         } catch ( e:Exception) {
             e.printStackTrace()
             Log.e("imagen",e.message)
         }
         return file.path
     }
    private fun guardarFotoEnArchivo() {


         val path = Environment.getExternalStorageDirectory().toString()
         var fOut: OutputStream? = null
         val file = File(
             path,
             "evansimageprofile" + getDriverId_Prefs(prefs) + ".jpg"
         ) // the File to save , append increasing numeric counter to prevent files from getting overwritten.
         try {
             fOut = FileOutputStream(file)
         } catch (e: FileNotFoundException) {
             e.printStackTrace()
         }

        // val pictureBitmap = "" // obtaining the Bitmap
         pictureBitmap.compress(
             Bitmap.CompressFormat.JPEG,
             85,
             fOut
         ) // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
         try {
             fOut!!.flush() // Not really required
             fOut.close()
             MediaStore.Images.Media.insertImage(
                 getContentResolver(),
                 file.absolutePath,
                 file.name,
                 file.name
             )

         } catch (e: IOException) {
             e.printStackTrace()
         }

         // do not forget to close the stream

     }*/
    private fun saveOnPreferences(id: String, token: String,accesToken:String,email:String, accountActivate:Boolean, name:String, surname:String, city:String, cellphone:String, dni:String,referred:Boolean){
        val editor = prefs.edit()
        editor.putString("id",id)
        editor.putString("token",token)
        editor.putString("email",email)
        editor.putString("accesToken",accesToken)
        editor.putBoolean("accountActivate",accountActivate)
        editor.putString("name",name)
        editor.putString("surname",surname)
        editor.putString("city",city)
        editor.putString("cellphone",cellphone)
        editor.putString("dni",dni)
        editor.putString("password", login_edit_text_contraseña.text.toString())
        editor.putBoolean("isreferred", referred)
        editor.apply()
    }


}
