package com.evans.technologies.conductor.fragments.fragment_pasos_requeridos;


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

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.evans.technologies.conductor.R;
import com.evans.technologies.conductor.data.network.service.auth.ClienteRetrofit;
import com.evans.technologies.conductor.utils.ComunicacionesRealTime.updateListenerNotifications;
import com.evans.technologies.conductor.fragments.pasos_requeridos;
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
import static com.evans.technologies.conductor.utils.UtilsKt.setcriminalRecodCert;
import static com.evans.technologies.conductor.utils.UtilsKt.setdriverLicense;
import static com.evans.technologies.conductor.utils.UtilsKt.setpoliceRecordCert;
import static com.evans.technologies.conductor.utils.UtilsKt.setpropertyCardBack;
import static com.evans.technologies.conductor.utils.UtilsKt.setpropertyCardForward;
import static com.evans.technologies.conductor.utils.UtilsKt.setsoat;
import static com.evans.technologies.conductor.utils.UtilsKt.toastLong;

/**
 * A simple {@link Fragment} subclass.
 */
public class f_pasos_requeridos extends Fragment implements  View.OnClickListener{

    int id_solicitud;
    String titulo_text;
    View view;
    File file;
    String capturePath;
    @BindView(R.id.ffpr_btn_subit)
    Button enviar_imagen_btn;
    @BindView(R.id.ffpr_imv_take_photo)
    ImageView takePhoto;
    @BindView(R.id.ffpr_titulo)
    TextView titulo;
    @BindView(R.id.fpr_progress)
    ProgressBar progressBar;
    Uri doc;
    SharedPreferences prefs;
    updateListenerNotifications comunicateFrag;
    public f_pasos_requeridos() {
        // Required empty public constructor
    }
    public f_pasos_requeridos(int id,String titulo){
        id_solicitud=id;
        this.titulo_text=titulo;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        prefs = getContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        view= inflater.inflate(R.layout.fragment_f_pasos_requeridos, container, false);
        ButterKnife.bind(this,view);
        getTitulo();
        takePhoto.setOnClickListener(this);
        Log.e("updatenot",""+id_solicitud);
        enviar_imagen_btn.setOnClickListener(this);

        return view;
    }

    private void getTitulo() {
        titulo.setText(titulo_text);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ffpr_btn_subit:

                if (file!=null){
                    progressBar.setVisibility(View.VISIBLE);
                    switch (id_solicitud){
                        case 1:
                            licencia();
                            break;
                        case 2:
                            Soat();
                            // cert_penal();
                            break;
                        case 3:
                            propertyCardForward();
                            // cert_policial();
                            break;
                        case 4:
                            propertyCardBack();
                            break;
                     /*   case 5:


                            break;
                        case 6:

                            break;*/
                    }
                }else {
                    toastLong(getActivity(),"Ingrese una imagen.");
                }

                break;
            case R.id.ffpr_imv_take_photo:

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
                                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
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
    private void propertyCardBack() {
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "propertyCardBack");
        Call<Driver> retrofit= ClienteRetrofit.getInstance().getApi()
                .gdd_propertyCardBack(getDriverId_Prefs(prefs),guardarFotoEnArchivo("propertyCardBack"),name);
        retrofit.enqueue(new Callback<Driver>() {
            @Override
            public void onResponse(Call<Driver> call, Response<Driver> response) {
                if (response.isSuccessful()){
                    Log.e("pasos_requeridos","Enviado Correctamente la imagen");
                    setpropertyCardBack(prefs);
                    comunicateFrag.updateNotificatones(true, "mapaInicio",new pasos_requeridos());
                }else {
                    Log.e("pasos_requeridos","error"+ response.code() +response.message());
                }
            }

            @Override
            public void onFailure(Call<Driver> call, Throwable t) {
                Log.e("pasos_requeridos","error"+t.getMessage());
            }
        });
    }
    private void propertyCardForward() {
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "propertyCardForward");
        Call<Driver> retrofit= ClienteRetrofit.getInstance().getApi()
                .gdd_propertyCardForward(getDriverId_Prefs(prefs),guardarFotoEnArchivo("propertyCardForward"),name);
        retrofit.enqueue(new Callback<Driver>() {
            @Override
            public void onResponse(Call<Driver> call, Response<Driver> response) {
                if (response.isSuccessful()){
                    Log.e("pasos_requeridos","Enviado Correctamente la imagen");
                    setpropertyCardForward(prefs);
                    comunicateFrag.updateNotificatones(true, "mapaInicio",new pasos_requeridos());
                }else {
                    Log.e("pasos_requeridos","error"+ response.code() +response.message());
                }
            }

            @Override
            public void onFailure(Call<Driver> call, Throwable t) {
                Log.e("pasos_requeridos","error"+t.getMessage());
            }
        });
    }

    private MultipartBody.Part guardarFotoEnArchivo(String name) {
        MultipartBody.Part body=null;

        //MediaType.parse("multipart/form-data")
        Log.e("subir_imagen",file.getPath() +""+file.getParent() +""+file.getAbsolutePath() +""+file.getPath() +"");
        if (detectar_formato(file.getPath()).equals("ninguno")){
            toastLong(getActivity(),"Error formato no valido");

        }else {
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("image/"+detectar_formato(file.getPath())),
                            file
                    );
            Log.e("subir_imagen","nombre"+file.getName());
            // MultipartBody.Part is used to send also the actual file name
            body =
                    MultipartBody.Part.createFormData(name, file.getName(), requestFile);


        }
        return body;
    }

    private void Soat() {
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "SOAT");
        Call<Driver> retrofit= ClienteRetrofit.getInstance().getApi()
                .gdd_soat(getDriverId_Prefs(prefs),guardarFotoEnArchivo("SOAT"),name);
        retrofit.enqueue(new Callback<Driver>() {
            @Override
            public void onResponse(Call<Driver> call, Response<Driver> response) {
                if (response.isSuccessful()){
                    Log.e("pasos_requeridos","Enviado Correctamente la imagen");
                    progressBar.setVisibility(View.GONE);
                    setsoat(prefs);
                    comunicateFrag.updateNotificatones(true, "mapaInicio",new pasos_requeridos());
                }else {
                    Log.e("pasos_requeridos","error"+ response.code() +response.message());
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Driver> call, Throwable t) {
                Log.e("pasos_requeridos","error"+t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void cert_policial() {
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "policeRecordCert");
        Call<Driver> retrofit= ClienteRetrofit.getInstance().getApi()
                .gdd_policeRecordCert(getDriverId_Prefs(prefs),guardarFotoEnArchivo("policeRecordCert"),name);
        retrofit.enqueue(new Callback<Driver>() {
            @Override
            public void onResponse(Call<Driver> call, Response<Driver> response) {
                if (response.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    Log.e("pasos_requeridos","Enviado Correctamente la imagen");
                    setpoliceRecordCert(prefs);
                    comunicateFrag.updateNotificatones(true, "mapaInicio",new pasos_requeridos());
                }else {
                    Log.e("pasos_requeridos","error"+ response.code() +response.message());
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Driver> call, Throwable t) {
                Log.e("pasos_requeridos","error"+t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void cert_penal() {
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "criminalRecodCert");
        Call<Driver> retrofit= ClienteRetrofit.getInstance().getApi()
                .gdd_criminalRecordCert(getDriverId_Prefs(prefs),guardarFotoEnArchivo("criminalRecodCert"),name);
        retrofit.enqueue(new Callback<Driver>() {
            @Override
            public void onResponse(Call<Driver> call, Response<Driver> response) {
                if (response.isSuccessful()){
                    Log.e("pasos_requeridos","Enviado Correctamente la imagen");
                    progressBar.setVisibility(View.GONE);
                    setcriminalRecodCert(prefs);
                    comunicateFrag.updateNotificatones(true, "mapaInicio",new pasos_requeridos());
                }else {
                    Log.e("pasos_requeridos","error"+ response.code() +response.message());
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Driver> call, Throwable t) {
                Log.e("pasos_requeridos","error"+t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void licencia() {
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "driverLicense");
        Call<Driver> retrofit= ClienteRetrofit.getInstance().getApi()
                .gdd_driverLicense(getDriverId_Prefs(prefs),guardarFotoEnArchivo("driverLicense"),name);
        retrofit.enqueue(new Callback<Driver>() {
            @Override
            public void onResponse(Call<Driver> call, Response<Driver> response) {
                if (response.isSuccessful()){
                    setdriverLicense(prefs);
                    progressBar.setVisibility(View.GONE);
                    Log.e("pasos_requeridos","Enviado Correctamente la imagen");

                    comunicateFrag.updateNotificatones(true, "mapaInicio",new pasos_requeridos());


                }else {
                    Log.e("pasos_requeridos","error"+ response.code() +response.message());
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Driver> call, Throwable t) {
                Log.e("pasos_requeridos","error"+t.getMessage());
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
                        takePhoto.setImageBitmap(getImageRotate(getPath(getActivity(),doc),imagen));
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

                            takePhoto.setImageBitmap(getImageRotate(file.getAbsolutePath(),myBitmap));

                        }
                    }catch (Exception e){
                        Log.e("errorCamera",e.getMessage());
                    }



                    break;
            }
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
