package com.evans.technologies.conductor.Utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.*
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import com.evans.technologies.conductor.Activities.MainActivity
import com.evans.technologies.conductor.R
import com.evans.technologies.conductor.fragments.change_password.changepassword
import com.evans.technologies.conductor.fragments.change_password.correo
import com.evans.technologies.conductor.fragments.change_password.set_codigo
import com.evans.technologies.conductor.fragments.mapaInicio
import com.evans.technologies.conductor.fragments.pasos_requeridos
import com.evans.technologies.conductor.fragments.tipo_Socio
import com.google.firebase.messaging.FirebaseMessagingService
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.geometry.LatLng
import java.io.File
import java.io.FileOutputStream
import java.util.*


fun settokenrecuperar(navFragment: SharedPreferences, id:String){
    val editor = navFragment.edit()
    editor.putString("settokenrecuperar",id)
    editor.apply()
}
fun gettokenrecuperar(navFragment: SharedPreferences): String? {
    return navFragment.getString("settokenrecuperar","")
}
fun setIDrecuperar(navFragment: SharedPreferences, id:String){
    val editor = navFragment.edit()
    editor.putString("setIDrecuperar",id)
    editor.apply()
}
fun getIDrecuperar(navFragment: SharedPreferences): String? {
    return navFragment.getString("setIDrecuperar","")
}
fun setNavFragment(navFragment: SharedPreferences, Frag:String){
    val editor = navFragment.edit()
    editor.putString("NavFragment",Frag)
    editor.apply()
}
fun getBeforeNavFragment(navFragment: SharedPreferences): Fragment? {
    val data=navFragment.getString("NavFragment","")
    if (data.toString().contains("changepassword")){
        return changepassword()
    }
    if (data.toString().contains("correo")){
        return correo()
    }
    if (data.toString().contains("set_codigo")){
        return set_codigo()
    }
    return null
}
///**************************************************
fun Activity.toastShort(mensaje:String){
    Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show()
}
fun Activity.toastLong(mensaje:String){
    Toast.makeText(this,mensaje,Toast.LENGTH_LONG).show()
}
fun ViewGroup.inflate(layoutId: Int): View {
    return LayoutInflater.from(context).inflate(layoutId,this,false)
}
fun getUserEmail(prefs: SharedPreferences): String? {
    return prefs.getString("email", "")
}
fun llaveChat(prefs: SharedPreferences,llaveChat:String){
    val editor = prefs.edit()
    editor.putString("llaveChat",llaveChat)
    editor.apply()
}
fun getLlaveChat(prefs: SharedPreferences): String? {
    return prefs.getString("llaveChat", "")
}

fun getUserPassword(prefs: SharedPreferences): String? {
    return prefs.getString("password", "")
}

fun getUserName(prefs: SharedPreferences): String? {
    return prefs.getString("name", "")
}
fun getDriverId_Prefs(prefs: SharedPreferences): String? {
    return prefs.getString("id", "")
}
fun getToken(prefs: SharedPreferences): String? {
    return prefs.getString("token", "")
}
fun getUserSurname(prefs: SharedPreferences): String? {
    return prefs.getString("surname", "")
}
 fun setVehiculoInfo(prefs: SharedPreferences, brandCar: String?, colorCar: String?, modelCar: String?, licenseCar: String?) {
     val editor = prefs.edit()
     editor.putString("brandCar",brandCar)
     editor.putString("colorCar",colorCar)
     editor.putString("modelCar",modelCar)
     editor.putString("licenseCar",licenseCar)
     editor.apply()
}
fun getBrandCar(prefs: SharedPreferences): String? {
    return prefs.getString("brandCar", "")
}
fun getcolorCar(prefs: SharedPreferences): String? {
    return prefs.getString("colorCar", "")
}
fun getmodelCar(prefs: SharedPreferences): String? {
    return prefs.getString("modelCar", "")
}
fun getlicenseCar(prefs: SharedPreferences): String? {
    return prefs.getString("licenseCar", "")
}
fun getCityDriver(prefs: SharedPreferences): String? {
    return prefs.getString("city", "")
}

fun getUserCellphone(prefs: SharedPreferences): String? {
    return prefs.getString("cellphone", "")
}
fun setStatusSwitch(dataDriver: SharedPreferences,status:Boolean){
    val editor = dataDriver.edit()
    editor.putBoolean("statusSwitch",status)
    editor.apply()
}

fun getTienePasajero(dataDriver: SharedPreferences): Boolean? {
    return dataDriver.getBoolean("pasajeroTiene", false)
}
fun setTienePasajero(dataDriver: SharedPreferences,pasajero:Boolean){
    val editor = dataDriver.edit()
    editor.putBoolean("pasajeroTiene",pasajero)
    editor.apply()
}
fun getCronometroStop(dataDriver: SharedPreferences): Boolean? {
    return dataDriver.getBoolean("CronometroStop", false)
}
fun setCronometroStop(dataDriver: SharedPreferences,stop:Boolean){
    val editor = dataDriver.edit()
    editor.putBoolean("CronometroStop",stop)
    editor.apply()
}
fun setTieneInfo(dataDriver: SharedPreferences,info:Boolean){
    val editor = dataDriver.edit()
    editor.putBoolean("tieneinfoDriver",info)
    editor.apply()
}
fun getTieneInfo(prefs: SharedPreferences): Boolean? {
    return prefs.getBoolean("tieneinfoDriver", false)
}
fun setRutaImagen(prefs: SharedPreferences, ruta:String) {
    val editor = prefs.edit()
    editor.putString("rutaImg",ruta)
    editor.apply()
}
fun getRutaImagen(prefs: SharedPreferences):String?{
    return prefs.getString("rutaImg", "nulo")
}

fun setEstadoViews(dataDriver: SharedPreferences,estado:Int){
    val editor = dataDriver.edit()
    editor.putInt("estadoView",estado)
    editor.apply()
}

fun getEstadoView(dataDriver: SharedPreferences): Int?{
    return dataDriver.getInt("estadoView", 1)
}
fun setdataNotification_noti(dataDriver: SharedPreferences,data:String,boleano:String){
    val editor = dataDriver.edit()
    editor.putString("dataNotification_noti",data+boleano)
    editor.apply()
}
fun getdataNotification_noti(dataDriver: SharedPreferences): String?{
    return dataDriver.getString("dataNotification_noti", "nulo")
}
fun setLastDirection(dataDriver: SharedPreferences,direction:String){
    val editor = dataDriver.edit()
    editor.putString("lastDirections",direction)
    editor.apply()
}
fun getLastDirection(dataDriver: SharedPreferences): String?{
    return dataDriver.getString("lastDirections", "")
}

fun setDataNotification(dataDriver: SharedPreferences,origenLat:String,origenLog:String,destLat:String,destLong:String,
                        startAddress:String,endAddress:String,price:String,discountPrice:String,userId:String,driverId:String,viajeId:String){
    val editor = dataDriver.edit()
    editor.putString("origenLat",origenLat)
    editor.putString("origenLog",origenLog)
    editor.putString("destLat",destLat)
    editor.putString("destLong",destLong)
    editor.putString("startAddress",startAddress)
    editor.putString("endAddress",endAddress)
    editor.putString("price",price)
    editor.putString("discountPrice",discountPrice)
    editor.putString("userId",userId)
    editor.putString("driverId",driverId)
    editor.putString("viajeId",viajeId)
    editor.apply()
}
//////////////
fun getOrigenLat(dataDriver: SharedPreferences): String? {
    return dataDriver.getString("origenLat", "nulo")
}
fun getOrigenLong(dataDriver: SharedPreferences): String? {
    return dataDriver.getString("origenLog", "nulo")
}
fun getDestinoLat(dataDriver: SharedPreferences): String? {
    return dataDriver.getString("destLat", "nulo")
}
fun getDestinoLong(dataDriver: SharedPreferences): String? {
    return dataDriver.getString("destLong", "nulo")
}

fun getPrice(dataDriver: SharedPreferences): String? {
    return dataDriver.getString("price", "nulo")
}
fun getPriceDiscount(dataDriver: SharedPreferences): String? {
    return dataDriver.getString("discountPrice", "0.0")
}
fun setPrice(dataDriver: SharedPreferences,precio:String){
    var editor=dataDriver.edit()
    editor.putString("price",precio)
    editor.apply()
}
fun setPriceDiscount(dataDriver: SharedPreferences,discountPrice:String) {
    var editor=dataDriver.edit()
    editor.putString("discountPrice",discountPrice)
    editor.apply()
}
fun getViajeId(dataDriver: SharedPreferences): String? {
    return dataDriver.getString("viajeId", "nulo")
}
fun getUserId(dataDriver: SharedPreferences): String? {
    return dataDriver.getString("userId", "nulo")
}
fun getStartAddress(dataDriver: SharedPreferences): String? {
    return dataDriver.getString("startAddress", "nulo")
}
fun getEndAddress(dataDriver: SharedPreferences): String? {
    return dataDriver.getString("endAddress", "nulo")
}
//////////////////

fun getStatusSwitch(dataDriver: SharedPreferences): Boolean {
    return dataDriver.getBoolean("statusSwitch", false)
}
fun SharedPreferences.edit(func: SharedPreferences.Editor.() -> Unit){
    val editor= edit()
    editor.func()
    editor.apply()
}

fun EditText.passwordvalido() =this.text.toString().isNotEmpty()&&
        this.text.toString().length.compareTo(5)==1
fun EditText.userValido() = this.text.toString().isNotEmpty()&&
        this.text.toString().contains('@')
fun Activity.limpiarNotify(){
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.cancelAll()
}
//Onbtener Iniciacion de datos para el soat y tipo de conductor con prefs
fun setPasosRequeridos_init(prefs: SharedPreferences,pasoN:Int){
    val editor = prefs.edit()
    editor.putInt("PasosRequeridos_init",pasoN)
    editor.apply()
}
fun getPasosRequeridos_init(prefs: SharedPreferences): Int?{
    return prefs.getInt("PasosRequeridos_init", 0)
}


fun saveToInternalStorage(bitmapImage: Bitmap, name:String):String?{
    /* var cw: ContextWrapper =  ContextWrapper(getApplicationContext())
     var directory = cw.getDir("imageDir", Context.MODE_PRIVATE)
     var mypath=File(directory,"profile.jpg")*/

    val path =  Environment.getExternalStorageDirectory().path+ "/" + "evansTaxy" + "/"
    var data=File(path)
    if (!data.exists()) {
        data.mkdirs()
    }
    val file =
        File(path, "evanstechnologiesdriver$name.jpg")

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
//Obtener Clase
fun setClassBackFragment(prefs: SharedPreferences,direction:String){
    val editor = prefs.edit()
    editor.putString("ClassBackFragment",direction)
    editor.apply()
}
fun getsetClassBackFragmentToString(prefs: SharedPreferences): String? {
    return prefs.getString("ClassBackFragment", "a")
}
fun setImgUrlProfile(prefs: SharedPreferences,url:String){
    val editor = prefs.edit()
    editor.putString("imgurl",url)
    editor.apply()
}
fun getImgUrlProfile(prefs: SharedPreferences): String? {
    return prefs.getString("imgurl", "")
}
fun getClassBackFragment(prefs: SharedPreferences): Fragment{
    when(prefs.getString("ClassBackFragment", "")){

        "pasos_requeridos"->{
            return pasos_requeridos()
        }
        "tipo_Socio"->{
            return tipo_Socio()
        }
        "mapaInicio"->{
            return mapaInicio()
        }
    }
    return mapaInicio()
}
// Documnetos llenados del police y sus set y getters
fun datosLlenadossoatetc(prefs: SharedPreferences): Boolean {
    return getpoliceRecordCert(prefs)!!&&getcriminalRecodCert(prefs)!!&&getdriverLicense(prefs)!!&&
            getsoat(prefs)!!&&getpropertyCardForward(prefs)!!&&getpropertyCardBack(prefs)!!
}
fun setDatosLlenados(prefs:SharedPreferences, policeRecordCert:Boolean,criminalRecodCert:Boolean,
                     driverLicense:Boolean,soat:Boolean,propertyCardForward:Boolean,propertyCardBack:Boolean ){
    val editor = prefs.edit()
    editor.putBoolean("policeRecordCert",policeRecordCert)
    editor.putBoolean("criminalRecodCert",criminalRecodCert)
    editor.putBoolean("driverLicense",driverLicense)
    editor.putBoolean("soat",soat)
    editor.putBoolean("propertyCardForward",propertyCardForward)
    editor.putBoolean("propertyCardBack",propertyCardBack)
    editor.apply()
}
fun getpoliceRecordCert(prefs: SharedPreferences):Boolean?{
    return prefs.getBoolean("policeRecordCert",true)
}
fun getcriminalRecodCert(prefs: SharedPreferences):Boolean?{
    return prefs.getBoolean("criminalRecodCert",true)
}
fun getdriverLicense(prefs: SharedPreferences):Boolean?{
    return prefs.getBoolean("driverLicense",false)
}
fun getsoat(prefs: SharedPreferences):Boolean?{
    return prefs.getBoolean("soat",false)
}
fun getpropertyCardForward(prefs: SharedPreferences):Boolean?{
    return prefs.getBoolean("propertyCardForward",false)
}
fun getpropertyCardBack(prefs: SharedPreferences):Boolean?{
    return prefs.getBoolean("propertyCardBack",false)
}

fun setpoliceRecordCert(prefs: SharedPreferences){
    val editor = prefs.edit()
    editor.putBoolean ("policeRecordCert",true)
    editor.apply()
}
fun setcriminalRecodCert(prefs: SharedPreferences){
    val editor = prefs.edit()
    editor.putBoolean ("criminalRecodCert",true)
    editor.apply()
}
fun setdriverLicense(prefs: SharedPreferences){
    val editor = prefs.edit()
    editor.putBoolean ("driverLicense",true)
    editor.apply()
}
fun setsoat(prefs: SharedPreferences){
    val editor = prefs.edit()
    editor.putBoolean ("soat",true)
    editor.apply()
}
fun setpropertyCardForward(prefs: SharedPreferences){
    val editor = prefs.edit()
    editor.putBoolean ("propertyCardForward",true)
    editor.apply()
}
fun setpropertyCardBack(prefs: SharedPreferences){
    val editor = prefs.edit()
    editor.putBoolean ("propertyCardBack",true)
    editor.apply()
}
fun satosLlenadossoatetc(prefs: SharedPreferences):Boolean?{
    return getpoliceRecordCert(prefs)!!&&getcriminalRecodCert(prefs)!!&&getdriverLicense(prefs)!!&&
            getsoat(prefs)!!&&getpropertyCardForward(prefs)!!&&getpropertyCardBack(prefs)!!
}
fun setTipoSocio_init(prefs: SharedPreferences,TipoN:Int){
    val editor = prefs.edit()
    editor.putInt("TipoSocio_init",TipoN)
    editor.apply()
}
fun getTipoSocio_init(prefs: SharedPreferences): Int?{
    return prefs.getInt("TipoSocio_init", 0)
}
//Detecatar formato fotos

fun detectar_formato(texto: String): String {
    return if (texto.contains("jpg")) {
        "jpg"
    } else if (texto.contains("jpeg")) {
        "jpeg"
    } else if (texto.contains("svg")) {
        "svg"
    } else if (texto.contains("png")) {
        "png"
    } else
        "ninguno"


}
fun Activity.getPath(uri: Uri): String {
    val projection = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = managedQuery(uri, projection, null, null, null)
    startManagingCursor(cursor)
    val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
    cursor.moveToFirst()
    return cursor.getString(column_index)
}
fun getaccountActivate(prefs: SharedPreferences):Boolean{
    return prefs.getBoolean("accountActivate",false)
}
fun setaccountActivate(prefs: SharedPreferences,valor:Boolean){
    val Editor=prefs.edit()
    Editor.putBoolean("accountActivate",valor)
    Editor.apply()
}

@SuppressLint("WrongConstant")
fun Context.sendNotification(titulo:String,body:String) {

    val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    val notificationBuilder = NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.iconevans)
        .setContentTitle(titulo)
        .setContentText(body)
        .setAutoCancel(true)
        .setSound(defaultSoundUri)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        //.setColor(Color.BLUE)
    val notificationManager = getSystemService(FirebaseMessagingService.NOTIFICATION_SERVICE) as NotificationManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // Create the NotificationChannel
        val name = getString(R.string.evans_name)
        val descriptionText = getString(R.string.evans_descripcion)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(getString(R.string.default_notification_channel_id), name, importance)
        mChannel.description = descriptionText
        mChannel.setShowBadge(true)
        mChannel.canShowBadge()
        mChannel.enableLights(true)
        mChannel.setLightColor(Color.RED);
        mChannel.enableVibration(true)
        mChannel.setVibrationPattern( longArrayOf(100, 200, 300, 400, 500))
        notificationManager.createNotificationChannel(mChannel)
    }
    notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
}
fun getPrimerplano(prefs: SharedPreferences): Boolean? {
    return prefs.getBoolean("getPrimerplano", true)

}
fun setPrimerplano(prefs: SharedPreferences,primerPlano:Boolean){
    val editor = prefs.edit()
    editor.putBoolean("getPrimerplano",primerPlano)
    editor.apply()
}
fun Context.startNewActivity( packageName:String,data:String) {
    var intent = getPackageManager().getLaunchIntentForPackage(packageName);
    if (intent == null) {
        // Bring user to the market or let them choose an app?
        intent = Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + packageName))
    }
    intent.putExtra("data", data)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}
fun Context.launchApp( packageName:String) {
    var intent =  Intent()
    intent.setPackage(packageName)

    var pm = packageManager
    var resolveInfos:List<ResolveInfo> = pm.queryIntentActivities(intent, 0)
    Collections.sort(resolveInfos,  ResolveInfo.DisplayNameComparator(pm))

    if(resolveInfos.size > 0) {
        var launchable:ResolveInfo = resolveInfos[0]
        var  activity:ActivityInfo = launchable.activityInfo
        var name:ComponentName= ComponentName(activity.applicationInfo.packageName,
                activity.name)
        var i=Intent(Intent.ACTION_MAIN)

        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED

        i.component = name

        startActivity(i)
    }
}
fun  rotateImageSouce(source:Bitmap, angle: Int):Bitmap {
    var matrix: Matrix =  Matrix()
    matrix.postRotate(angle.toFloat())
    return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                               matrix, true)
}
fun getImageRotate(path:String,bitmap:Bitmap):Bitmap{
    var ei: ExifInterface =  ExifInterface(path)
    var orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                     ExifInterface.ORIENTATION_UNDEFINED)

    var rotatedBitmap:Bitmap = bitmap
    when(orientation) {

         ExifInterface.ORIENTATION_ROTATE_90->{
             rotatedBitmap = rotateImageSouce(bitmap, 90)
         }
         ExifInterface.ORIENTATION_ROTATE_180->{
             rotatedBitmap = rotateImageSouce(bitmap, 180)
         }
         ExifInterface.ORIENTATION_ROTATE_270->{
             rotatedBitmap = rotateImageSouce(bitmap, 270)
         }
        ExifInterface.ORIENTATION_NORMAL->{
            rotatedBitmap = bitmap
        }
    }
    return rotatedBitmap
}
fun getClaseMapaInicio(prefs: SharedPreferences): Boolean? {
    if (prefs.getString("claseActual", "a")!!.contains("mapaInicio")) {
        return true
    }
    return false
}
fun getClaseChat(prefs: SharedPreferences): Boolean? {
    if (prefs.getString("claseActual", "a")!!.contains("Fragment_chat")) {
        return true
    }
    return false
}
fun setClaseActual(prefs: SharedPreferences,clase:String){
    val editor = prefs.edit()
    editor.putString("claseActual",clase)
    editor.apply()

}
fun getCurrentRoute(dataDriver: SharedPreferences): String? {

    return dataDriver.getString("currentRoute", "nulo")
}
fun setCurrentRoute(dataDriver:SharedPreferences,currentRoute:String){
    val editor = dataDriver.edit()
    editor.putString("currentRoute",currentRoute)
    editor.apply()

}
fun getgetTieneNotificacionViaje(dataDriver: SharedPreferences): Boolean? {
    return dataDriver.getBoolean("getTieneNotificacionViaje", false)
}
fun setTieneNotificacionViaje(dataDriver: SharedPreferences,notificacionActiva:Boolean){
    val editor = dataDriver.edit()
    editor.putBoolean("getTieneNotificacionViaje",notificacionActiva)
    editor.apply()
}
//Aceptado iomg
fun getEnvioVocher(prefs: SharedPreferences): Boolean? {
    return prefs.getBoolean("setEnvioVocher", false)
}
fun getIsReferred(prefs: SharedPreferences): Boolean? {
    return prefs.getBoolean("isreferred", false)
}
fun setReferido(prefs: SharedPreferences,refirio:Boolean){
    val editor = prefs.edit()
    editor.putBoolean("isreferred",refirio)
    editor.apply()
}
fun setEnvioVocher(prefs: SharedPreferences,voucher:Boolean){
    val editor = prefs.edit()
    editor.putBoolean("setEnvioVocher",voucher)
    editor.apply()
}
fun getVoucherAceptado(prefs: SharedPreferences): Boolean? {
    return prefs.getBoolean("getVoucherAceptado", false)
}

fun setVoucherAceptado(prefs: SharedPreferences,voucher:Boolean){
    val editor = prefs.edit()
    editor.putBoolean("getVoucherAceptado",voucher)
    editor.apply()
}
fun setTieneNotificacionAdmin(prefs: SharedPreferences,noti:Boolean){
    val editor = prefs.edit()
    editor.putBoolean("TieneNotificacionAdmin",noti)
    editor.apply()
}
fun getTieneNotificacionAdmin(prefs: SharedPreferences): Boolean? {
    return prefs.getBoolean("TieneNotificacionAdmin", false)
}
fun setChatJson(prefs: SharedPreferences,chatJson:String){
    val editor = prefs.edit()
    editor.putString("setChatJson",chatJson)
    editor.apply()
}
fun getChatJson(prefs: SharedPreferences): String? {
    return prefs.getString("setChatJson", "nulo")
}
fun getestaFragmentChat(prefs: SharedPreferences):Boolean{
    if (prefs.getString("claseActual", "a")!!.contains("Fragment_chat")) {
        return true
    }
    return false
}
//fun  Activity.isMyServiceRunning( serviceClass:Class<service_mqtt>):Boolean {
//    val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
//    for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
//        if (serviceClass.getName().equals(service.service.className)) {
//            return true
//        }
//    }
//    return false
//}
fun setStatusService_active(prefs: SharedPreferences,status:Boolean){
    val editor = prefs.edit()
    editor.putBoolean("setStatusService_active",status)
    editor.apply()
}
fun getStatusService_active(prefs: SharedPreferences): Boolean? {
    return prefs.getBoolean("setStatusService_active", false)
}
fun setLastPostition(prefs: SharedPreferences,lat:Double,long:Double){
    val editor = prefs.edit()
    editor.putString("lastPositionLat",lat.toString())
    editor.putString("lastPositionLong",long.toString())
    editor.apply()
}
fun GetLastPosition(prefs: SharedPreferences): Point? {
    if (prefs.getString("lastPositionLat","").equals("")){
        return null
    }else{
        return Point.fromLngLat(prefs.getString("lastPositionLong","")!!.toDouble(),prefs.getString("lastPositionLat","")!!.toDouble())
    }
}
fun getApiWebVersion(prefs: SharedPreferences): String? {
    return prefs.getString("getApiWebVersion", "0")
}
fun setApiWebVersion(prefs: SharedPreferences,version:String){
    val editor = prefs.edit()
    editor.putString("getApiWebVersion",version)
    editor.apply()
}
fun Activity.getViewUpdateVersion(context:Context){
    var dialogClickListener: DialogInterface.OnClickListener  =  DialogInterface.OnClickListener { dialog, which ->
        when (which) {
            DialogInterface.BUTTON_POSITIVE -> {
                val appPackageName = context.packageName // getPackageName() from Context or Activity object
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
                } catch (anfe: android.content.ActivityNotFoundException) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
                }
                finish()

            }

        }
    }
    var builder: AlertDialog.Builder  =  AlertDialog.Builder(context)
    builder.setCancelable(false)
    builder.setMessage("Actualize la version por favor").setPositiveButton("Actualizar", dialogClickListener).show()
}

fun Context.getVersionApp():String{
    var version=0
    try {
        val pInfo = getPackageManager().getPackageInfo(packageName, 0)
        version = pInfo.versionCode
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return ""+version!!
}
/*
* fun  Activity.isMyServiceRunning( serviceClass:Class<Any>):Boolean {
    var manager =  getSystemService(Context.ACTIVITY_SERVICE)as ActivityManager
    for ()
    for ( service: ActivityManager.RunningServiceInfo : manager.getRunningServices(Integer.MAX_VALUE)) {
        if (serviceClass.getName().equals(service.service.getClassName()))
        { return true; }
    }
    return false;
}
*/
