package com.evans.technologies.conductor.fragments.fragment_notificaciones;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.evans.technologies.conductor.R;
import com.evans.technologies.conductor.data.network.service.auth.ClienteRetrofit;
import com.evans.technologies.conductor.utils.Adapters.adapter_rv_notificaciones;
import com.evans.technologies.conductor.utils.ComunicacionesRealTime.OnclickItemListener;
import com.evans.technologies.conductor.model.chats;
import com.evans.technologies.conductor.model.infoDriver;
import com.evans.technologies.conductor.model.notification.notification;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.evans.technologies.conductor.utils.UtilsKt.getDriverId_Prefs;
import static com.evans.technologies.conductor.utils.UtilsKt.setClaseActual;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_notificaciones_rv extends Fragment {
    private RecyclerView.Adapter adapterRview;
    SharedPreferences prefs;
    private View view;

    @BindView(R.id.ffnrv_rv_lista_notificaciones)
    RecyclerView rv_chat;
    @BindView(R.id.ffnrv_progressBar)
    ProgressBar progressBar;
    @BindView(R.id.ffnrv_btn_cargar_notificaciones)
    Button cargarMas;
    @BindView(R.id.swipeRefresh_Notify)
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<notification> notifications= new ArrayList<>();
    public fragment_notificaciones_rv() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        prefs = getContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        setClaseActual(prefs,new fragment_notificaciones_rv().toString());
        view= inflater.inflate(R.layout.fragment_fragment_notificaciones_rv, container, false);
        ButterKnife.bind(this,view);
        progressBar.setVisibility(View.VISIBLE);
        getllamarNotificaciones();
        rv_chat.setLayoutManager( new LinearLayoutManager(getContext()) );
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Esto se ejecuta cada vez que se realiza el gesto
                Call<infoDriver> allNotify= ClienteRetrofit.getInstance().getApi().getAllNotifys(getDriverId_Prefs(prefs));
                allNotify.enqueue(new Callback<infoDriver>() {
                    @Override
                    public void onResponse(Call<infoDriver> call, Response<infoDriver> response) {
                        swipeRefreshLayout.setRefreshing(false);
                        if (response.body()!=null){
                            notifications.clear();
                            notifications.addAll(response.body().getNotificacion());
                            adapterRview.notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onFailure(Call<infoDriver> call, Throwable t) {
                        Log.e("jsonAll", "fallo"+ t.getMessage());
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
        return view;
    }

    public ArrayList<notification> getllamarNotificaciones() {
        Log.e("jsonAll","entro  "+getDriverId_Prefs(prefs));
        Call<infoDriver> allNotify= ClienteRetrofit.getInstance().getApi().getAllNotifys(getDriverId_Prefs(prefs));
        allNotify.enqueue(new Callback<infoDriver>() {
            @Override
            public void onResponse(Call<infoDriver> call, Response<infoDriver> response) {
                //Log.e("jsonAll",response.body().getNotificacion().get(2).getDescription()+"\n"+response.code());
                    notifications=response.body().getNotificacion();
                progressBar.setVisibility(View.GONE);
                adapterRview = new adapter_rv_notificaciones(getContext(), R.layout.dialog_layout_rv_notificaciones,notifications, new OnclickItemListener() {
                    @Override
                    public void itemClickNotify(notification notifica, int adapterPosition) {
                       notificacionVisto(notifica);

                    }

                    @Override
                    public void itemClickChat(chats chat, int adapterPosition) {

                    }
                });
                rv_chat.setAdapter(adapterRview);
                }

            @Override
            public void onFailure(Call<infoDriver> call, Throwable t) {
                Log.e("jsonAll", "fallo"+ t.getMessage());
            }
        });
        return notifications;
    }
    private void notificacionVisto(notification notifica) {
        Call<infoDriver> change= ClienteRetrofit.getInstance().getApi().changeStatusNotify(notifica.getMessageID(),true);
        change.enqueue(new Callback<infoDriver>() {
            @Override
            public void onResponse(Call<infoDriver> call, Response<infoDriver> response) {
                if (response.isSuccessful()){
                    if (response.body().isOk()){
                        Bundle bundle = new Bundle();
                        bundle.putString("nombre", notifica.getUser());
                        bundle.putString("fecha", notifica.getFHregister());
                        bundle.putString("titulo", notifica.getTitle());
                        bundle.putBoolean("estado", notifica.getState());
                        bundle.putString("MessageID", notifica.getMessageID());
                        bundle.putString("mensaje", notifica.getDescription());
                        Fragment newFragment = new fragment_notificaciones_cuerpo();
                        newFragment.setArguments(bundle);
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.main_layout_change_fragment, newFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        Log.e("correcto","notificacion evnciada con exito");
                    }
                }
                Log.e("correcto",response.code()+"");
            }

            @Override
            public void onFailure(Call<infoDriver> call, Throwable t) {

            }
        });
    }
}
