package com.evans.technologies.conductor.fragments.change_password;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.evans.technologies.conductor.R;
import com.evans.technologies.conductor.data.network.service.auth.ClienteRetrofit;
import com.evans.technologies.conductor.model.Driver;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.evans.technologies.conductor.utils.UtilsKt.getIDrecuperar;
import static com.evans.technologies.conductor.utils.UtilsKt.setNavFragment;
import static com.evans.technologies.conductor.utils.UtilsKt.settokenrecuperar;
import static org.jetbrains.anko.ToastsKt.toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class set_codigo extends Fragment {

    View view;
    SharedPreferences navFragment;
    @BindView(R.id.fsc_btn_next)
    Button next;
    @BindView(R.id.fsc_edtxt_codigo)
    EditText codigo;
    String code;
    @BindView(R.id.progressBar_codigo)
    ProgressBar progressBar;
    public set_codigo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        navFragment= getContext().getSharedPreferences("navFragment", Context.MODE_PRIVATE);
        view= inflater.inflate(R.layout.fragment_set_codigo, container, false);
        ButterKnife.bind(this,view);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code=codigo.getText().toString().trim();
                if (comprobarcampos()){
                    progressBar.setVisibility(View.GONE);
                    Call<Driver> sendCorreo= ClienteRetrofit.getInstance().getApi().sendCodigo_recuperar(getIDrecuperar(navFragment),code);
                    sendCorreo.enqueue(new Callback<Driver>() {
                        @Override
                        public void onResponse(Call<Driver> call, Response<Driver> response) {
                            if (response.body()!=null){
                                Log.e("codigo_set","tiene cuerpo");
                            }
                            try {
                                Log.e("codigo_set",response.code()+"\n"+
                                        getIDrecuperar(navFragment)+"\n"+
                                        code+"\n");
                            }catch (Exception e){

                            }

                            if (response.isSuccessful()){
                                progressBar.setVisibility(View.VISIBLE);
                                setNavFragment(navFragment,new set_codigo().toString());
                                settokenrecuperar(navFragment,code);
                                FragmentManager manager = getActivity().getSupportFragmentManager();
                                manager.beginTransaction().replace(R.id.recuperar_frag,
                                        new changepassword()
                                ).commit();

                            }else{
                                progressBar.setVisibility(View.VISIBLE);
                                toast(getActivity(),"El codigo no es valido");

                            }
                        }

                        @Override
                        public void onFailure(Call<Driver> call, Throwable t) {
                            progressBar.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }


        });
        return view;
    }

    private boolean comprobarcampos() {
        if (code.isEmpty()){
            codigo.setError("El campo esta vacio");
            return false;
        }
        if (code.length()!=5){
            codigo.setError("Codigo invalido el codigo es mayor de 5 digitos");
            return false;
        }

        return true;
    }

}
