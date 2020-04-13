package com.evans.technologies.conductor.Activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.ActivityManager
import android.content.*
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.evans.technologies.conductor.R
import com.evans.technologies.conductor.Retrofit.RetrofitClient
import com.evans.technologies.conductor.Utils.ComunicacionesRealTime.ComunicateFrag
import com.evans.technologies.conductor.Utils.ComunicacionesRealTime.updateListenerNotifications
import com.evans.technologies.conductor.Utils.*

import com.evans.technologies.conductor.Utils.miui.canDrawOverlayViews
import com.evans.technologies.conductor.Utils.miui.dialogEmergente
import com.evans.technologies.conductor.Utils.miui.isXiaomi
import com.evans.technologies.conductor.fragments.*
import com.evans.technologies.conductor.fragments.fragment_notificaciones.fragment_notificaciones_rv
import com.evans.technologies.conductor.model.Driver
import com.evans.technologies.conductor.model.RegistroInicioSesion
import com.evans.technologies.conductor.model.config
import com.evans.technologies.conductor.model.infoDriver
import com.evans.technologies.conductor.ui.main.view.referidos.ReferidosDialogFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class MainActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener, updateListenerNotifications,
    View.OnClickListener {



    private lateinit var prefs: SharedPreferences
    private lateinit var dataDriver: SharedPreferences
    lateinit var comunicateFrag: ComunicateFrag.mapa_inicio
    lateinit var comunicateFragChat: ComunicateFrag.chat
    lateinit var receiver: BroadcastReceiver
    lateinit var textNotificacion: TextView
    var chatSnapshot: DataSnapshot? =null
    //lateinit var  data:String
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE)
       // setStatusService_active(prefs,false)
       // if(!(getStatusService_active(prefs))!!){
        //    startService(Intent(this@MainActivity, service_mqtt::class.java))
         //   setStatusService_active(prefs,false)
       // }
        dataDriver = getSharedPreferences("datadriver", Context.MODE_PRIVATE)
        //comprobarStatusTrip()
        Log.d("erroraccount", getaccountActivate(prefs).toString())

        setContentView(R.layout.activity_main)

        FirebaseDatabase.getInstance().reference.child("settingDriver").child(getDriverId_Prefs(prefs)!!)
            .addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                        if(p0.exists()){
                            var configuraciones:config = p0.getValue( config::class.java)!!
                            settingAccount(configuraciones)
                        }
                }
            })

                FirebaseDatabase.getInstance().getReference().child("chatsFirebase").child(
                    getDriverId_Prefs(prefs)!!).limitToLast(15)
                    .addValueEventListener(object : ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if(p0.exists()){
                                chatSnapshot=p0;
                                if (!(getLlaveChat(dataDriver).equals(""))){
                                    var datos:MediaPlayer= MediaPlayer.create(this@MainActivity,R.raw.sound)
                                    datos.start()
                                    datos.setOnCompletionListener {
                                        it.release()
                                    }
                                }

                                if (getClaseMapaInicio(prefs)!!){
                                    comunicateFrag.mensajeGet(p0)
                                }else if(getClaseChat(prefs)!!){
                                    comunicateFragChat.sendDtaMessage(p0)
                                }
                            }
                        }

                    })




        textNotificacion= MenuItemCompat.getActionView(main_nav_view_inicio.getMenu().
            findItem(R.id.nav_notificacion))  as TextView
        updateVersion()
        // Ingresar los datos al toolbar
        setSupportActionBar(toolbar)
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)

        acv_imagebutton_back.setOnClickListener (this)
        //INFORMACIÓN DEL USUARIO EN EL MENU DE NAVEGACIÓN
        val headerView = main_nav_view_inicio.getHeaderView(0)
        headerView.nav_header_image_view_profile.setOnClickListener(this)

        //HACEMOS LA LLAMADA A SWITCH LAYOUT PARA MOSTRAR EL LOGO DE EVANS

        //val actionBar = getSupportActionBar()

       // actionBar?.setCustomView(R.layout.switch_layout)

        supportActionBar!!.displayOptions = ActionBar.DISPLAY_HOME_AS_UP or ActionBar.DISPLAY_SHOW_CUSTOM

    //Suscripcipnes ********************************************************************

       /* if (!getChatJson(dataDriver).equals("nulo")){
            val intent = Intent("subsUnsubs")
            intent.putExtra("subs", "chat")
            intent.putExtra("subsUnsubs", false)
            val broadcaster = LocalBroadcastManager.getInstance(this@MainActivity)
            broadcaster.sendBroadcast(intent)
        }*/
        ///////////////////////////////////////////////////////////////////////////////////////////////////
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        toggle.drawerArrowDrawable.color=resources.getColor(R.color.white)


        main_nav_view_inicio.setNavigationItemSelectedListener(this)
        var menu=main_nav_view_inicio.menu
        if(!(getVoucherAceptado(prefs)!!)){
            tienevoucher(menu)
        }

        //Insertar Countado en la notificacion
        if(getTieneNotificacionAdmin(prefs)!!){

            initializeCountDrawer(true)
        }
        ///
        menu.findItem(R.id.nav_notificacion).setEnabled(true)
        menu.findItem(R.id.nav_ingresos).setEnabled(false)
        menu.findItem(R.id.nav_banca).setEnabled(false)
        if (getTieneInfo(prefs)!!){
            menu.findItem(R.id.nav_car).setEnabled(false)
        }

        if (!isXiaomi()){
            var menu=main_nav_view_inicio.menu
            menu.findItem(R.id.nav_settings).setVisible(false)
        }
        setInfoUser(headerView)//Función para mostrar datos personales
        switchon.isChecked = getStatusSwitch(dataDriver)



        switchon.setOnCheckedChangeListener { buttonView, isChecked ->
            if (getTienePasajero(dataDriver)!!){
                switchon.isChecked=true
                crear_dialog_cancelado()
            }else{
                val call = RetrofitClient.getInstance()
                    .api.switchStatus(getToken(prefs)!!, getDriverId_Prefs(prefs)!!, isChecked)
                call.enqueue(object : Callback<Driver> {
                    override fun onResponse(call: Call<Driver>, response: Response<Driver>) {
                        if (response.isSuccessful) {
                            try {
                                comunicateFrag.updatefrag(isChecked)
                            }catch (E:Exception){
                                Log.e("entra aca",E.message)
                            }
                            Log.e("cambio", "exitoso")
                        }else{
                            switchon.isChecked= getStatusSwitch(dataDriver)

                        }
                        Log.e("cambio", "" + response.code())
                    }

                    override fun onFailure(call: Call<Driver>, t: Throwable) {
                        switchon.isChecked= getStatusSwitch(dataDriver)
                    }
                })

            }



        }

        if(!getaccountActivate(prefs)){
            comprobarAcountActivate()
            comprobarRegistroIniciodeSesion()
        }else{
            switchon.visibility= View.VISIBLE
            switchon.isChecked= getStatusSwitch(dataDriver)
        }
        //isdataExtrasNotificacion()

        Sfragmentdefault()
        //COmpruba si el usuario o taxy tienen un viaje o algo similar para tomar acciones

        //COmprobar si esta registrado la info del usuario como soat y tipo de socio


        /*var intent =  Intent("miui.intent.action.APP_PERM_EDITOR")
       intent.setClassName("com.miui.securitycenter",
               "com.miui.permcenter.permissions.PermissionsEditorActivity")
       intent.putExtra("extra_pkgname", getPackageName())
       startActivity(intent)*/
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val gson = Gson()
                val topic: config = gson.fromJson<config>(
                    intent.getStringExtra("data").toString(),
                    config::class.java
                )
                if (topic.response!=null){
                    if(topic.response.contains("setting")){
                        settingAccount(topic)
                    }else {
                        toastLong("Tiene una nueva notificacion "+intent.getStringExtra("data"))
                        initializeCountDrawer(true)
                    }

                }else{
                    toastLong("Tiene una nueva notificacion ")
                }


                //if(intent.getStringExtra("data").contains("notify"))
            }
        }

       /* val intent = Intent("subsUnsubs")
        intent.putExtra("subs", "notify")
        intent.putExtra("valor",false)
        val broadcaster = LocalBroadcastManager.getInstance(this@MainActivity)
        broadcaster.sendBroadcast(intent)*/
        LocalBroadcastManager.getInstance(this@MainActivity).registerReceiver(
            receiver,
            IntentFilter("mainActivity"))

    }

    private fun settingAccount(topic: config) {
       if (topic.accountActivate!=null){
           if (topic.accountActivate){
               if (getaccountActivate(prefs)){

               }else{
                   setaccountActivate(prefs,topic.accountActivate)
                   switchon.visibility= View.VISIBLE
                   switchon.isChecked= getStatusSwitch(dataDriver)
                   setEstadoViews(dataDriver,1)
                   comunicateFrag.updatefrag(false)
               }

           }else{
               if (getEstadoView(dataDriver)!! >=4){
                   setaccountActivate(prefs,topic.accountActivate)
               }else{
                   setaccountActivate(prefs,topic.accountActivate)
                   switchon.visibility= View.GONE
                   setStatusSwitch(dataDriver,false)
                   switchon.isChecked= getStatusSwitch(dataDriver)
                   setEstadoViews(dataDriver,1)
                   comunicateFrag.updatefrag(false)
               }
           }

       }
        if(topic.tripActivate!=null){
            if (!topic.tripActivate){
                val editor = dataDriver.edit()
                editor.clear()
                editor.apply()
                setEstadoViews(dataDriver,2)
                setStatusSwitch(dataDriver,true)

            }
        }
        if(topic.informationCar!=null){

        }
        if(topic.photosCar!=null){
            setDatosLlenados(prefs,true,true,
                topic.photosCar.driverLicence?: getdriverLicense(prefs)!!,
                topic.photosCar.soatFront?: getsoat(prefs)!!,
                topic.photosCar.tarjetaPropFront?: getpropertyCardForward(prefs)!!,
                topic.photosCar.tarjetaPropReverse?: getpropertyCardBack(prefs)!!)
            iniciarLlenadoDeDatosRegistro()

        }
    }


    private fun tienevoucher(menu: Menu) {
        var tienevoucherfun=RetrofitClient.getInstance().api.tiene_voucher(getDriverId_Prefs(prefs)!!)
        tienevoucherfun.enqueue(object : Callback<Driver>{
            override fun onResponse(call: Call<Driver>, response: Response<Driver>) {
                if(response.isSuccessful){
                    if(response.body()!=null){
                        setEnvioVocher(prefs,response.body()!!.estadoimg?:false)
                        setVoucherAceptado(prefs,response.body()!!.acceptimg?:false)
                        if (response.body()!!.estadoimg?:false){
                            menu.findItem(R.id.nav_banca).setEnabled(false)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<Driver>, t: Throwable) {
             }

        })
    }
    fun initializeCountDrawer(notificacion: Boolean){

        //Gravity property aligns the text
        if (notificacion){
            textNotificacion.setGravity(Gravity.CENTER_VERTICAL)
            textNotificacion.setTypeface(null, Typeface.BOLD)
            textNotificacion.setTextColor(getResources().getColor(R.color.red))
            textNotificacion.setText("NEW✯✯")
        }else{
            textNotificacion.setText("")
        }


    }
    fun updateVersion(){
        FirebaseDatabase.getInstance().reference.child("settinsApp").child("driver").addValueEventListener(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()){
                        try{
                            val dato= p0.getValue(infoDriver::class.java)
                            setApiWebVersion(prefs,dato!!.version+"")
                            if (!(dato!!.version.equals(getVersionApp()))){
                                if (getEstadoView(dataDriver)!! <4){
                                    getViewUpdateVersion(this@MainActivity)
                                }
                            }
                        }catch (e:Exception){
                            Log.e("error ",e.message)
                        }
                    }
                }


            })
            
    }

    override fun updateNotificatones(check: Boolean?,after:String,before: Fragment) {
        try{

            if (check!!){
                acv_imagebutton_back.visibility= View.VISIBLE
                switchon.visibility= View.GONE
                val manager = supportFragmentManager
                Log.e("updatenot",after)
                setClassBackFragment(prefs,after)
                manager.beginTransaction().replace(R.id.main_layout_change_fragment,
                    before
                ).commit()
                Handler().postDelayed({
                    if(before.toString().contains("Fragment_chat")){
                        comunicateFragChat.sendDtaMessage(chatSnapshot)
                    }
                }, 500)

               // switchon.isChecked=getStatusSwitch(dataDriver)

            }else{
                acv_imagebutton_back.visibility= View.GONE
                if(getaccountActivate(prefs)){
                    switchon.visibility= View.VISIBLE
                    switchon.isChecked=getStatusSwitch(dataDriver)

                    if(getStatusSwitch(dataDriver)){
                        setEstadoViews(dataDriver,2)
                    }else{
                        setEstadoViews(dataDriver,1)
                    }

                }

                val manager = supportFragmentManager
                manager.beginTransaction().replace(R.id.main_layout_change_fragment,
                    before
                ).commit()

            }
        }catch (E:Exception){
            Log.e("UpdateNot",E.message)
        }

    }



    private fun comprobarAcountActivate() {
        var acountActivate=RetrofitClient.getInstance().api.callAcountActivate(getDriverId_Prefs(prefs)!!)
        acountActivate.enqueue(object : Callback<Driver> {
            override fun onFailure(call: Call<Driver>, t: Throwable) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call<Driver>, response: Response<Driver>) {
                if (response.isSuccessful){
                    if (response.body()!!.accountActivate) {
                        setaccountActivate(prefs,response.body()!!.accountActivate)
                        switchon.visibility= View.VISIBLE
                        setEstadoViews(dataDriver,1)
                    }else{
                        switchon.visibility= View.GONE
                    }
                }

            }

        })
    }

    private fun comprobarRegistroIniciodeSesion() {
        Log.e("Main","comprobarRegistroIniciodeSesion"+ datosLlenadossoatetc(prefs))
        Log.e("ErrorLogin", datosLlenadossoatetc(prefs).toString()+"main")

        var llamada: Call<RegistroInicioSesion> = RetrofitClient.getInstance().api
            .getDataInicioSesion(getDriverId_Prefs(prefs)!!)
        llamada.enqueue(object : Callback<RegistroInicioSesion> {
            override fun onFailure(call: Call<RegistroInicioSesion>, t: Throwable) {
                Log.e("ErrorLogin",t.message.toString()+"mainerror")
            }
            override fun onResponse(
                call: Call<RegistroInicioSesion>,
                response: Response<RegistroInicioSesion>
            ) {
                try{
                    Log.e("ErrorLogin",response.code().toString()+"mainSucess")
                    setDatosLlenados(prefs,response.body()!!.data!!.policeRecord?:true,response.body()!!.data!!.criminalRecod?:true,response.body()!!.data!!.driverLicence,
                        response.body()!!.data!!.soatFront,response.body()!!.data!!.tarjetaPropFront,response.body()!!.data!!.tarjetaPropReverse)
                    iniciarLlenadoDeDatosRegistro()
                }catch (e:Exception){
                    Log.e("ErrorLogin",response.code().toString()+"mainfail")
                }
            }

        })

        // }
        // Funcion para llenado de datos

    }

    private fun iniciarLlenadoDeDatosRegistro() {
        Log.e("Main","comprobarRegistroIniciodeSesion"+ getTipoSocio_init(prefs))
        
        if(!(getaccountActivate(prefs))){
            if (!datosLlenadossoatetc(prefs)){
                Log.e("ErrorLogin", datosLlenadossoatetc(prefs).toString()+"llenadoDatos")
                switchon.visibility= View.GONE
                val manager = supportFragmentManager
                manager.beginTransaction().replace(R.id.main_layout_change_fragment,
                    pasos_requeridos()
                ).commit()


            }
        }
    }

    private fun crear_dialog_cancelado() {
        val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    val comando = RetrofitClient.getInstance().api.puStatusTrip(
                        getViajeId(dataDriver)!!,
                        true,
                        false,
                        false,
                        false
                    )
                    comando.enqueue(object : Callback<Driver> {
                        override fun onResponse(call: Call<Driver>, response: Response<Driver>) {
                            if (response.isSuccessful) {
                                val llamada = RetrofitClient.getInstance().api.driverTOuser(
                                    getDriverId_Prefs(prefs)!!,
                                    getUserId(dataDriver)!!,
                                    "Viaje Cancelado",
                                    "Taxy Cancelo Viaje",
                                    "viajecancelado",
                                    "",
                                    getViajeId(dataDriver)!!

                                )
                                llamada.enqueue(object : Callback<Driver> {
                                    override fun onResponse(call: Call<Driver>, response: Response<Driver>) {
                                        if (response.isSuccessful) {
                                            val intent = Intent("subsUnsubs")
                                             intent.putExtra("subs", "chat")
                                             intent.putExtra("subsUnsubs", false)
                                             val broadcaster = LocalBroadcastManager.getInstance(this@MainActivity)
                                             broadcaster.sendBroadcast(intent)
                                            comunicateFrag.updatefrag(false)
                                            switchon.isChecked=false

                                        }
                                        Log.e("Mensaje Cancelad", response.code().toString() + "")

                                    }

                                    override fun onFailure(call: Call<Driver>, t: Throwable) {

                                    }
                                })
                            }
                        }

                        override fun onFailure(call: Call<Driver>, t: Throwable) {

                        }
                    })

                }

                DialogInterface.BUTTON_NEGATIVE -> {
                    switchon.isChecked=true
                }
            }//No button clicked
        }
        val builder = AlertDialog.Builder(this)
        builder.setMessage("¿Esta seguro de querer cancelar el viaje?")
            .setPositiveButton("Si", dialogClickListener)
            .setNegativeButton("No", dialogClickListener).show()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
// Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onStart() {
       // Log.e("entro", " onStart" + isMyServiceRunning(service_mqtt::class.java) + "")
       // startMqtt()
       /* try {
            if (!isMyServiceRunning(service_mqtt::class.java)) {
                startService(Intent(this, service_mqtt::class.java))
            }
             <service android:name=".Utils.Service.service_mqtt" />

        <service android:name="org.eclipse.paho.android.service.MqttService"></service>
        } catch (e: java.lang.Exception) {
            Log.e("LifecycleObserver", e.message)
        }*/
        super.onStart()
    }

    override fun onDestroy() {
      //  stopService(Intent(this@MainActivity, service_mqtt::class.java))

        super.onDestroy()
    }
    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager =
            getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
    @SuppressLint("WrongConstant")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
// Handle navigation view item clicks here.

        var mifragment: Fragment? = null
        var fragmentSeleccionado = false

        when (item.itemId) {
            R.id.nav_travel -> {
                mifragment = mapaInicio()
                fragmentSeleccionado = true
            }
            R.id.nav_ingresos -> {
               // mifragment = Fragment_chat()
                //fragmentSeleccionado=true
            }
            R.id.nav_cuenta -> {
                if((getTieneInfo(prefs)!!)){
                    mifragment = TusViajesGratisFragment()
                    fragmentSeleccionado = true
                }else{
                    mifragment = registrarDatosFragment()
                    fragmentSeleccionado = true
                }

            }
            R.id.nav_banca -> {
              //  mifragment= fragment_recargar_evans()
              // fragmentSeleccionado = true
            }
            R.id.nav_settings -> {
                if (!canDrawOverlayViews() && isXiaomi()) {
                    //permission not granted
                    Log.e("canDrawOverlayViews", "-No-");
                    dialogEmergente("ACTIVAR: !!Mostrar ventanas emergentes mientras se ejecuta en segundo plano")
                    //write above answered permission code for MIUI here
                }else {

                }
            }
            R.id.nav_help -> {
                mifragment = pasos_requeridos()
                fragmentSeleccionado = true
            }
            R.id.nav_notificacion->{
                initializeCountDrawer(false)
                setTieneNotificacionAdmin(prefs,false)

                mifragment= fragment_notificaciones_rv()
                fragmentSeleccionado = true
            }
            R.id.nav_car -> {

                mifragment = registrarDatosFragment()
                fragmentSeleccionado = true
            }
            R.id.nav_share->{
                val dialog= ReferidosDialogFragment()
                dialog.show(supportFragmentManager,"MainActivity")
            }
            R.id.nav_logout->{
             //   stopService(Intent(this@MainActivity, service_mqtt::class.java))
                setStatusService_active(prefs,false)
                removerData()
                removeSharedPreferences()
                logOut()
            }
            R.id.nav_exit -> {
                removerData()
            }
        }

//// ESTA CONDICION DIBUJA EN EL FRAGMENTO EL ITEM SELECCIONADO
        if (fragmentSeleccionado) {
            val manager = supportFragmentManager
            if (mifragment != null) {
                Log.e("navDrawe","entro aca x0"+mifragment.toString()+"  "+mapaInicio().toString())
                if (!(mifragment.toString().contains("mapaInicio"))){
                    switchon.visibility= View.GONE

                }else{
                    Log.e("navDrawe","entro aca"+ getaccountActivate(prefs))
                    if (getaccountActivate(prefs)){
                        Log.e("navDrawe","entro aca x2")
                        switchon.visibility= View.VISIBLE
                        switchon.isChecked= getStatusSwitch(dataDriver)
                    }

                }
                acv_imagebutton_back.visibility= View.GONE

                manager.beginTransaction().replace(R.id.main_layout_change_fragment, mifragment).commit()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
    fun Sfragmentdefault() {
        try{
            val manager = supportFragmentManager
            manager.beginTransaction().replace(R.id.main_layout_change_fragment, mapaInicio()).commit()
        }catch (E:Exception){
            Log.e("error",E.message)
        }


    }

    private fun logOut(){
        setStatusSwitch(prefs,false)
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun removeSharedPreferences(){
        val editor = prefs.edit()
        editor.clear()
        editor.apply()

        val editor2 = dataDriver.edit()
        editor2.clear()
        editor2.apply()
    }
    private fun comprobarStatusTrip() {
        if (!(getViajeId(dataDriver).equals("nulo"))){
            var statusTrip=RetrofitClient.getInstance().api.getStatusTrip(getViajeId(dataDriver)!!)
            statusTrip.enqueue(object : Callback<infoDriver> {
                override fun onFailure(call: Call<infoDriver>, t: Throwable) {
                    Log.e("error", t.message)
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
                override fun onResponse(call: Call<infoDriver>, response: Response<infoDriver>) {
                    if ( !(response.body()!!.isOk)){
                        val editor = dataDriver.edit()
                        editor.clear()
                        editor.apply()
                        setEstadoViews(dataDriver,2)
                        Log.e("statusTrip",response.message()+""+response.body()!!.isOk)
                    }
                    Log.e("statusTrip",response.message()+""+response.body()!!.isOk)

                }

            })
        }

    }
    private fun removerData(){
        val call = RetrofitClient.getInstance()
            .api.switchStatus(getToken(prefs)!!,  getDriverId_Prefs(prefs)!!, false)


        Log.e("salida","Saliendo ")
        call.enqueue(object : Callback<Driver> {
            override fun onResponse(call: Call<Driver>, response: Response<Driver>) {
                setStatusSwitch(dataDriver,false)
                setEstadoViews(dataDriver,1)
                Log.e("datos enviados","Saliendo ")
                finish()
            }

            override fun onFailure(call: Call<Driver>, t: Throwable) {
                Log.e("datos enviados","error ")
            }
        })

    }
    private fun setInfoUser(headerView: View) {

        var nombres = getUserName(prefs)
        var apellidos = getUserSurname(prefs)

        if (!TextUtils.isEmpty(nombres) && !TextUtils.isEmpty(apellidos)){
            headerView.header_nav_text_view_nombre?.text = nombres
            headerView.header_nav_text_view_apellido?.text = apellidos
        }
        if (!getRutaImagen(prefs).equals("nulo")){
            var file: File = File(getRutaImagen(prefs))

            if (file.exists()) {
//
                val myBitmap = BitmapFactory.decodeFile(getRutaImagen(prefs))
                Glide.with(application).asBitmap().load(getImageRotate(getRutaImagen(prefs)!!,myBitmap))
                    .apply(RequestOptions.circleCropTransform())
                    .into(headerView.nav_header_image_view_profile)

            }
        }
        Log.e("datatext", getRutaImagen(prefs))
        Log.e("datatext", getUserSurname(prefs))
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                val permission = permissions[0]
                val result = grantResults[0]
                if (permission == Manifest.permission.ACCESS_FINE_LOCATION) {
                    if (result == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this@MainActivity, "Permiso Activado", Toast.LENGTH_LONG)

                    } else {
                        Toast.makeText(this@MainActivity, "Permiso Denegado", Toast.LENGTH_LONG)
                    }
                }
            }

            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
    fun updateApi( listener: ComunicateFrag.mapa_inicio) {
        comunicateFrag = listener
    }

    fun updateComuChat( listener: ComunicateFrag.chat) {
        comunicateFragChat = listener
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.acv_imagebutton_back -> {
                if (acv_imagebutton_back.visibility== View.VISIBLE){
                    if (getsetClassBackFragmentToString(prefs).equals("mapaInicio")){
                        acv_imagebutton_back.visibility= View.GONE
                        if (getaccountActivate(prefs))
                            switchon.visibility= View.VISIBLE
                    }else{
                        acv_imagebutton_back.visibility= View.VISIBLE
                        switchon.visibility= View.GONE
                    }

                    val manager = supportFragmentManager
                    Log.e("updatenot", getClassBackFragment(prefs).toString())
                    manager.beginTransaction().replace(R.id.main_layout_change_fragment,
                        getClassBackFragment(prefs)
                    ).commit()
                }
            }
            R.id.nav_header_image_view_profile->{
                val manager = supportFragmentManager
                manager.beginTransaction().replace(R.id.main_layout_change_fragment, TusViajesGratisFragment()).commit()
                drawer_layout.closeDrawer(GravityCompat.START)
            }

        }
    }



}

