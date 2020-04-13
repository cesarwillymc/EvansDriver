package com.evans.technologies.conductor.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.evans.technologies.conductor.R;
import com.evans.technologies.conductor.ui.main.view.registerCar.pasos_requeridos;
import com.evans.technologies.conductor.utils.Adapters.Recycler_tipo_socio;
import com.evans.technologies.conductor.utils.ComunicacionesRealTime.updateListenerNotifications;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.evans.technologies.conductor.utils.UtilsKt.setClaseActual;
import static com.evans.technologies.conductor.utils.UtilsKt.setTipoSocio_init;
import static com.evans.technologies.conductor.utils.constantes.AppConstants.SEND_CLAVE_ADAPTER_TIPO_SOCIO;


/**
 * A simple {@link Fragment} subclass.
 */
public class tipo_Socio extends Fragment implements  View.OnClickListener{
    RecyclerView recyclerView_info;
    View view;
    updateListenerNotifications comunicateFrag;
    private RecyclerView.Adapter adapterRview;
    SharedPreferences prefs;
    @BindView(R.id.fts_btn_cntinuar)
    Button continuar;
    int tipo=0;
    public tipo_Socio() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        prefs = getContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        setClaseActual(prefs,new tipo_Socio().toString());
        view= inflater.inflate(R.layout.fragment_tipo__socio, container, false);
        ButterKnife.bind(this,view);
        recyclerView_info=(RecyclerView) view.findViewById( R.id.fts_rcv_tipo_socio ) ;
        LinearLayoutManager layoutManager= new LinearLayoutManager( getContext() );
        RecyclerView.LayoutManager recycler_view_manager_info=layoutManager;
        recyclerView_info.setLayoutManager( recycler_view_manager_info );
        continuar.setOnClickListener(this);
        Map<String,Object> datos = new ArrayMap<>();
        datos.put("tipo","Socio conductor con vehiculo Propio");
        datos.put("desc","Tengo un vehiculo de 4 puertas que conducire yo mismo");
        ArrayList<Map<String,Object>> data= new ArrayList<>();
        data.add(datos);
        adapterRview = new Recycler_tipo_socio( getContext(), R.layout.dialog_opciones_tipo_socio,SEND_CLAVE_ADAPTER_TIPO_SOCIO,data , new Recycler_tipo_socio.OnItemClickListener2() {
            @Override
            public void OnClickListener2(Map<String, Object> comentarios, int adapterPosition, View vista) {
                adapterRview.notifyDataSetChanged();
                vista.setEnabled(false);
                vista.setBackgroundColor(Color.BLUE);
                tipo=adapterPosition+1;

            }
        } );

        recyclerView_info.setAdapter(adapterRview);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fts_btn_cntinuar:
                if (tipo!=0){
                    //Call<Driver> llamar= RetrofitClient.getInstance().getApi().enviar_tipo_socio(getDriverId_Prefs(prefs),getTipoSocio_init(prefs));
                    //  llamar.enqueue(new Callback<Driver>() {
                    //     @Override
                    //     public void onResponse(Call<Driver> call, Response<Driver> response) {
                    //         if (response.isSuccessful()){
                    setTipoSocio_init(prefs,1);
                    comunicateFrag.updateNotificatones(true,"tipo_Socio",new pasos_requeridos());
                    //           }
                    //    }

                    //     @Override
                    //    public void onFailure(Call<Driver> call, Throwable t) {

                    //    }
                    //  });
                }
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
