package com.evans.technologies.conductor.utils.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.evans.technologies.conductor.R;

import java.util.ArrayList;
import java.util.Map;

import static com.evans.technologies.conductor.utils.UtilsKt.getaccountActivate;
import static com.evans.technologies.conductor.utils.constantes.AppConstants.SEND_CLAVE_ADAPTER_DATOS_REQUERIDOS;
import static com.evans.technologies.conductor.utils.constantes.AppConstants.SEND_CLAVE_ADAPTER_TIPO_SOCIO;


public class Recycler_tipo_socio extends RecyclerView.Adapter< Recycler_tipo_socio.ViewHolder> {
    private Context mcontex;
    private  int layoutResources;
    private  ArrayList<Map<String, Object>>  comentariosArrayList;
    private OnItemClickListener2 Listen;
    private String clave;
    private SharedPreferences prefs;

    public Recycler_tipo_socio(Context mcontex, int layoutResources,String clave, ArrayList<Map<String, Object>> comentariosArrayList, OnItemClickListener2 Listen) {
        this.mcontex = mcontex;
        this.layoutResources = layoutResources;
        this.comentariosArrayList = comentariosArrayList;
        this.Listen = Listen;
        this.clave=clave;
        prefs=prefs = mcontex.getSharedPreferences("Preferences", Context.MODE_PRIVATE);
}
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( mcontex ).inflate( layoutResources,parent,false );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(comentariosArrayList.get(position),Listen);

    }


    @Override
    public int getItemCount() {
        if(comentariosArrayList.size()>0){
            return comentariosArrayList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

        TextView dots_txt_tipo,dots_txt_desc,dodr_txt_titulo,dodr_estado;
        ImageView mImage;

        RatingBar ratingBar;
        View vista;
        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            itemView.setEnabled(true);
            if (clave.equals(SEND_CLAVE_ADAPTER_TIPO_SOCIO)){
                itemView.setBackgroundColor(Color.BLACK);
                dots_txt_tipo =(TextView) itemView.findViewById( R.id.dots_txt_tipo );
                dots_txt_desc=(TextView) itemView.findViewById( R.id.dots_txt_desc );
            }else if (clave.equals(SEND_CLAVE_ADAPTER_DATOS_REQUERIDOS)){
                dodr_estado=(TextView) itemView.findViewById( R.id.dodr_estado );
                dodr_txt_titulo=(TextView) itemView.findViewById( R.id.dodr_txt_titulo );
                itemView.setBackgroundColor(Color.GREEN);
            }
          /*  mFecha=(TextView) itemView.findViewById( R.id.rv_comentarios_text_user_fech );
//            mTituloComentario.setVisibility( View.GONE );
            mComentario=(TextView) itemView.findViewById( R.id.rv_comentarios_text_user_comentarios );
            mNombreuser=(TextView) itemView.findViewById( R.id.rv_comentarios_name_user );
            mImage =(ImageView) itemView.findViewById( R.id.rv_comentarios_imagen_user_image );
            ratingBar=(RatingBar)itemView.findViewById( R.id.rv_comentarios_user_rating );*/
//jpeg png svg jpg

        }


        public void bind(final Map<String, Object> comentarios, final OnItemClickListener2 listen) {
            if (clave.equals(SEND_CLAVE_ADAPTER_TIPO_SOCIO)){
                dots_txt_tipo.setText( comentarios.get("tipo").toString());
                dots_txt_desc.setText( comentarios.get("desc").toString());
            }else if (clave.equals(SEND_CLAVE_ADAPTER_DATOS_REQUERIDOS)){
                dodr_txt_titulo.setText(comentarios.get("titulo").toString());
               // Log.e("recycler",comentarios.get("estado").toString()+" pos "+comentarios.get("titulo").toString());
                if (getaccountActivate(prefs)){
                    itemView.setEnabled(false);
                    dodr_estado.setText("Aprobado");
                    itemView.setBackgroundColor(Color.WHITE);
                }else if (comentarios.get("estado")!=null){

                    itemView.setEnabled(false);
                    dodr_estado.setText("Pendiente");
                    itemView.setBackgroundColor(Color.WHITE);
                }else {
                    dodr_estado.setText("No Enviado");
                }
            }
          /*  mNombreuser.setText( comentarios.getUser());
            mComentario.setText( comentarios.getTexto() );
            // mImage.setImageBitmap( StringToBitmap(comentarios.) );
            ratingBar.setRating( comentarios.getRating());
            mFecha.setText(comentarios.getFecha()+" " +comentarios.getHora());*/
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listen.OnClickListener2(comentarios,getAdapterPosition(),itemView);
                }
            });
        }
    }
    public interface OnItemClickListener2{
        void OnClickListener2(Map<String, Object> comentarios, int adapterPosition, View vista);
    }
}