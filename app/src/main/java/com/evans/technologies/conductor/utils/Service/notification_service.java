package com.evans.technologies.conductor.utils.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.evans.technologies.conductor.utils.Firebase.SystemUtils;


public class notification_service extends Service {
    Context context=this;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       /* Intent data =new  Intent("clase");
        data.putExtra("data", remoteMessage.data.toString());
        NotificationReceiver brod=new NotificationReceiver();
        brod.onReceive(context,intent);*/
        if(SystemUtils.isAppAlive(context, "com.evans.technologies.driver")){

            LocalBroadcastManager broadcaster= LocalBroadcastManager.getInstance(context);
          //  broadcaster.sendBroadcast();
        }else {
            //If the app process has been killed, restart app first, pass the start parameters of DetailActivity into Intent, and the parameters are passed into MainActivity through SplashActivity. At this time, the initialization of APP has been completed, and in MainActivity, you can jump to DetailActivity according to the input parameters.
            Log.i("NotificationReceiver", "the app process is dead");
            Intent launchIntent = context.getPackageManager().
                    getLaunchIntentForPackage("com.evans.technologies.driver");
            launchIntent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            //launchIntent.putExtra("data",data.getStringExtra("data"));
            context.startActivity(launchIntent);
        }

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
