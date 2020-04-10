package com.evans.technologies.conductor.utils.Firebase

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.evans.technologies.conductor.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        Log.e(TAG, "nuevo mensaje recibido: ")
        Log.e(TAG, remoteMessage.data.toString())
        /*val launchIntent =
            applicationContext.getPackageManager().getLaunchIntentForPackage(applicationContext.getPackageName())
        launchIntent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(launchIntent)*/
       // remoteMessage.notification?.let {
          //  Log.d(TAG, "Message Notification Body: ")
           // sendNotification(remoteMessage)
       // }
        remoteMessage.data?.let {
            val intent = Intent("clase")
            intent.putExtra("data", remoteMessage.data.toString())
            val brod: NotificationReceiver =NotificationReceiver()
            brod.onReceive(applicationContext,intent)
           // val  broadcaster:LocalBroadcastManager = LocalBroadcastManager.getInstance(this)
           // broadcaster.sendBroadcast(intent)
        }
      /*  if (remoteMessage.notification!=null){

        }

        if(!remoteMessage.data.isEmpty()){


        }*/




    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
        Log.d(TAG, "sendRegistrationTokenToServer($token)")
    }

    @SuppressLint("WrongConstant")
    private fun sendNotification(messageBody: RemoteMessage) {
        //val intent = Intent(this, SplashScreen::class.java)
        val intent =
            applicationContext.packageManager.getLaunchIntentForPackage(applicationContext.packageName)
        intent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP )
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        var data:String=messageBody.data.toString()
        intent.putExtra("NotificationMessage",data)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.iconevans)
            .setContentTitle(messageBody.notification?.title)
            .setContentText(messageBody.notification?.body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            //.setColor(Color.BLUE)
            //.setWhen(System.currentTimeMillis()*2)
            //.setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
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


        //messageBody.data.toString()
       /* val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("notificacion_data","Hola es un mensake de prubea")
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT)
        Log.e("Notificacion","Entro a la notificacion")
        //val intent = Intent(this, MainActivity::class.java)
        //intent.putExtra("NotificationMessage",messageBody.data.toString())


        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.iconevans)
                .setContentTitle(messageBody.notification?.title)
                .setContentText(messageBody.notification?.body)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setFullScreenIntent(pendingIntent,true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
            .setColor(Color.BLUE)




        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = getString(R.string.evans_name)
            val descriptionText = getString(R.string.evans_descripcion)
            val importance = NotificationManager.IMPORTANCE_MAX
            val channel = NotificationChannel("evanstechnologiesdriver notificaciones", name, importance).apply {
                description = descriptionText
            }

            notificationManager.createNotificationChannel(channel)
        }
       // notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
        with(NotificationManagerCompat.from(this)) {
            notify(0, notificationBuilder.build())
        }*/
    }

    companion object {

        private const val TAG = "MyFirebaseMsgService"
    }
}