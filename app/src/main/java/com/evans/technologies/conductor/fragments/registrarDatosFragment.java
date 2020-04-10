package com.evans.technologies.conductor.fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.evans.technologies.conductor.Activities.MainActivity;
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
import static com.evans.technologies.conductor.utils.UtilsKt.setRutaImagen;
import static com.evans.technologies.conductor.utils.UtilsKt.setTieneInfo;
import static com.evans.technologies.conductor.utils.UtilsKt.setVehiculoInfo;
import static com.evans.technologies.conductor.utils.UtilsKt.toastLong;

/**
 * A simple {@link Fragment} subclass.
 */
public class registrarDatosFragment extends Fragment implements View.OnClickListener{

    View view;
    @BindView(R.id.register_edit_text_brandCar)
    EditText register_edit_text_brandCar;
    @BindView(R.id.register_edit_text_modelCar)
    EditText register_edit_text_modelCar;
    @BindView(R.id.register_edit_text_colorCar)
    EditText register_edit_text_colorCar;
    @BindView(R.id.register_edit_text_yearCar)
    EditText register_edit_text_yearCar;
    @BindView(R.id.register_edit_text_licenseCar)
    EditText register_edit_text_licenseCar;
    @BindView(R.id.register_edit_text_LicenseCategory)
    EditText register_edit_text_LicenseCategory;
    @BindView(R.id.register_button_registrar_datos_adicionales)
    Button register_button_registrar_datos_adicionales;
    private String brandCar,modelCar,colorCar,yearCar,licenseCar,licenseCategory;
    private SharedPreferences prefs;
    @BindView(R.id.btnEditarFoto)
    ImageButton btnEditarFoto;
    @BindView(R.id.imgFotoPerfil)
    ImageView imgFotoPerfil;
    @BindView(R.id.frd_progress_bar)
    ProgressBar progressBar;
    private File file;
    private Uri doc;
    private String capturePath;
    public registrarDatosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_registrar_datos, container, false);
        prefs = getContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        setClaseActual(prefs,new registrarDatosFragment().toString());
        ButterKnife.bind(this,view);
        register_button_registrar_datos_adicionales.setOnClickListener(this);
        btnEditarFoto.setOnClickListener(this);

        return  view;

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case 10: //10 -> Seleccionar de la galería
                    doc= data.getData();
                    Log.e("subir_imagen",doc.toString());
                    imgFotoPerfil.setImageURI(doc);
                    Bitmap imagen = BitmapFactory.decodeFile(getPath(getActivity(),doc));
                    imgFotoPerfil.setImageBitmap(getImageRotate(getPath(getActivity(),doc),imagen));
                    if (doc!=null){
                        file = new File(getPath(getActivity(),doc));
                    }
                    break;
                case 20: //20 -> Tomar nueva foto

                    // bitmap = (Bitmap) data.getExtras().get("data");
                    if(file.exists()){

                        Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        imgFotoPerfil.setImageBitmap(getImageRotate(file.getAbsolutePath(),myBitmap));


                    }

                    break;
            }
        }

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_button_registrar_datos_adicionales:
                register_button_registrar_datos_adicionales.setEnabled(false);

                if (comprobarDatosnoNulos()){

                    if(file!=null) {
                        //  progressBar.setVisibility(View.VISIBLE);
                        guardarFotoEnArchivo();
                    }else {
                        Toast.makeText(getActivity(),"Para continuar, carga alguna foto.",Toast.LENGTH_SHORT ).show();
                    }

                }else{
                    Toast.makeText(getActivity(),"Ingrese su informacion.",Toast.LENGTH_SHORT ).show();
                }

                break;
            case  R.id.btnEditarFoto:
                final CharSequence[] opciones = {"Tomar Foto", "Cargar Imagen"};
                final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(getContext());

                alertOpciones.setTitle("Seleccione una opción:");
                alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (opciones[i].equals("Tomar Foto")){
                            file=null;
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.CAMERA},
                                    150);
                            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                                    == PackageManager.PERMISSION_GRANTED) {
                                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                                StrictMode.setVmPolicy(builder.build());
                                String directoryPath = Environment.getExternalStorageDirectory() + "/" + "evansTaxy" + "/";
                                String filePath = directoryPath+getDriverId_Prefs(prefs)+".jpeg";
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
                    }
                });

                alertOpciones.show();

                break;
        }
    }

    private void guardarFotoEnArchivo() {


        //MediaType.parse("multipart/form-data")
        Log.e("subir_imagen",file.getPath() +""+file.getParent() +""+file.getAbsolutePath() +""+file.getPath() +"");
        if (detectar_formato(file.getPath()).equals("ninguno")){
            toastLong(getActivity(),"Error formato no valido");
            //  progressBar.setVisibility(View.GONE);
        }else {
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("image/"+detectar_formato(file.getPath())),
                            file
                    );
            Log.e("subir_imagen","nombre"+file.getName());
            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("profile", file.getName(), requestFile);
            subir_datos(body);
        }

    }
    private void subir_datos(MultipartBody.Part body){
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(getContext());
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Cargando...");
        progressDoalog.setTitle("Subiendo Datos");
        progressDoalog.setCancelable(false);
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // show it
        progressDoalog.show();
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "profile");
        Log.e("datos ",getDriverId_Prefs(prefs));
        Call<Driver> subir_imagen = ClienteRetrofit.getInstance().getApi()
                .guardarImagenes(getDriverId_Prefs(prefs),body,name);
        subir_imagen.enqueue(new Callback<Driver>() {
            @Override
            public void onResponse(Call<Driver> call, Response<Driver> response) {
                if (response.isSuccessful()){
                    Log.e("subir_imagen",response.code()+""+getDriverId_Prefs(prefs));
                    subir_info();


                }else {
                    //  progressBar.setVisibility(View.GONE);
                    Log.e("subir_imagen",response.code()+"");
                    toastLong(getActivity(),"Error al subir la foto, "+response.message());
                    register_button_registrar_datos_adicionales.setEnabled(true);
                }
                progressDoalog.dismiss();
            }

            @Override
            public void onFailure(Call<Driver> call, Throwable t) {
                register_button_registrar_datos_adicionales.setEnabled(true);
                Log.e("subir_imagen",t.getMessage());
                toastLong(getActivity(),"La conexion esta demorando, revisa tu conexion a internet");
                // progressBar.setVisibility(View.GONE);
                progressDoalog.dismiss();
            }

        });


    }

    public void subir_info(){
        Call<Driver> llamada= ClienteRetrofit.getInstance().getApi()
                .enviarDatosAdicionales(getDriverId_Prefs(prefs),brandCar,modelCar,colorCar,yearCar,licenseCar,licenseCategory);
        llamada.enqueue(new Callback<Driver>() {
            @Override
            public void onResponse(Call<Driver> call, Response<Driver> response) {
                if (response.isSuccessful()){
                    setVehiculoInfo(prefs,brandCar,colorCar,
                            modelCar,licenseCar);
                    if (file.exists()) {
                        setRutaImagen(prefs,file.getPath());
                    }
                    // progressBar.setVisibility(View.GONE);
                    setTieneInfo(prefs,true);
                    register_button_registrar_datos_adicionales.setEnabled(true);
                    Log.e("subir_imagen",response.message());
                    // Log.e("subir_imagen",response.body().getMessage());
                    Log.e("subir_imagen",response.code()+"");
                    Log.e("subir_imagen","id "+getDriverId_Prefs(prefs));
                    Log.e("subir_imagen","subir datos "+response.message());
                    //Log.e("subir_imagen","Tipe"+getContext().getContentResolver().getType(doc));

                    // Log.e("subir_imagen","subir datos "+response.body().getMessage());
                    toastLong(getActivity(),"Se guardaron los datos correctamente");
                    Intent intent= new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                }else {
                    //progressBar.setVisibility(View.GONE);
                    toastLong(getActivity(),"Error al Traer Respuesta, Intente Nuevamente");
                    register_button_registrar_datos_adicionales.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<Driver> call, Throwable t) {
                //progressBar.setVisibility(View.GONE);
                toastLong(getActivity(),"Revise su conexion a internet");
                register_button_registrar_datos_adicionales.setEnabled(true);
            }
        });
    }
    private boolean comprobarDatosnoNulos(){
        brandCar=register_edit_text_brandCar.getText().toString();
        modelCar=register_edit_text_modelCar.getText().toString();
        colorCar=register_edit_text_colorCar.getText().toString();
        yearCar=register_edit_text_yearCar.getText().toString();
        licenseCar=register_edit_text_licenseCar.getText().toString();
        licenseCategory=register_edit_text_LicenseCategory.getText().toString();

        if (brandCar.isEmpty())
        {
            register_edit_text_brandCar.setError("Es necesario ingresar tu Marca de carro.");
            register_edit_text_brandCar.requestFocus();
            return false;
        }
        if (modelCar.isEmpty())
        {
            register_edit_text_modelCar.setError("Es necesario ingresar tus Modelo de carro.");
            register_edit_text_modelCar.requestFocus();
            return false;
        }
        if (colorCar.isEmpty())
        {
            register_edit_text_colorCar.setError("Ingresa tu número de Color de carro.");
            register_edit_text_colorCar.requestFocus();
            return false;
        }
        if (yearCar.isEmpty())
        {
            register_edit_text_yearCar.setError("Es necesario tu año de carro.");
            register_edit_text_yearCar.requestFocus();
            return false;
        }
        if (licenseCar.isEmpty())
        {
            register_edit_text_licenseCar.setError("Es necesario tu placa de carro");
            register_edit_text_licenseCar.requestFocus();
            return false;
        }
        if (licenseCategory.isEmpty())
        {
            register_edit_text_LicenseCategory.setError( "Es necesario tu tipo de licensia");
            register_edit_text_LicenseCategory.requestFocus();
            return false;
        }
        return true;
    }

}
