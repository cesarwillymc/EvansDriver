package com.evans.technologies.conductor.ui.auth.signIn.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.evans.technologies.conductor.ui.auth.signUp.view.RegisterActivity
import com.evans.technologies.conductor.ui.recover.recuperar_account
import com.evans.technologies.conductor.R
import com.evans.technologies.conductor.base.BaseActivity
import com.evans.technologies.conductor.data.remote.auth.signin.RepositoryAuthImpl
import com.evans.technologies.conductor.model.Driver
import com.evans.technologies.conductor.utils.*
import com.summit.roomexample.base.Resource
import com.summit.summitfood.presentation.SignInViewModel
import com.summit.summitfood.presentation.SignInViewModelFactory
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {
    private val viewModel by lazy { ViewModelProvider(this,SignInViewModelFactory(RepositoryAuthImpl())).get(SignInViewModel::class.java) }
    private lateinit var prefs: SharedPreferences
    private lateinit var dataDriver: SharedPreferences
    private lateinit var navFragment: SharedPreferences
    lateinit var data: Driver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onClearedSharedTemp()
        login_button_logeo.setOnClickListener{
            userLogin()
        }
        login_button_registrar.setOnClickListener{
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
        al_txt_olvidaste.setOnClickListener{
            startActivity(Intent(this, recuperar_account::class.java))
        }

    }
    private fun onClearedSharedTemp(){
        navFragment= getSharedPreferences("navFragment", Context.MODE_PRIVATE)
        navFragment.edit().clear().apply()
    }
    override fun getLayout(): Int =R.layout.activity_login
    private fun hideProgress(){
        progressBar.visibility= View.GONE
        login_button_logeo.isEnabled=true
        login_button_registrar.isEnabled=true

    }
    private fun showProgress(){
        progressBar.visibility= View.VISIBLE
        login_button_logeo.isEnabled=false
        login_button_registrar.isEnabled=false
    }
    private fun userLogin() {
        val email=login_edit_text_usuario.text.toString().trim()
        val pass=login_edit_text_contraseña.text.toString().trim()
        if (!userValido(email))
        {
            login_edit_text_usuario.error = "Correo no valido"
            login_edit_text_usuario.requestFocus()
            return
        }
        if (!passwordvalido(pass))
        {
            login_edit_text_contraseña.error = "Contraseña no valida"
            login_edit_text_contraseña.requestFocus()
            return
        }
        viewModel.SignIn(email, pass).observe(this, Observer {
            when(it){
                is Resource.Loading->{
                    hideKeyboard()
                    showProgress()
                }
                is Resource.Success->{
                    toastLong("Logeado Correctamente")
                    hideProgress()
                 //   startActivity<MainActivity>()
                  //  finish()
                }
                is Resource.Failure->{

                    toastLong("Error ${it.exception.message}")
                    hideProgress()
                }
            }
        })
    }

    override fun onDestroy() {
        viewModel.dettachJob()
        super.onDestroy()
    }
    override fun onDetachedFromWindow() {
        viewModel.dettachJob()
        super.onDetachedFromWindow()
    }

   /* private fun comprobarRegistroIniciodeSesion() {
        // Funcion para llenado de datos
        var llamada: Call<RegistroInicioSesion> = RetrofitClient.instance.api
            .getDataInicioSesion(getDriverId_Prefs(prefs)!!)
        llamada.enqueue(object : Callback<RegistroInicioSesion> {
            override fun onFailure(call: Call<RegistroInicioSesion>, t: Throwable) {
                toastLong("Revise su conexion a internet")
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
        val getInfoDriver = RetrofitClient.instance
            .api.getInfoDriver(id)
        getInfoDriver.enqueue(object : Callback<infoDriver> {
            override fun onResponse(call: Call<infoDriver>, response: Response<infoDriver>) {
                if (response.isSuccessful()) {
                    if (response.body()!!.information.driverImg!=null&&!(response.body()!!.information.driverImg.equals("null"))){
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
                toastLong("Revise su conexion a internet")
            }
        })
        val getInfo= RetrofitClient.instance
            .api.getUserInfo(token,id)
        getInfo.enqueue(object: Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {

                toastLong("Revise su conexion a internet")
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
                                    val call = ClienteRetrofit.instance
                                        .api.tokenFCM(id,token,Firebasetoken)
                                    Log.e("tokenid",Firebasetoken)
                                    call.enqueue(object: Callback<Driver> {
                                        override fun onResponse(call: Call<Driver>, response: Response<Driver>) {


                                            if (response.isSuccessful){

                                                saveOnPreferences(id,token,Firebasetoken,
                                                    data.email?:"Desconocido",data.accountActivate,data.name?:"Non",
                                                    data.surname?:"Desc", data.city?:"Puno",
                                                    data.cellphone?:"999999999", data.numDocument)
                                                comprobarRegistroIniciodeSesion()

                                                if (imagenExiste(getDriverId_Prefs(prefs)!!)){
                                                    login_button_logeo.isEnabled=true
                                                    login_button_registrar.isEnabled=true
                                                    login_progressbar.visibility= View.GONE
                                                    setRutaImagen(prefs,"${Constants.APP_SETTINGS_PATH_PHOTOS}${getDriverId_Prefs(prefs)!!}.jpg")
                                                    startActivity<MainActivity>("tokensend" to token)
                                                    finish()
                                                }else if (data.imageProfile!=null||!(data.imageProfile.equals("nulo"))){

                                                    guardar_foto(BASE_URL_AMAZON_IMG+data.imageProfile)
                                                }
                                                else{
                                                    login_button_logeo.isEnabled=true
                                                    login_button_registrar.isEnabled=true
                                                    login_progressbar.visibility= View.GONE

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
                                            toastLong("Revise su conexion a internet")
                                        }
                                    })

                                }else{
                                    login_button_logeo.isEnabled=true
                                    login_button_registrar.isEnabled=true
                                    login_progressbar.visibility= View.GONE
                                    toastLong("Error al obtener Instancia ${task.result}")
                                    Log.e("Hola", "${task.result} getInstanceId failed ${task.exception}")
                                }
                            }
                        )
                }else{
                    login_button_logeo.isEnabled=true
                    login_button_registrar.isEnabled=true
                    login_progressbar.visibility= View.GONE
                    toastLong("Error: "+ response.code())
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
        Glide.with(application)
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
    private fun saveOnPreferences(id: String, token: String,accesToken:String,email:String, accountActivate:Boolean, name:String, surname:String, city:String, cellphone:String, dni:String){
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
        editor.apply()
    }*/


}
