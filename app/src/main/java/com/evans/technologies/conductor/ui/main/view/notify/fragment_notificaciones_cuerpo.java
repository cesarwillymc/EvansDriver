package com.evans.technologies.conductor.ui.main.view.notify;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.evans.technologies.conductor.R;
import com.evans.technologies.conductor.data.network.service.auth.ClienteRetrofit;
import com.evans.technologies.conductor.model.infoDriver;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.evans.technologies.conductor.utils.UtilsKt.setClaseActual;
import static com.evans.technologies.conductor.utils.UtilsKt.toastLong;
import static com.evans.technologies.conductor.utils.UtilsKt.toastShort;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_notificaciones_cuerpo extends Fragment implements  View.OnClickListener{

    @BindView(R.id.ffnc_btn_aceptar_notificacion)
    Button acceptNotificacion;
    @BindView(R.id.ffnc_btn_borrar_notificacion)
    Button borrarNotifi;
    @BindView(R.id.ffnc_txt_cuerpo_notificacion)
    TextView cuerpo;
    @BindView(R.id.ffnc_txt_fecha_notificacion)
    TextView fecha;
    @BindView(R.id.ffnc_txt_titulo_notificacion)
    TextView titulo;
    @BindView(R.id.ffnc_txt_nombre_notificacion)
    TextView nombre;


    SharedPreferences prefs;
    private View view;
    String id;
    public fragment_notificaciones_cuerpo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        prefs = getContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        setClaseActual(prefs,new fragment_notificaciones_cuerpo().toString());
        view= inflater.inflate(R.layout.fragment_fragment_notificaciones_cuerpo, container, false);
        ButterKnife.bind(this,view);
        fecha.setText(getArguments().getString("fecha"));
        cuerpo.setText(getArguments().getString("mensaje"));
        titulo.setText(getArguments().getString("titulo"));
        nombre.setText(getArguments().getString("nombre"));
        id=getArguments().getString("MessageID");
        acceptNotificacion.setOnClickListener(this);
        borrarNotifi.setOnClickListener(this);

        Log.e("MessageID",id);
        return view;
    }


    private void deleteNotify() {
        Call<infoDriver> deleteNotify= ClienteRetrofit.getInstance().getApi().deleteNotify(id.trim());
        deleteNotify.enqueue(new Callback<infoDriver>() {
            @Override
            public void onResponse(Call<infoDriver> call, Response<infoDriver> response) {
                if (response.isSuccessful()){
                    if (response.body().isOk()){
                        toastShort(getActivity(),"Borrado correctamente");
                       Fragment newFragment = new fragment_notificaciones_rv();
                       FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                       transaction.replace(R.id.main_layout_change_fragment, newFragment);
                       transaction.addToBackStack(null);
                       transaction.commit();
                    }
                }else{
                    toastLong(getActivity(),"Error al borrar");
                }
                Log.e("correcto",response.code()+"");
            }

            @Override
            public void onFailure(Call<infoDriver> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ffnc_btn_aceptar_notificacion:
                Fragment newFragment = new fragment_notificaciones_rv();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_layout_change_fragment, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.ffnc_btn_borrar_notificacion:
                deleteNotify();
                break;

        }
    }
}
