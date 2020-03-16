package com.evans.technologies.conductor.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.evans.technologies.conductor.R;
import com.evans.technologies.conductor.Utils.Adapters.Recycler_tipo_socio;
import com.evans.technologies.conductor.Utils.ComunicacionesRealTime.updateListenerNotifications;
import com.evans.technologies.conductor.fragments.fragment_pasos_requeridos.f_pasos_requeridos;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.evans.technologies.conductor.Utils.UtilsKt.datosLlenadossoatetc;
import static com.evans.technologies.conductor.Utils.UtilsKt.getUserName;
import static com.evans.technologies.conductor.Utils.UtilsKt.getaccountActivate;
import static com.evans.technologies.conductor.Utils.UtilsKt.getcriminalRecodCert;
import static com.evans.technologies.conductor.Utils.UtilsKt.getdriverLicense;
import static com.evans.technologies.conductor.Utils.UtilsKt.getpoliceRecordCert;
import static com.evans.technologies.conductor.Utils.UtilsKt.getpropertyCardBack;
import static com.evans.technologies.conductor.Utils.UtilsKt.getpropertyCardForward;
import static com.evans.technologies.conductor.Utils.UtilsKt.getsoat;
import static com.evans.technologies.conductor.Utils.UtilsKt.setClaseActual;
import static com.evans.technologies.conductor.Utils.UtilsKt.toastLong;
import static com.evans.technologies.conductor.Utils.constantes.AppConstants.SEND_CLAVE_ADAPTER_DATOS_REQUERIDOS;


/**
 * A simple {@link Fragment} subclass.
 */
public class pasos_requeridos extends Fragment implements View.OnClickListener {
    RecyclerView recyclerView_info;
    View view;
    private RecyclerView.Adapter adapterRview;
    SharedPreferences prefs;
    updateListenerNotifications comunicateFrag;
    @BindView(R.id.fpr_txt_welcome)
    TextView welcome;
    @BindView(R.id.fpr_btn_finalizar)
    Button finalizado;
    public pasos_requeridos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        prefs = getContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        setClaseActual(prefs,new pasos_requeridos().toString());
        view= inflater.inflate(R.layout.fragment_pasos_requeridos, container, false);
        ButterKnife.bind(this,view);
        welcome.setText("Te damos la bienvenida, "+getUserName(prefs));
        finalizado.setOnClickListener(this);
        recyclerView_info=(RecyclerView) view.findViewById( R.id.fpr_rcv_pasos_requeridos ) ;
        LinearLayoutManager layoutManager= new LinearLayoutManager( getContext() );
        RecyclerView.LayoutManager recycler_view_manager_info=layoutManager;
        recyclerView_info.setLayoutManager( recycler_view_manager_info );

        Map<String,Object> datos = new ArrayMap<>();
        datos.put("titulo","Licencia de Conducir");
        /*Map<String,Object> datos1 = new ArrayMap<>();
        datos1.put("titulo","Certificado de antecedentes penales");
        Map<String,Object> datos2 = new ArrayMap<>();
        datos2.put("titulo","Certificado de antecedentes policiales");*/
        Map<String,Object> datos3 = new ArrayMap<>();
        datos3.put("titulo","SOAT");
        Map<String,Object> datos4 = new ArrayMap<>();
        datos4.put("titulo","Tarjeta de propiedad frontal");
        Map<String,Object> datos5 = new ArrayMap<>();
        datos5.put("titulo","Tarjeta de propiedad reversa");
        ArrayList<Map<String,Object>> data= new ArrayList<>();
        if (getdriverLicense(prefs))
            datos.put("estado","true");
       /* if (getcriminalRecodCert(prefs))
            datos1.put("estado","true");
        if (getpoliceRecordCert(prefs))
            datos2.put("estado","true");*/
        if (getsoat(prefs))
            datos3.put("estado","true");
        if (getpropertyCardForward(prefs))
            datos4.put("estado","true");
        if (getpropertyCardBack(prefs))
            datos5.put("estado","true");

        data.add(datos);
       // data.add(datos1);
       // data.add(datos2);
        data.add(datos3);
        data.add(datos4);
        data.add(datos5);
        try {
            adapterRview.notifyDataSetChanged();
        }catch (Exception e){

        }

        adapterRview = new Recycler_tipo_socio( getContext(), R.layout.dialog_opciones_datos_requeridos,SEND_CLAVE_ADAPTER_DATOS_REQUERIDOS,data , new Recycler_tipo_socio.OnItemClickListener2() {
            @Override
            public void OnClickListener2(Map<String, Object> comentarios, int adapterPosition, View vista) {
                adapterRview.notifyDataSetChanged();
                vista.setEnabled(false);
                vista.setBackgroundColor(Color.BLUE);
                comunicateFrag.updateNotificatones(true, "pasos_requeridos",new f_pasos_requeridos(adapterPosition+1,comentarios.get("titulo").toString()));
            }
        } );

        recyclerView_info.setAdapter(adapterRview);
        /*refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Esto se ejecuta cada vez que se realiza el gesto
                data.clear();
                Map<String,Object> datos = new ArrayMap<>();
                datos.put("titulo","Licencia de Conducir");
        Map<String,Object> datos1 = new ArrayMap<>();
        datos1.put("titulo","Certificado de antecedentes penales");
        Map<String,Object> datos2 = new ArrayMap<>();
        datos2.put("titulo","Certificado de antecedentes policiales");
                Map<String,Object> datos3 = new ArrayMap<>();
                datos3.put("titulo","SOAT");
                Map<String,Object> datos4 = new ArrayMap<>();
                datos4.put("titulo","Tarjeta de propiedad frontal");
                Map<String,Object> datos5 = new ArrayMap<>();
                datos5.put("titulo","Tarjeta de propiedad reversa");
                ArrayList<Map<String,Object>> data= new ArrayList<>();
                if (getdriverLicense(prefs))
                    datos.put("estado","true");

                if (getsoat(prefs))
                    datos3.put("estado","true");
                if (getpropertyCardForward(prefs))
                    datos4.put("estado","true");
                if (getpropertyCardBack(prefs))
                    datos5.put("estado","true");

                data.add(datos);
                // data.add(datos1);
                // data.add(datos2);
                data.add(datos3);
                data.add(datos4);
                data.add(datos5);
                adapterRview.notifyDataSetChanged();
            }
        });*/
        return view;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fpr_btn_finalizar:
                if (   (getaccountActivate(prefs)) ){

                }else{
                    if (datosLlenadossoatetc(prefs)){
                        toastLong(getActivity(),"Espere confirmacion de la cuenta.");

                    }else {
                        toastLong(getActivity(),"Llenar datos mas tarde.");
                    }
                }

                comunicateFrag.updateNotificatones(false,"mapaInicio",new mapaInicio());
                break;
        }
    }
    @Override
    public void onAttach(@NonNull Context context) {
        try {
            comunicateFrag = (updateListenerNotifications) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnFragmentInteractionListener");
        }
        super.onAttach(context);
    }

}
