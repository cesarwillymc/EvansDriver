package com.evans.technologies.conductor.fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.evans.technologies.conductor.R;
import com.evans.technologies.conductor.data.network.service.auth.ClienteRetrofit;
import com.evans.technologies.conductor.model.Driver;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.evans.technologies.conductor.utils.UtilsKt.detectar_formato;
import static com.evans.technologies.conductor.utils.UtilsKt.getDriverId_Prefs;
import static com.evans.technologies.conductor.utils.UtilsKt.getImageRotate;
import static com.evans.technologies.conductor.utils.UtilsKt.getPath;
import static com.evans.technologies.conductor.utils.UtilsKt.setClaseActual;
import static com.evans.technologies.conductor.utils.UtilsKt.setEnvioVocher;
import static com.evans.technologies.conductor.utils.UtilsKt.toastLong;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_recargar_evans extends Fragment implements View.OnClickListener {

    private View view;


    File file;
    String capturePath;
    Uri doc;
    public fragment_recargar_evans() {
        // Required empty public constructor
    }
    @BindView(R.id.ffre_btn_enviar_data)
    Button enviar_data;
    @BindView(R.id.ffre_edit_txt_fecha)
    EditText edt_fecha;
    @BindView(R.id.ffre_edit_txt_monto)
    EditText edt_monto;
    @BindView(R.id.ffre_edit_txt_serie)
    EditText edt_serie;
    @BindView(R.id.ffre_imv_voucher)
    ImageView img_voucher;
    @BindView(R.id.ffre_txt_titulo_principal)
    TextView txt_principal;
    @BindView(R.id.ffre_txt_titulo_secundario)
    TextView txt_secundario;
    @BindView(R.id.ffre_progressbar)
    ProgressBar progressBar;
    SharedPreferences prefs;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        prefs = getContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        setClaseActual(prefs,new fragment_recargar_evans().toString());
        view= inflater.inflate(R.layout.fragment_fragment_recargar_evans, container, false);
        ButterKnife.bind(this,view);
        img_voucher.setOnClickListener(this);
        enviar_data.setOnClickListener(this);
        return view;
    }
    public boolean datosVacios(){
        if(edt_fecha.getText().toString().equals("")||edt_fecha.getText().toString()!=null){
            edt_fecha.setError("Completa el campo correctamente");
            return true;
        }
        if(edt_monto.getText().toString().equals("")||edt_monto.getText().toString()!=null){
            edt_monto.setError("Completa el campo correctamente");
            return true;
        }
        if(edt_serie.getText().toString().equals("")||edt_serie.getText().toString()!=null){
            edt_serie.setError("Completa el campo correctamente");
            return true;
        }
        return false;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ffre_btn_enviar_data:
                if (file!=null){
                    if (!datosVacios()) {
                        progressBar.setVisibility(View.VISIBLE);
                        send_txt_datos();
                    }
                }else{
                    Toast.makeText(getActivity(),"Insertar imagen",Toast.LENGTH_LONG).show();
                }

                break;
            case  R.id.ffre_imv_voucher:

                final CharSequence[] opciones = {"Tomar Foto", "Cargar Imagen"};
                final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(getContext());

                alertOpciones.setTitle("Seleccione una opción:");
                alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            if (opciones[i].equals("Tomar Foto")){
                                file=null;
                                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                                        == PackageManager.PERMISSION_GRANTED) {
                                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                                    StrictMode.setVmPolicy(builder.build());
                                    String directoryPath = Environment.getExternalStorageDirectory() + "/" + "evanstechnologiesdriver" + "/";
                                    String filePath = directoryPath+Long.toHexString(System.currentTimeMillis())+".jpeg";
                                    File data = new File(directoryPath);

                                    if (!data.exists()) {
                                        data.mkdirs();
                                    }
                                    file=new File(filePath);
                                    capturePath = filePath; // you will process the image from this path if the capture goes well
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    intent.putExtra( MediaStore.EXTRA_OUTPUT, Uri.fromFile( new File(filePath) ) );
                                    startActivityForResult(intent, 20);
                                } else {
                                    if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                            Manifest.permission.CAMERA)) {
                                        ActivityCompat.requestPermissions(getActivity(),
                                                new String[]{Manifest.permission.CAMERA},
                                                20);
                                    }
                                }
                            } else {
                                file=null;
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        30);

                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                intent.setType("image/");
                                startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicación"),10);
                            }
                        }catch (Exception e){
                            Log.e("errorcamera",e.getMessage());
                        }

                    }
                });

                alertOpciones.show();
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        30);
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA},
                        20);
                break;
        }
    }
    private MultipartBody.Part guardarFotoEnArchivo(String name) {
        MultipartBody.Part body=null;
        Log.e("subir_imagen",file.getPath() +""+file.getParent() +""+file.getAbsolutePath() +""+file.getPath() +"");
        if (detectar_formato(file.getPath()).equals("ninguno")){
            toastLong(getActivity(),"Error formato no valido");

        }else {
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("image/"+detectar_formato(file.getPath())),
                            file
                    );
            Log.e("subir_imagen","nombre"+file.getName());
            body =
                    MultipartBody.Part.createFormData(name, file.getName(), requestFile);
        }
        return body;
    }
    private void send_data_img(String idPago) {
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "baucherImg");
        Call<Driver> retrofit= ClienteRetrofit.getInstance().getApi()
                .insert_voucher(idPago,guardarFotoEnArchivo("baucherImg"),name);
        retrofit.enqueue(new Callback<Driver>() {
            @Override
            public void onResponse(Call<Driver> call, Response<Driver> response) {
                if (response.isSuccessful()){
                    setEnvioVocher(prefs,true);
                    progressBar.setVisibility(View.GONE);
                    toastLong(getActivity(),"Se enviaron los datos correctamente");
                    Log.e("pasos_requeridos","Enviado Correctamente la imagen");


                }else {
                    toastLong(getActivity(),"Error en guardar la imagen");
                    Log.e("pasos_requeridos","error"+ response.code() +response.message());
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Driver> call, Throwable t) {
                toastLong(getActivity(),"Error "+t.getMessage());
                Log.e("pasos_requeridos","error"+t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void send_txt_datos() {
        Call<Driver>  enviarData= ClienteRetrofit.getInstance().getApi()
                .send_data_voucher(getDriverId_Prefs(prefs),edt_fecha.getText().toString().trim(),
                        edt_monto.getText().toString().trim(),edt_serie.getText().toString().trim());
        enviarData.enqueue(new Callback<Driver>() {
            @Override
            public void onResponse(Call<Driver> call, Response<Driver> response) {
                if (response.isSuccessful()){
                    if (response.body()!=null){
                        toastLong(getActivity(),response.body().getIdPago());
                        send_data_img(response.body().getIdPago());
                    }

                }else{
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Driver> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case 10: //10 -> Seleccionar de la galería
                    try {
                        doc= data.getData();
                        Log.e("subir_imagen",doc.toString());
                        //takePhoto.setImageURI(doc);
                        Bitmap imagen = BitmapFactory.decodeFile( getPath(getActivity(),doc));
                        img_voucher.setImageBitmap(getImageRotate(getPath(getActivity(),doc),imagen));
                        if (doc!=null)
                            file = new File(getPath(getActivity(),doc));

                    }catch (Exception e){
                        Log.e("errorCamera",e.getMessage());
                    }


                    break;
                case 20: //20 -> Tomar nueva foto
                    try {
                        if(file.exists()){

                            Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                            img_voucher.setImageBitmap(getImageRotate(file.getAbsolutePath(),myBitmap));

                        }
                    }catch (Exception e){
                        Log.e("errorCamera",e.getMessage());
                    }



                    break;
            }
        }

    }
}
