package com.evans.technologies.conductor.Utils.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.evans.technologies.conductor.R;
import com.evans.technologies.conductor.Retrofit.RetrofitClient;
import com.evans.technologies.conductor.Utils.ComunicacionesRealTime.OnclickItemListener;
import com.evans.technologies.conductor.fragments.fragment_notificaciones.fragment_notificaciones_rv;
import com.evans.technologies.conductor.model.infoDriver;
import com.evans.technologies.conductor.model.notification.notification;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.evans.technologies.conductor.Utils.Change_time_utilsKt.getDiferencia;
import static com.evans.technologies.conductor.Utils.UtilsKt.toastLong;

public class adapter_rv_notificaciones extends RecyclerView.Adapter<adapter_rv_notificaciones.ViewHolder> {

    Context context;
    int layoutResources;
    ArrayList<notification> notificaciones;
    OnclickItemListener listen;
    Activity activity;
    public adapter_rv_notificaciones(Context context, int layoutResources, ArrayList<notification> notificaciones, OnclickItemListener listen ){
        this.context=context;
        this.layoutResources=layoutResources;
        this.notificaciones=notificaciones;
        this.listen=listen;
       // this.activity=activity;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( context ).inflate( layoutResources,parent,false );
        ButterKnife.bind(this,view);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(notificaciones.get(position),listen);
    }

    @Override
    public int getItemCount() {
        if(notificaciones.size()>0){
            return notificaciones.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
      //  @BindView(R.id.dlrvn_img_tipo_notificacion)
        ImageView img_tipo_not;
       // @BindView(R.id.dlrvn_txt_notificacion_cuerpo)
        TextView txt_nto_cuerpo;
       // @BindView(R.id.dlrvn_txt_notificacion_fecha)
        TextView txt_nto_fecha;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_tipo_not = itemView.findViewById(R.id.dlrvn_img_tipo_notificacion);
            txt_nto_cuerpo = itemView.findViewById(R.id.dlrvn_txt_notificacion_cuerpo);
            txt_nto_fecha = itemView.findViewById(R.id.dlrvn_txt_notificacion_fecha);

        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(final notification notificaciones, final OnclickItemListener listen){
            if (!notificaciones.getState()){
                itemView.setBackground(context.getDrawable(R.drawable.border_notify_new));
            }else{
                itemView.setBackground(context.getDrawable(R.drawable.border_white));
            }
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    deleteNotify(itemView,getAdapterPosition());
                    return true;
                }
            });
            txt_nto_cuerpo.setText(notificaciones.getTitle());
            Date dateActual = new Date();
            Log.e("rv_noti",dateActual.toString());
           /* try{

            }catch (Exception e){
                Log.e("rv_noti","error  "+e.getMessage());
            }*/
           /* try{

               // LocalDateTime localDateTime = LocalDateTime.parse(datoTime.replace("T"," "));
               // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
               // String formatDateTime = localDateTime.format(formatter);
               // Date dateinicial= new Date(datoTime.replace("T"," "));


            }catch (Exception e){
                Log.e("rv_noti","error  "+e.getMessage());
            }*/
            /*String datoTime=notificaciones.getFHregister();
            Log.e("rv_noti",datoTime);
            String año= datoTime.charAt(0)+""+datoTime.charAt(1)+""+datoTime.charAt(2)+""+datoTime.charAt(3);
            String mes= datoTime.charAt(5)+""+datoTime.charAt(6);
            String dia= datoTime.charAt(8)+""+datoTime.charAt(9);
            String hora= datoTime.charAt(11)+""+datoTime.charAt(12);
            String minuto= datoTime.charAt(14)+""+datoTime.charAt(15);
            String segundo= datoTime.charAt(17)+""+datoTime.charAt(18);
            Log.e("rv_noti", "fechas   "+ Integer.parseInt(año) +"   "+
                    Integer.parseInt(mes)+"  "+Integer.parseInt(dia) +"   "+Integer.parseInt(hora) +"   "+Integer.parseInt(minuto)
                    +"   "+Integer.parseInt(segundo));
            Date dateNotifi = new Date(Integer.parseInt(año)-1900,Integer.parseInt(mes)-1,Integer.parseInt(dia)
                    ,Integer.parseInt(hora),Integer.parseInt(minuto),Integer.parseInt(segundo));
            Log.e("rv_noti",dateNotifi.toString());*/
           // Date datefinal = new Date();
          //  Date dateinicial= new Date(notificaciones.getFHregister());
            //
            txt_nto_fecha.setText(notificaciones.getFHregister());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listen.itemClickNotify(notificaciones,getAdapterPosition());
                }
            });
        }

    }
    private void deleteNotify(View itemView, int adapterPosition) {
        Call<infoDriver> deleteNotify= RetrofitClient.getInstance().getApi().deleteNotify(notificaciones.get(adapterPosition).getMessageID());
        deleteNotify.enqueue(new Callback<infoDriver>() {
            @Override
            public void onResponse(Call<infoDriver> call, Response<infoDriver> response) {
                if (response.isSuccessful()){
                    if (response.body().isOk()){


                    }
                }else{
                   // toastLong(get,"Error al borrar");
                }
                Log.e("correcto",response.code()+"");
            }

            @Override
            public void onFailure(Call<infoDriver> call, Throwable t) {

            }
        });
    }



}
