package com.evans.technologies.conductor.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evans.technologies.conductor.Activities.MainActivity;
import com.evans.technologies.conductor.R;
import com.evans.technologies.conductor.Utils.Adapters.adapter_rv_chat;
import com.evans.technologies.conductor.Utils.ComunicacionesRealTime.ComunicateFrag;
import com.evans.technologies.conductor.Utils.ComunicacionesRealTime.OnclickItemListener;
import com.evans.technologies.conductor.model.chats;
import com.evans.technologies.conductor.model.notification.data;
import com.evans.technologies.conductor.model.notification.notification;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.evans.technologies.conductor.Utils.UtilsKt.getChatJson;
import static com.evans.technologies.conductor.Utils.UtilsKt.getDriverId_Prefs;
import static com.evans.technologies.conductor.Utils.UtilsKt.getLlaveChat;
import static com.evans.technologies.conductor.Utils.UtilsKt.getUserName;
import static com.evans.technologies.conductor.Utils.UtilsKt.setClaseActual;
import static com.evans.technologies.conductor.Utils.UtilsKt.setEstadoViews;
import static com.evans.technologies.conductor.Utils.UtilsKt.setStatusSwitch;
import static com.evans.technologies.conductor.Utils.UtilsKt.toastLong;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_chat extends Fragment implements View.OnClickListener {

    private View view;
    private RecyclerView.Adapter adapterRview;
    @BindView(R.id.ffc_rv_lista_chats)
    RecyclerView rv_chat;
    @BindView(R.id.ffc_btn_enviar_msg)
    ImageButton send_chat;
    @BindView(R.id.ffc_btn_select_photo)
    ImageButton select_photo;
    @BindView(R.id.ffc_edit_txt_mensaje_send)
    EditText typing_mensaje;
    SharedPreferences prefs,dataDriver;
    BroadcastReceiver receiver;
    ArrayList<chats> mensajes= new ArrayList<>(  );

    public Fragment_chat(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        prefs = getContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        dataDriver = getActivity().getSharedPreferences("datadriver", Context.MODE_PRIVATE);
        setClaseActual(prefs,new Fragment_chat().toString());
        view= inflater.inflate(R.layout.fragment_fragment_chat, container, false);
        ButterKnife.bind(this,view);
        send_chat.setOnClickListener(this);
        rv_chat.setLayoutManager( new LinearLayoutManager(getContext()) );
        //rv_chat.smoothScrollToPosition(adapterRview.getItemCount());
        adapterRview = new adapter_rv_chat(getContext(), R.layout.dialog_layout_rv_chat_user,R.layout.dialog_layout_rv_chat_other, mensajes, new OnclickItemListener() {


            @Override
            public void itemClickNotify(notification notifica, int adapterPosition) {

            }

            @Override
            public void itemClickChat(chats chat, int adapterPosition) {

            }
        });

        rv_chat.setAdapter(adapterRview);

        ((MainActivity) Objects.requireNonNull(getActivity())).updateComuChat(new ComunicateFrag.chat() {

            @Override
            public void sendDtaMessage(DataSnapshot dataSnapshot) {

                llenarData(dataSnapshot);
            }
        });
        Log.e("chat","entro primero aca");
        return view;
    }

    private void llenarData(DataSnapshot dataSnapshot) {
        if (dataSnapshot!=null){
                mensajes.clear();
                for (DataSnapshot dato:dataSnapshot.getChildren()){
                    chats android= dato.getValue(chats.class);
                    mensajes.add(android);

                }
                adapterRview.notifyDataSetChanged();
                try {
                    rv_chat.smoothScrollToPosition(adapterRview.getItemCount()-1);
                }catch (Exception e){

                }

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ffc_btn_enviar_msg:
                if (!typing_mensaje.getText().toString().isEmpty()) {
                    try{
                        View view = getActivity().getCurrentFocus();
                        view.clearFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                    }catch (Exception e ){

                    }
                    String send = typing_mensaje.getText().toString().trim();
                    String id = getDriverId_Prefs(prefs);
                    String nombre = getUserName(prefs);
                    Date date = new Date();
                    DateFormat hourFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Map<String, Object> map = new HashMap<String, Object>();

                    map.put("mensajeChat", send);
                    map.put("nombreChat", nombre);
                    map.put("idUserChat", id);
                    map.put("fechaChat", hourFormat.format(date));
                    FirebaseDatabase.getInstance().getReference().child("chatsFirebase").child(getDriverId_Prefs(prefs)).push().setValue(map);
                    typing_mensaje.setText("");
                    break;
                }else{
                    toastLong(getActivity(),"No se puede enviar mensajes vacios");
                }
        }
    }
}
