package com.evans.technologies.conductor.utils.Firebase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.evans.technologies.conductor.ui.main.view.MainActivity;

import static com.evans.technologies.conductor.utils.UtilsKt.getClaseMapaInicio;
import static com.evans.technologies.conductor.utils.UtilsKt.getPrimerplano;
import static com.evans.technologies.conductor.utils.UtilsKt.setdataNotification_noti;


public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent data) {
        //Judging whether the app process survives
        SharedPreferences prefs = context.getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        SharedPreferences dataDriver = context.getSharedPreferences("datadriver", Context.MODE_PRIVATE);
        setdataNotification_noti(dataDriver,"nulo","");
        Log.e("getData", "entro onrecive" );
        if (getPrimerplano(prefs)){
            if (getClaseMapaInicio(prefs)){
                Log.e("getData", getClaseMapaInicio(prefs)+" esta en la clase mapa");
                LocalBroadcastManager broadcaster= LocalBroadcastManager.getInstance(context);
                broadcaster.sendBroadcast(data);
            }else{
                Log.e("LifecycleObserver", "is open");
                setdataNotification_noti(dataDriver,data.getStringExtra("data"),"");
                Intent launchIntent=new Intent(context, MainActivity.class);
                launchIntent.setFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                context.startActivity(launchIntent);
            }
        }else {
            Log.e("LifecycleObserver", "the app process is dead"+"\n"+data.getStringExtra("data"));
            //context.startActivity(new Intent(context,SplashScreen.class));
           // startNewActivity(context,"com.evanstechnologiesdriver",data.getStringExtra("data"));
            //launchApp(context,"com.evanstechnologiesdriver");
            setdataNotification_noti(dataDriver,data.getStringExtra("data"),"");
            Log.e("getData", "segundo plano entro aca para abrir intent" );
            Intent launchIntent = context.getPackageManager().
                    getLaunchIntentForPackage("com.evans.technologies.driver");
            launchIntent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
          /*  try{
                launchIntent.putExtra("data",data.getStringExtra("data"));
            }catch (Exception e){

            }*/

            context.startActivity(launchIntent);
        }
       /* if(SystemUtils.isAppAlive(context, "com.evanstechnologiesdriver")){
            Log.e("NotificationReceiver", "the app process is dead"+"\n"+data.getStringExtra("data"));
            Intent launchIntent = context.getPackageManager().
                    getLaunchIntentForPackage("com.evanstechnologiesdriver");
            launchIntent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            launchIntent.putExtra("data",data.getStringExtra("data"));
            context.startActivity(launchIntent);

        }else {
            //If the app process has been killed, restart app first, pass the start parameters of DetailActivity into Intent, and the parameters are passed into MainActivity through SplashActivity. At this time, the initialization of APP has been completed, and in MainActivity, you can jump to DetailActivity according to the input parameters.
            Log.e("NotificationReceiver", "is open");
            LocalBroadcastManager broadcaster= LocalBroadcastManager.getInstance(context);
            broadcaster.sendBroadcast(data);
        }*/
    }
}