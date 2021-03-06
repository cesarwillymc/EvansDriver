package com.evans.technologies.conductor.Utils.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.evans.technologies.conductor.R;
import com.evans.technologies.conductor.Utils.ComunicacionesRealTime.OnclickItemListener;
import com.evans.technologies.conductor.model.chats;

import java.util.ArrayList;

import static com.evans.technologies.conductor.Utils.UtilsKt.getDriverId_Prefs;

public class adapter_rv_chat extends RecyclerView.Adapter< RecyclerView.ViewHolder> {

    Context context;
    int layoutResources_other,layoutResources_user;
    ArrayList<chats> chat;
    OnclickItemListener listen;
    SharedPreferences prefs;
    View user;
    View other;
    public adapter_rv_chat(Context context, int layoutResources_user, int layoutResources_other, ArrayList<chats> chat, OnclickItemListener listen){
        this.context=context;
        this.layoutResources_other=layoutResources_other;
        this.layoutResources_user=layoutResources_user;
        this.chat=chat;
        this.listen=listen;
        prefs=context.getSharedPreferences("Preferences", Context.MODE_PRIVATE);
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         user = LayoutInflater.from( context ).inflate( layoutResources_user,parent,false );
         other = LayoutInflater.from( context ).inflate( layoutResources_other,parent,false );
        if (viewType==1){
            return  new mismensajes(user);
        }
            return new othermensajes(other);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (chat.get(position).getIdUserChat().equals(getDriverId_Prefs(prefs))){
            ((mismensajes)holder).mensaje.setText(chat.get(position).getMensajeChat());
            ((mismensajes)holder).fecha.setText(chat.get(position).getFechaChat());
            ((mismensajes)holder).nombre.setText(chat.get(position).getNombreChat());
            ((mismensajes)holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listen.itemClickChat(chat.get(position),position);
                }
            });
            // itemView.setBackground(context.getResources().getDrawable(R.drawable.border_text));
            // itemView.setLayoutParams(params);
        }else{
            ((othermensajes)holder).mensajeOther.setText(chat.get(position).getMensajeChat());
            ((othermensajes)holder).fechaOther.setText(chat.get(position).getFechaChat());
            ((othermensajes)holder).nombreOther.setText(chat.get(position).getNombreChat());
            ((othermensajes)holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listen.itemClickChat(chat.get(position),position);
                }
            });
            // itemView.setBackground( context.getResources().getDrawable(R.drawable.border_search));
        }
    }

    @Override
    public int getItemViewType(int position) {

          if (chat.get(position).getIdUserChat().equals(getDriverId_Prefs(prefs))) {
              return 1;
          }else {
              return 2;
          }

    }

    @Override
    public int getItemCount() {
        if(chat.size()>0){
            return chat.size();
        }
        return 0;
    }

    public class mismensajes extends RecyclerView.ViewHolder{
        // @BindView(R.id.dlrvc_txt_username)
        TextView nombre;
        //@BindView(R.id.dlrvc_txt_fecha)
        TextView fecha;
        // @BindView(R.id.dlrvc_txt_mensaje)
        TextView mensaje;
        public mismensajes(@NonNull View itemView) {
            super(itemView);
            nombre= itemView.findViewById(R.id.dlrvc_txt_username);
            fecha= itemView.findViewById(R.id.dlrvc_txt_fecha);
            mensaje= itemView.findViewById(R.id.dlrvc_txt_mensaje);
            //

        }


    }
    public class othermensajes extends RecyclerView.ViewHolder{
        // @BindView(R.id.dlrvc_txt_username)
        TextView nombreOther;
        //@BindView(R.id.dlrvc_txt_fecha)
        TextView fechaOther;
        // @BindView(R.id.dlrvc_txt_mensaje)
        TextView mensajeOther;
        public othermensajes(@NonNull View itemView) {
            super(itemView);
            nombreOther= itemView.findViewById(R.id.dlrvc_txt_usernameOther);
            fechaOther= itemView.findViewById(R.id.dlrvc_txt_fechaOther);
            mensajeOther= itemView.findViewById(R.id.dlrvc_txt_mensajeOther);
        }
    }
}
